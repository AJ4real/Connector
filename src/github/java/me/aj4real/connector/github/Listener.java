package me.aj4real.connector.github;

import me.aj4real.connector.Connector;
import me.aj4real.connector.Event;
import me.aj4real.connector.Response;
import me.aj4real.connector.github.events.GithubRepositoryEvents;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Listener {
    GithubConnector c;
    String url;
    long refreshDelay;
    Consumer event;
    String etag;

    public <T extends Event> Listener(GithubConnector c, String url, Consumer<T> event) {
        try {
            this.c = c;
            this.url = url;
            this.event = event;
            Response r = c.readJson(url);
            this.etag = r.getHeaders().get("ETag").get(0);
            this.refreshDelay = (Long.valueOf(r.getHeaders().get("X-Poll-Interval").get(0)) * 1000) + 1000;
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private <T extends Event> void start() throws IOException {
        try {
            Thread.sleep(refreshDelay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Map<String, String> headers = new HashMap<>();
        headers.put("If-None-Match", etag);
        Response r = c.readJson(url, Connector.REQUEST_METHOD.GET, null, headers);
        etag = r.getHeaders().get("ETag").get(0);
        System.out.println(etag);

        if (r.getData() != null) {
            for (Object o : (JSONArray) r.getData()) {
                JSONObject jo = (JSONObject) o;
                try {
                    T e = (T) GithubRepositoryEvents.valueOf((String) jo.get("type")).getEventClass().getConstructor(GithubConnector.class, JSONObject.class).newInstance(c, jo);

                    event.accept(e);
                } catch (IllegalArgumentException e) {

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println(r.getHeaders().get("X-RateLimit-Remaining").get(0));
        start();

    }
}