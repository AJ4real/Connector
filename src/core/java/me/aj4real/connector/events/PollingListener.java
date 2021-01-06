package me.aj4real.connector.events;

import me.aj4real.connector.Connector;

public abstract class PollingListener extends Listener {
    protected final String url;
    public PollingListener(Connector c, EventHandler handler, String url) {
        super(c, handler);
        this.url = url;
    }

    public abstract <T extends Event> void listen();

    public abstract void terminate();
}
