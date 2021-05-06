package me.aj4real.connector.discord.objects;

import me.aj4real.connector.Task;
import me.aj4real.connector.discord.DiscordConnector;
import org.json.simple.JSONObject;

import javax.naming.OperationNotSupportedException;

public class Bot {
    protected final DiscordConnector c;
    protected final JSONObject data;
    private String summary, name, icon, description,verifyKey;
    private Snowflake id;
    private boolean hook, _public, requireCodeGrant;
    private String[] scopes; //TODO
    public Bot(DiscordConnector c, JSONObject data) {
        this.c = c;
        this.data = data;
        this.summary = (String) data.get("summary");
        this.name = (String) data.get("name");
        this.icon = (String) data.get("icon");
        this.description = (String) data.get("description");
        this.verifyKey = (String) data.get("verify_key");
        this.id = Snowflake.of((String) data.get("id"));
        this.hook = (boolean) data.get("hook");
        this._public = (boolean) data.get("bot_public");
        this.requireCodeGrant = (boolean) data.get("bot_require_code_grant");
        // data.get("team"); TODO
    }

    public String[] getScopes() throws OperationNotSupportedException {
        //TODO
        throw new OperationNotSupportedException();
    }
    public boolean isPublic() {
        return this._public;
    }
    public boolean isHook() {
        return this.hook;
    }
    public boolean requiresCodeGrant() {
        return this.requireCodeGrant;
    }
    public Snowflake getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }
    public String getDescription() {
        return this.description;
    }
    public String getSummary() {
        return this.summary;
    }
    public String getIcon() {
        return this.icon;
    }
    public String getVerifyKey() {
        return this.verifyKey;
    }
    public User getOwner() {
        return new User(c, (JSONObject) data.get("owner"));
    }
}
