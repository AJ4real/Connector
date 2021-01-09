package me.aj4real.connector.events;

import me.aj4real.connector.Connector;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class WebSocketListener extends Listener {
    protected final int port;
    protected final ServerSocket server;
    protected final Socket client;
    protected final ObjectOutputStream out;
    protected final ObjectInputStream in;
    public WebSocketListener(Connector c, EventHandler handler, int port) throws IOException {
        super(c, handler);
        this.port = port;
        this.server = new ServerSocket(port);
        client = server.accept();
        out = new ObjectOutputStream(client.getOutputStream());
        in = new ObjectInputStream(client.getInputStream());
    }

    @Override
    public abstract <T extends Event> void listen();

    @Override
    public abstract void terminate();

    public abstract void sendKeepAlive();
}
