package me.aj4real.connector.discord.specs;

import me.aj4real.connector.Endpoint;
import me.aj4real.connector.Request;
import org.json.simple.JSONObject;

public class ModifyGuildMemberSpec extends Request {
    JSONObject data;

    public ModifyGuildMemberSpec(Endpoint endpoint) {
        super(endpoint);
        this.data = new JSONObject();
    }

    public void setNick(String nick) {
        data.put("nick", nick);
    }

    public void setMuted(boolean muted) {
        data.put("muted", muted);
    }

    public void setDeaf(boolean deaf) {
        data.put("deaf", deaf);
    }

    public void setRoles(String[] roleIds) {
        data.put("roles", roleIds);
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public JSONObject serialize() {
        return this.data;
    }

}
