package me.aj4real.connector.github.specs;

import me.aj4real.connector.Connector;
import me.aj4real.connector.Endpoint;
import me.aj4real.connector.Request;
import org.json.simple.JSONObject;

public class CreateIssueSpec extends Request {
    private JSONObject data;
    public CreateIssueSpec(Endpoint endpoint) {
        super(endpoint);
        data = new JSONObject();
    }

    public void setTitle(String title) {
        data.put("title", title);
    }

    public void setBody(String body) {
        data.put("body", body);
    }

    @Override
    public boolean isValid() {
        return data.get("title") != null;
    }

    @Override
    public JSONObject serialize() {
        return data;
    }
}
