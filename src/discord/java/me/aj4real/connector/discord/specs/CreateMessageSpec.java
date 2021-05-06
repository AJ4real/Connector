package me.aj4real.connector.discord.specs;

import me.aj4real.connector.Endpoint;
import me.aj4real.connector.Request;
import me.aj4real.connector.discord.objects.Message;
import me.aj4real.connector.discord.objects.Snowflake;
import org.json.simple.JSONObject;

public class CreateMessageSpec extends Request {
    JSONObject data = new JSONObject();
    public CreateMessageSpec(Endpoint endpoint) {
        super(endpoint);
    }

    public void setContent(String message) {
        data.put("content", message);
    }

    public void setReference(Message message) {
        setReference(message.getId(), message.getChannelId(), message.getGuildId().isPresent() ? message.getGuildId().get() : null);
    }
    public void setReference(Snowflake messageId, Snowflake channelId, Snowflake guildId) {
        setReference(messageId.asString(), channelId.asString(), guildId.asString());
    }
    public void setReference(String messageId, String channelId, String guildId) {
        JSONObject ref = new JSONObject();
        ref.put("message_id", messageId);
        ref.put("channel_id", channelId);
        if (guildId != null) ref.put("guild_id", guildId);
        data.put("message_reference", ref);
    }
    public void setTts(boolean tts) {
        data.put("tts", tts);
    }

    @Override
    public boolean isValid() {
        if(data.containsKey("embeds") || data.containsKey("content") || data.containsKey("file")) return true;
        return false;
    }

    @Override
    public JSONObject serialize() {
        return data;
    }
}
