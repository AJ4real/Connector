package me.aj4real.connector.discord.specs;

import me.aj4real.connector.Endpoint;
import me.aj4real.connector.Request;
import me.aj4real.connector.discord.objects.EmbedData;
import org.json.simple.JSONObject;

import java.io.File;

public class InvokeWebhookSpec extends Request {
    private JSONObject data = new JSONObject();
    public InvokeWebhookSpec(Endpoint endpoint) {
        super(endpoint);
    }

    public void setContent(String message) {
        data.put("content", message);
    }

    public void setUsername(String username) {
         data.put("username", username);
    }

    public void setAvatarUrl(String url) {
        data.put("avatar_url", url);
    }

    public void setFile(File file) {
        //TODO
        throw new UnsupportedOperationException();
    }

    public void setEmbeds(EmbedData[] embeds) {
        //TODO
        throw new UnsupportedOperationException();
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
