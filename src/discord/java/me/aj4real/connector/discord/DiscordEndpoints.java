package me.aj4real.connector.discord;

import me.aj4real.connector.Endpoint;

public class DiscordEndpoints {
    //TODO: https://discord.com/developers/docs/interactions/slash-commands#endpoints
    public static final String BASE = "https://discord.com/api/v8";
    public static final String CDN = "https://cdn.discordapp.com";
    public static final Endpoint BOT = new Endpoint(Endpoint.HttpMethod.GET, BASE + "/oauth2/applications/@me");
//    public static final Endpoint AUTH = new Endpoint(Endpoint.HttpMethod.GET, BASE + "/oauth2/@me");
    public static final Endpoint DIRECT_MESSAGE_CHANNEL = new Endpoint(Endpoint.HttpMethod.GET, BASE + "/users/@me/channels");
    public static final Endpoint GUILD_AUDIT_LOG = new Endpoint(Endpoint.HttpMethod.GET, BASE + "/guilds/{guild_id}/audit-logs");
    public static final Endpoint GUILDS = new Endpoint(Endpoint.HttpMethod.GET, BASE + "/guilds/{guild_id}");
    public static final Endpoint MODIFY_GUILD = new Endpoint(Endpoint.HttpMethod.PATCH, BASE + "/guilds/{guild_id}");
    public static final Endpoint CREATE_CHANNEL = new Endpoint(Endpoint.HttpMethod.POST, BASE + "/guilds/{guild_id}/channels");
    public static final Endpoint GUILD_CHANNELS = new Endpoint(Endpoint.HttpMethod.GET, BASE + "/guilds/{guild_id}/channels");
    public static final Endpoint GUILD_ROLES = new Endpoint(Endpoint.HttpMethod.GET, BASE + "/guilds/{guild_id}/roles");
    public static final Endpoint CHANNEL = new Endpoint(Endpoint.HttpMethod.GET, BASE + "/channels/{channel_id}");
    public static final Endpoint CREATE_MESSAGE = new Endpoint(Endpoint.HttpMethod.POST, BASE + "/channels/{channel_id}/messages");
    public static final Endpoint CHANNEL_WEBHOOKS = new Endpoint(Endpoint.HttpMethod.GET, BASE + "/channels/{channel_id}/webhooks");
    public static final Endpoint CREATE_CHANNEL_WEBHOOK = new Endpoint(Endpoint.HttpMethod.POST, BASE + "/channels/{channel_id}/webhooks");
    public static final Endpoint GUILD_WEBHOOKS = new Endpoint(Endpoint.HttpMethod.GET, BASE + "/guilds/{guild_id}/webhooks");
    public static final Endpoint WEBHOOK = new Endpoint(Endpoint.HttpMethod.GET, BASE + "/webhooks/{webhook_id}");
    public static final Endpoint WEBHOOK_WITH_TOKEN = new Endpoint(Endpoint.HttpMethod.GET, BASE + "/webhooks/{webhook_id}/{token}");
    public static final Endpoint INVOKE_WEBHOOK = new Endpoint(Endpoint.HttpMethod.POST, BASE + "/webhooks/{webhook_id}/{token}");
    public static final Endpoint MODIFY_WEBHOOK = new Endpoint(Endpoint.HttpMethod.PATCH, BASE + "/webhooks/{webhook_id}");
    public static final Endpoint MODIFY_WEBHOOK_WITH_TOKEN = new Endpoint(Endpoint.HttpMethod.PATCH, BASE + "/webhooks/{webhook_id}/{token}");
    public static final Endpoint DELETE_WEBHOOK = new Endpoint(Endpoint.HttpMethod.DELETE, BASE + "/webhooks/{webhook_id}");
    public static final Endpoint DELETE_WEBHOOK_WITH_TOKEN = new Endpoint(Endpoint.HttpMethod.DELETE, BASE + "/webhooks/{webhook_id}/{token}");
    public static final Endpoint MODIFY_CHANNEL = new Endpoint(Endpoint.HttpMethod.PATCH, BASE + "/channels/{channel_id}");
    public static final Endpoint USER = new Endpoint(Endpoint.HttpMethod.GET, BASE + "/users/{user_id}");
    public static final Endpoint DELETE_GUILD_COMMAND = new Endpoint(Endpoint.HttpMethod.DELETE,BASE + "/applications/{application_id}/guilds/{guild_id}/commands/{command_id}");
    public static final Endpoint GET_GUILD_COMMANDS = new Endpoint(Endpoint.HttpMethod.GET,BASE + "/applications/{application_id}/guilds/{guild_id}/commands");
    public static final Endpoint ADD_GUILD_COMMANDS = new Endpoint(Endpoint.HttpMethod.POST,BASE + "/applications/{application_id}/guilds/{guild_id}/commands");
    public static final Endpoint DELETE_APPLICATION_COMMAND = new Endpoint(Endpoint.HttpMethod.DELETE,BASE + "/applications/{application_id}/commands/{command_id}");
    public static final Endpoint GET_APPLICATION_COMMANDS = new Endpoint(Endpoint.HttpMethod.GET,BASE + "/applications/{application_id}/commands");
    public static final Endpoint ADD_APPLICATION_COMMANDS = new Endpoint(Endpoint.HttpMethod.POST,BASE + "/applications/{application_id}/commands");
    public static final Endpoint GUILD_MEMBER = new Endpoint(Endpoint.HttpMethod.GET, BASE + "/guilds/{guild_id}/members/{user_id}");
    public static final Endpoint KICK_GUILD_MEMBER = new Endpoint(Endpoint.HttpMethod.DELETE, BASE + "/guilds/{guild_id}/members/{user_id}");
    public static final Endpoint MODIFY_GUILD_MEMBER = new Endpoint(Endpoint.HttpMethod.PATCH, BASE + "/guilds/{guild_id}/members/{user_id}");
    public static final Endpoint ADD_GUILD_MEMBER_ROLE = new Endpoint(Endpoint.HttpMethod.PUT, BASE + "/guilds/{guild_id}/members/{user_id}/roles/{role_id}");
    public static final Endpoint REMOVE_GUILD_MEMBER_ROLE = new Endpoint(Endpoint.HttpMethod.DELETE, BASE + "/guilds/{guild_id}/members/{user_id}/roles/{role_id}");
    public static final Endpoint GUILD_MEMBERS = new Endpoint(Endpoint.HttpMethod.GET, BASE + "/guilds/{guild_id}/members");
    public static final Endpoint USER_AVATAR_ICON = new Endpoint(Endpoint.HttpMethod.GET, CDN + "/avatars/{user_id}/{avatar}.jpeg");
    public static final String ICON_IMAGE_PATH = CDN + "/icons/%s/%s.%s";
    public static final String SPLASH_IMAGE_PATH = CDN + "/splashes/%s/%s.%s";
    public static final String DISCOVERY_SPLASH_IMAGE_PATH = CDN + "/discovery-splashes/%s/%s.%s";
    public static final String BANNER_IMAGE_PATH = CDN + "/banners/%s/%s.%s";

    public enum CDNFormats {

        PNG("png"),
        JPEG("jpeg"),
        GIF("gif"),
        UNKNOWN("UNKNOWN");

        private final String extension;

        CDNFormats(final String extension) {
            this.extension = extension;
        }
        public String getExtension() {
            return extension;
        }
    }
}
