package me.aj4real.connector.discord.events;

import me.aj4real.connector.Logger;
import me.aj4real.connector.discord.DiscordConnector;
import me.aj4real.connector.events.Event;
import me.aj4real.connector.events.EventHandler;
import me.aj4real.connector.events.WebSocketListener;
import me.aj4real.connector.events.WebSocketResponse;
import org.json.simple.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.net.http.WebSocket;

public class DiscordWebSocket extends WebSocketListener {

    private final String token;
    private long sequenceNumber = 0;
    private long interval = 0;
    private String appId, sessionId;

    public DiscordWebSocket(DiscordConnector c, EventHandler handler, String token) {
        super(c, handler, "wss://gateway.discord.gg/?v=6&encoding=json");
        this.token = token;
    }

    public String getAppId() {
        return appId;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    @Override
    public void keepalive(WebSocket webSocket) {
        JSONObject json = new JSONObject();
        json.put("op", 1);
        json.put("s", sequenceNumber);
        json.put("t", null);
        json.put("d", null);
        webSocket.sendText(json.toJSONString(), true);
    }

    @Override
    public void reconnect() {
        init();
        JSONObject json = new JSONObject();
        json.put("op", 6);
        JSONObject d = new JSONObject();
        d.put("token", token);
        d.put("session_id", sessionId);
        d.put("seq", sequenceNumber);
        json.put("d", d);
        Logger.log(Logger.Level.DEBUG, "Sending reconnect request through Gateway");
        getWebSocket().sendText(json.toJSONString(), true);
//        identify(getWebSocket());
    }

    @Override
    public void identify(WebSocket webSocket) {
        JSONObject connectionProperties = new JSONObject();
        connectionProperties.put("$os", System.getProperty("os.name"));
        connectionProperties.put("$browser", "Connector");
        connectionProperties.put("$device", "Connector");
        JSONObject d = new JSONObject();
        d.put("properties", connectionProperties);
        d.put("token", token);
        JSONObject json = new JSONObject();
        json.put("d", d);
        json.put("op", 2);
        Logger.log(Logger.Level.DEBUG, "Sending identification through Gateway");
        webSocket.sendText(json.toJSONString(), true);
    }

    @Override
    public <T extends Event> void handle(WebSocketResponse data) {
        if(data.getType() == WebSocketResponse.Type.ERROR) {
            Throwable throwable = (Throwable) data.getData();
            Logger.handle(throwable);
        }
        if(data.getType() == WebSocketResponse.Type.CLOSE) {

        }
        if (data.getData() instanceof JSONObject) {
            JSONObject json = (JSONObject) data.getData();
            long op = (long) json.get("op");
            if(json.get("s") != null) this.sequenceNumber = (long) json.get("s");
//            if(op != 11 && op != 0) Logger.log(Logger.Level.DEBUG, data.getData().toString());
            switch (Math.toIntExact(op)) {
                case 0:
                    DiscordEvents eventClassifier = DiscordEvents.valueOf((String) json.get("t"));
                    T event = null;
                    try {
                        event = (T) eventClassifier.getEventClass().getConstructor(DiscordConnector.class, JSONObject.class).newInstance(c, json);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        Logger.handle(e);
                    }
                    if(event != null) {
                        Logger.log(Logger.Level.DEBUG, "Fired " + event.getClass().getTypeName());
                        handler.fire(event);
                    }
                    break;
                case 10:
                    this.interval = (long) ((JSONObject)json.get("d")).get("heartbeat_interval");
                    setupKeepalive(interval);
                    break;
            }
            if(json.get("t") != null) {
                if(json.get("t").equals("READY")) {
                    this.appId = (String) ((JSONObject)((JSONObject)json.get("d")).get("application")).get("id");
                    this.sessionId = (String) ((JSONObject)json.get("d")).get("session_id");
                    ready();
                }
            }
        }
    }
}
