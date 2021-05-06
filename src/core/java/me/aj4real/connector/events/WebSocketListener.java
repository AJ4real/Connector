package me.aj4real.connector.events;

import me.aj4real.connector.Connector;
import me.aj4real.connector.Logger;
import me.aj4real.connector.events.websocket.WebsocketResponseReceivedEvent;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CountDownLatch;

public abstract class WebSocketListener extends Listener {

    protected final String url;
    private WebSocket webSocket;
    private WebSocketClient wsc;
    private final CountDownLatch latch;
    private TimerTask keepaliveTask;
    private Timer keepaliveTimer;
    private Thread webSocketThread;
    private boolean alive;

    public WebSocketListener(Connector c, EventHandler handler, String url) {
        super(c, handler);
        this.url = url;
        this.latch = new CountDownLatch(2);
    }

    public <T extends Event> void listen() {
        init();
        lock();
        identify(getWebSocket());
        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void init() {
        Logger.log(Logger.Level.DEBUG, "Initializing WebSocket to " + url);
        if(webSocket != null) {
            terminate();
        }
        wsc = new WebSocketClient(this, latch);
        webSocket = HttpClient
                .newHttpClient()
                .newWebSocketBuilder()
                .buildAsync(URI.create(url), wsc)
                .join();
        alive = true;
    }

    public void lock() {
        if(webSocketThread != null) {
            webSocketThread.interrupt();
            webSocketThread.stop();
        }
        webSocketThread = new Thread(() -> {
            try {
                latch.await();
            } catch (InterruptedException e) {
                Logger.handle(e);
            }
        });
        webSocketThread.start();
    }

    public WebSocket getWebSocket() { return this.webSocket; }

    public WebSocketClient getWebSocketClient() { return this.wsc; }

    public void setupKeepalive(long interval) {
        keepaliveTask = new TimerTask() {
            @Override
            public void run() {
                keepalive(webSocket);
                Logger.log(Logger.Level.DEBUG, "WebSocket keepalive packet sent");
            }
        };
        keepaliveTimer = new Timer();
        keepaliveTimer.schedule(keepaliveTask, interval, interval);
    }

    public abstract void keepalive(WebSocket webSocket);

    public abstract void identify(WebSocket webSocket);

    public abstract void reconnect();

    public void ready() {
        synchronized (this) {
            notify();
        }
    }

    public abstract <T extends Event> void handle(WebSocketResponse data);

    @Override
    public void terminate() {
        alive = false;
        keepaliveTask.cancel();
        keepaliveTimer.cancel();
        webSocket.abort();
    }

    protected static class WebSocketClient implements WebSocket.Listener {
        private final CountDownLatch latch;
        private final WebSocketListener listener;
        private String text = "";
        private ByteBuffer data = null;

        public WebSocketClient(WebSocketListener listener, CountDownLatch latch) {
            this.listener = listener;
            this.latch = latch;
        }


        @Override
        public void onOpen(WebSocket webSocket) {
            WebSocketResponse<String> response = new WebSocketResponse<>(webSocket, WebSocketResponse.Type.OPEN, webSocket.getSubprotocol());
            listener.handle(response);
            WebSocket.Listener.super.onOpen(webSocket);
        }

        @Override
        public CompletionStage<?> onBinary(WebSocket webSocket, ByteBuffer buffer, boolean last) {
            synchronized (webSocket) {
                if(this.data == null) {
                    this.data = buffer;
                } else {
                    this.data = data.put(buffer);
                }
                if(last) {
                    WebSocketResponse<ByteBuffer> response = new WebSocketResponse<>(webSocket, WebSocketResponse.Type.BINARY, data);
                    listener.handle(response);
                    this.data = null;
                }
            }
            return WebSocket.Listener.super.onBinary(webSocket, buffer, last);
        }

        @Override
        public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
            Logger.log(Logger.Level.INFO, "WebSocket has been closed remotely: " + statusCode + " " + reason);
            WebSocketResponse<String> response = new WebSocketResponse<>(webSocket, WebSocketResponse.Type.CLOSE, reason);
            listener.handle(response);
            if(listener.alive) {
                Logger.log(Logger.Level.INFO, "WebSocket attempting to reconnect.");
                listener.reconnect();
            }
            return WebSocket.Listener.super.onClose(webSocket, statusCode, reason);
        }

        @Override
        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
            synchronized (webSocket) {
                try {
                    this.text = this.text + data.toString();
                    if(last) {
                        WebSocketResponse<JSONObject> response = new WebSocketResponse<>(webSocket, WebSocketResponse.Type.TEXT, (JSONObject) JSONValue.parse(text));
                        listener.handle(response);
                        listener.handler.fire(new WebsocketResponseReceivedEvent(listener.c, (JSONObject) JSONValue.parse(text)));
                        this.text = "";
                    }
                } catch (Exception e) {
                    onError(webSocket, e);
                }
            }
            return WebSocket.Listener.super.onText(webSocket, data, last);
        }

        @Override
        public void onError(WebSocket webSocket, Throwable error) {
            WebSocketResponse<Throwable> response = new WebSocketResponse<>(webSocket, WebSocketResponse.Type.ERROR, error);
            listener.handle(response);
            WebSocket.Listener.super.onError(webSocket, error);
        }

        @Override
        public CompletionStage<?> onPing(WebSocket webSocket, ByteBuffer message) {
            WebSocketResponse<ByteBuffer> response = new WebSocketResponse<>(webSocket, WebSocketResponse.Type.PING, message);
            listener.handle(response);
            return WebSocket.Listener.super.onPing(webSocket, message);
        }

        @Override
        public CompletionStage<?> onPong(WebSocket webSocket, ByteBuffer message) {
            WebSocketResponse<ByteBuffer> response = new WebSocketResponse<>(webSocket, WebSocketResponse.Type.PONG, message);
            listener.handle(response);
            return WebSocket.Listener.super.onPing(webSocket, message);
        }
    }
}
