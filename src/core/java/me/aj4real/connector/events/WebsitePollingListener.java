package me.aj4real.connector.events;

import me.aj4real.connector.Connector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class WebsitePollingListener extends PollingListener {

    public WebsitePollingListener(Connector c, EventHandler handler, String url) {
        super(c, handler, url);
    }

    @Override
    public <T extends Event> void listen() {
    }

    @Override
    public void terminate() {

    }
}
