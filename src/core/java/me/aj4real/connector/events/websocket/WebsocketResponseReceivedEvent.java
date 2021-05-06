package me.aj4real.connector.events.websocket;

import me.aj4real.connector.Connector;
import me.aj4real.connector.events.Event;
import org.json.simple.JSONObject;

public class WebsocketResponseReceivedEvent extends Event {
    public WebsocketResponseReceivedEvent(Connector c, JSONObject data) {
        super(c, data);
    }
}
