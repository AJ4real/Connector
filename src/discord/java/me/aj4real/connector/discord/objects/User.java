package me.aj4real.connector.discord.objects;

import me.aj4real.connector.Task;
import me.aj4real.connector.discord.DiscordConnector;
import me.aj4real.connector.discord.DiscordEndpoints;
import me.aj4real.connector.discord.objects.channel.DirectMessageChannel;
import org.json.simple.JSONObject;

import java.io.IOException;

public class User {
    protected DiscordConnector c;
    protected JSONObject data;
    private final String username, avatarHash, discriminator;
    private final Snowflake id;
    public User(DiscordConnector c, JSONObject data) {
        this.c = c;
        this.data = data;
        if(data.containsKey("user")) this.data = (JSONObject) data.get("user");
        this.username = (String) this.data.get("username");
        this.avatarHash = (String) this.data.get("avatar");
        this.id = Snowflake.of((String) this.data.get("id"));
        this.discriminator = (String) this.data.get("discriminator");
    }

    public Task<DirectMessageChannel> getDirectMessageChannel() {
        return Task.of(() -> {
            try {
                JSONObject payload = new JSONObject();
                payload.put("recipient_id", id.asString());
                JSONObject json = (JSONObject) c.readJson(DiscordEndpoints.DIRECT_MESSAGE_CHANNEL, payload.toString()).getData();
                DirectMessageChannel channel = new DirectMessageChannel(c, json);
                return channel;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    public String getUsername() {
        return this.username;
    }
    public String getAvatarHash() {
        return this.avatarHash;
    }
    public Snowflake getId() {
        return this.id;
    }
    public String getAvatarData() {
        try {
            return DiscordConnector.getImageData(DiscordEndpoints.USER_AVATAR_ICON.fulfil("user_id", id.asString()).fulfil("avatar", avatarHash).getUrl());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
    {
   "nick":"AJ (SchematiQ1)",
   "premium_since":null,
   "joined_at":"2020-11-24T18:09:03.328000+00:00",
   "roles":[
      "747269994550919188",
      "747283897129238629",
      "747298863395635281",
      "762063864321409054",
      "808349553484365834"
   ],
   "pending":false,
   "deaf":false,
   "mute":false,
   "user":{
      "id":"246693248423165955",
      "avatar":"89883b59e2bd28a243dda61c2b58ede9",
      "public_flags":0,
      "username":"AJ_4real",
      "discriminator":"6745"
   },
   "is_pending":false
}
     */

}
