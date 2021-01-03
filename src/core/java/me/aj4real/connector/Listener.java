package me.aj4real.connector;

public abstract class Listener {
    protected final EventHandler handler;
    protected final Connector c;
    public Listener(Connector c, EventHandler handler) {
        this.handler = handler;
        this.c = c;
    }
    public abstract <T extends Event> void listen();
    public abstract void terminate();
}
