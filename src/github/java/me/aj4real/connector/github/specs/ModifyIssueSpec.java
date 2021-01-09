package me.aj4real.connector.github.specs;

import me.aj4real.connector.Connector;
import me.aj4real.connector.Request;
import org.json.simple.JSONObject;

public class ModifyIssueSpec extends Request {
    private JSONObject data;
    public ModifyIssueSpec(String url) {
        super(url);
        data = new JSONObject();
    }

    public void setTitle(String title) {
        data.put("title", title);
    }

    public void setBody(String body) {
        data.put("body", body);
    }

    public void setState(State state) {
        data.put("state", state.name().toLowerCase());
    }

    @Override
    public Connector.REQUEST_METHOD getRequestMethod() {
        return Connector.REQUEST_METHOD.PATCH;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public JSONObject serialize() {
        return data;
    }
    public enum State {
        OPEN,
        CLOSED;
    }
}
