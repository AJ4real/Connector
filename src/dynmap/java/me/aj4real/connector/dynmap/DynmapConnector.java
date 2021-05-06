package me.aj4real.connector.dynmap;

import me.aj4real.connector.Connector;
import me.aj4real.connector.Endpoint;
import org.json.simple.JSONObject;

import java.io.IOException;

public class DynmapConnector extends Connector {
    private final DynmapConfiguration configuration;
    private final Dynmap dynmap;
    private final String configUrl, worldUrl, markersUrl;
    public DynmapConnector(String configUrl, String worldUrl, String markersUrl) throws IOException {
        this.configUrl = configUrl;
        this.worldUrl = worldUrl;
        this.markersUrl = markersUrl;
        JSONObject configJson = (JSONObject) readJson(new Endpoint(Endpoint.HttpMethod.GET, configUrl)).getData();
        this.configuration = new DynmapConfiguration(configJson);
        this.dynmap = new Dynmap(this, configuration);
        JSONObject markersJson = (JSONObject) readJson(new Endpoint(Endpoint.HttpMethod.GET, markersUrl)).getData();
        dynmap.patchMarkers(markersJson);
        DynmapPollingListener worldListener = new DynmapPollingListener(this, this.getHandler(), worldUrl);
        worldListener.listen();
    }
    public DynmapConfiguration getConfiguration() {
        return this.configuration;
    }
    public Dynmap getDynmap() {
        return this.dynmap;
    }
}
