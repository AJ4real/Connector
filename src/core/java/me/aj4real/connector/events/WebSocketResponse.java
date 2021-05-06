package me.aj4real.connector.events;

import java.net.http.WebSocket;

public class WebSocketResponse<T> {
    private WebSocket webSocket;
    private T data;
    private Type type;
    public WebSocketResponse(WebSocket webSocket, Type type, T data) {
        this.webSocket = webSocket;
        this.type = type;
        this.data = data;
    }

    public WebSocket getWebSocket() {
        return this.webSocket;
    }

    public T getData() {
        return this.data;
    }

    public Type getType() {
        return this.type;
    }

    public enum Type {
        OPEN,
        BINARY,
        CLOSE,
        TEXT,
        ERROR,
        PING,
        PONG;
    }
}
