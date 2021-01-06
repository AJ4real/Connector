package me.aj4real.connector.github.objects;

import me.aj4real.connector.github.GithubConnector;
import org.json.simple.JSONObject;

public class Label {
    private final GithubConnector c;
    private final JSONObject data;
    private final long id;
    private final String nodeId, name, description, hexColor;
    private final boolean isDefault;
    public Label(GithubConnector c, JSONObject data) {
        this.c = c;
        this.data = data;
        this.id = (long) data.get("id");
        this.nodeId = (String) data.get("node_id");
        this.name = (String) data.get("name");
        this.description = (String) data.get("description");
        this.hexColor = (String) data.get("color");
        this.isDefault = (boolean) data.get("default");
    }
    public long getId() {
        return this.id;
    }
    public String getNodeId() {
        return this.nodeId;
    }
    public String getDescription() {
        return this.description;
    }
    public String getName() {
        return this.name;
    }
    public String getHexColor() {
        return this.hexColor;
    }
    public boolean isDefault() {
        return this.isDefault;
    }
}