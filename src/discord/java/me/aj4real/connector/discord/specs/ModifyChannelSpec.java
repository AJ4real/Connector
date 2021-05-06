package me.aj4real.connector.discord.specs;

import me.aj4real.connector.Endpoint;
import me.aj4real.connector.Request;
import me.aj4real.connector.discord.objects.Channel;
import org.json.simple.JSONObject;

public class ModifyChannelSpec extends Request {

    JSONObject data;

    public ModifyChannelSpec(Endpoint endpoint) {
        super(endpoint);
        this.data = new JSONObject();
    }

    public void setName(String name) {
        data.put("name", name);
    }

    public void setType(Channel.Type type) {
        data.put("type", type.getId());
    }

    public void setTopic(String topic) {
        data.put("topic", topic);
    }

    public void setBitrate(int bitrate) {
        data.put("bitrate", bitrate);
    }

    public void setUserLimit(int limit) {
        data.put("user_limit", limit);
    }

    public void setUserRateLimit(int limit) {
        data.put("rate_limit_per_user", limit);
    }

    public void setPosition(int position) {
        data.put("position", position);
    }

    public void setNsfw(boolean nsfw) {
        data.put("nsfw", nsfw);
    }

    @Override
    public boolean isValid() {
        return data.get("name") != null;
    }

    @Override
    public JSONObject serialize() {
        return data;
    }
}
