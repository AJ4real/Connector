package me.aj4real.connector.github.events;

import me.aj4real.connector.Connector;
import me.aj4real.connector.Response;
import me.aj4real.connector.events.Event;
import me.aj4real.connector.events.EventHandler;
import me.aj4real.connector.events.PollingListener;
import me.aj4real.connector.github.GithubConnector;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GithubPollingListener extends PollingListener {
    long refreshDelay;
    String etag;
    Thread t;
    boolean running = false;
    public GithubPollingListener(Connector c, EventHandler handler, String url) {
        super(c, handler, url);
        try {
            Response r = c.readJson(url);
            this.etag = r.getHeaders().get("ETag").get(0);
            this.refreshDelay = (Long.valueOf(r.getHeaders().get("X-Poll-Interval").get(0)) * 1000) + 1000;
            listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <T extends Event> void listen() {
        t = new Thread(() -> {
            while(running) {
                try {
                    Thread.sleep(refreshDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Map<String, String> headers = new HashMap<>();
                headers.put("If-None-Match", etag);
                Response r = null;
                try {
                    r = c.readJson(url, Connector.REQUEST_METHOD.GET, null, headers);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                etag = r.getHeaders().get("ETag").get(0);

                if (r.getData() != null) {
                    for (Object o : (JSONArray) r.getData()) {
                        JSONObject jo = (JSONObject) o;
                        String type = (String) jo.get("type");

                        try {
                            T e = (T) GithubRepositoryEvents.valueOf(type).getEventClass().getConstructor(GithubConnector.class, JSONObject.class).newInstance(c, jo);
                            handler.fire(e);
                            System.out.println(type + " fired");
                            System.out.println(jo.toString());
                        } catch (Exception e) {
                            System.out.println("ERR : " + type);
                            System.out.println(jo.toString());
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        t.start();
        running = true;
    }

    @Override
    public void terminate() {
        t.interrupt();
        running = false;
    }
}
