package me.aj4real.connector.discord.specs;

import me.aj4real.connector.Endpoint;
import me.aj4real.connector.Request;
import me.aj4real.connector.discord.DiscordConnector;
import me.aj4real.connector.discord.objects.Snowflake;
import org.json.simple.JSONObject;

import java.io.IOException;

public class ModifyWebhookSpec extends Request {
    JSONObject data = new JSONObject();
    public ModifyWebhookSpec(Endpoint endpoint) {
        super(endpoint);
    }

    public void setName(String name) {
        data.put("name", name);
    }

    public void setAvatarIcon(String url) throws IOException {
        data.put("avatar", DiscordConnector.getImageData(url));
    }
    public void setChannel(Snowflake channelId) {
        data.put("channel_id", channelId.asString());
    }
    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public JSONObject serialize() {
        return data;
    }
}
