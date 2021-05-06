package me.aj4real.connector.dynmap;

import me.aj4real.connector.Connector;
import me.aj4real.connector.Endpoint;
import me.aj4real.connector.Response;
import me.aj4real.connector.dynmap.events.DynmapUpdateEvent;
import me.aj4real.connector.dynmap.events.PlayerChatEvent;
import me.aj4real.connector.dynmap.objects.Player;
import me.aj4real.connector.events.Event;
import me.aj4real.connector.events.EventHandler;
import me.aj4real.connector.events.PollingListener;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;

public class DynmapPollingListener extends PollingListener {

    private long refreshDelay;
    private Thread t;
    private boolean running;
    private long last = System.currentTimeMillis();

    public DynmapPollingListener(Connector c, EventHandler handler, String url) {
        super(c, handler, url);
        this.refreshDelay = ((DynmapConnector)c).getConfiguration().getUpdateRefreshDelay();
    }

    @Override
    public <T extends Event> void listen() {
        t = new Thread(() -> {
            while(running) {
                Response r = null;
                try {
                    r = c.readJson(new Endpoint(Endpoint.HttpMethod.GET, url.replace("{timestamp}", String.valueOf(last))));
                } catch (IOException e) {
                    me.aj4real.connector.Logger.handle(e);
                }
                if (r.getData() != null) {
                    JSONObject payload = (JSONObject) r.getData();
                    ((DynmapConnector)c).getDynmap().patchWorld(payload);
                    JSONArray updates = (JSONArray) payload.get("updates");
                    long newLast = last;
                    for(Object o : updates) {
                        JSONObject eventJson = (JSONObject) o;
                        long last = (long) eventJson.get("timestamp");
                        if (this.last < last) {
                            newLast = last;
                            String type = (String) eventJson.get("type");
                            if(type.equalsIgnoreCase("chat")) {
                                String source = (String) eventJson.get("source");
                                if(source.equalsIgnoreCase("player")) {
                                    Player player = ((DynmapConnector)c).getDynmap().getPlayer((String)eventJson.get("account"));
                                    PlayerChatEvent event = new PlayerChatEvent(c, player, (String) eventJson.get("message"));
                                    handler.fire(event);
                                }
                            }
                            DynmapUpdateEvent event = new DynmapUpdateEvent(c, eventJson);
                            handler.fire(event);
                        }
                    }
                    this.last = newLast;
                }
                try {
                    Thread.sleep(refreshDelay);
                } catch (InterruptedException e) {
                    me.aj4real.connector.Logger.handle(e);
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
