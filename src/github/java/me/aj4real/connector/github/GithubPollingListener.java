package me.aj4real.connector.github;

import me.aj4real.connector.*;
import me.aj4real.connector.github.events.GithubRepositoryEvents;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GithubPollingListener extends PollingListener {
    long refreshDelay;
    String etag;
    Thread t;
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
                    try {
                        T e = (T) GithubRepositoryEvents.valueOf((String) jo.get("type")).getEventClass().getConstructor(GithubConnector.class, JSONObject.class).newInstance(c, jo);
                        handler.fire(e);
                    } catch (Exception e) {
                    }
                }
            }
        });
        t.start();
    }

    @Override
    public void terminate() {
        t.interrupt();
    }
}
