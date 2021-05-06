package me.aj4real.connector.discord.specs;

import me.aj4real.connector.Endpoint;
import me.aj4real.connector.Request;
import me.aj4real.connector.discord.DiscordConnector;
import me.aj4real.connector.discord.objects.Snowflake;
import me.aj4real.connector.discord.objects.Guild;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.Locale;

public class ModifyGuildSpec extends Request {

    private JSONObject data;

    public ModifyGuildSpec(Endpoint endpoint) {
        super(endpoint);
        data = new JSONObject();
    }

    public void setVerificationLevel(int level) {
        data.put("verification_level", level);
    }

    public void setDefaultMessageNotifications(int i) {
        data.put("default_message_notifications", i);
    }

    public void setExplicitContentFilter(int i) {
        data.put("explicit_content_filter", i);
    }

    public void setName(String name) {
        data.put("name", name);
    }

    public void setVoiceRegion(Guild.VoiceRegion region) {
        data.put("region", region.getId());
    }

    public void setAfkChannelId(long id) {
        data.put("afk_channel_id", id);
    }

    public void setTimeout(long seconds) {
        data.put("afk_timeout", seconds);
    }

    public void setOwner(Snowflake snowflake) {
        data.put("owner_id", snowflake.asString());
    }

    public void setSplashUrl(String url) {
        try {
            data.put("splash", DiscordConnector.getImageData(url));
        } catch (IOException e) {
            me.aj4real.connector.Logger.handle(e);
        }
    }

    public void setBannerUrl(String url) {
        try {
            data.put("banner", DiscordConnector.getImageData(url));
        } catch (IOException e) {
            me.aj4real.connector.Logger.handle(e);
        }
    }

    public void setSystemChannelId(Snowflake snowflake) {
        data.put("system_channel_id", snowflake.asString());
    }

    public void setRulesChannelId(Snowflake snowflake) {
        data.put("rules_channel_id", snowflake.asString());
    }

    public void setPublicUpdatesChannelId(Snowflake snowflake) {
        data.put("public_updates_channel_id", snowflake.asString());
    }

    public void setLocale(Locale locale) {
        data.put("preferred_locale", locale.getLanguage() + "-" + locale.getCountry());
    }

    public void setIconUrl(String url) {
        try {
            data.put("icon", DiscordConnector.getImageData(url));
        } catch (IOException e) {
            me.aj4real.connector.Logger.handle(e);
        }
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

