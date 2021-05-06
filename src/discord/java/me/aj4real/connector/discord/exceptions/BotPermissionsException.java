package me.aj4real.connector.discord.exceptions;

import me.aj4real.connector.discord.objects.Bot;
import me.aj4real.connector.discord.objects.Role;

public class BotPermissionsException extends RuntimeException {
    Bot bot;
    Role.Permission[] perms;
    public BotPermissionsException(Bot bot, Role.Permission[] perms, String strPerms) {
        super("Bot " + (bot == null ? "null" : bot.getName()) + " requires the following permissions. " + strPerms);
        this.bot = bot;
        this.perms = perms;
    }
    public Bot getBot() {
        return this.bot;
    }
    public Role.Permission[] getPermissions() {
        return this.perms;
    }
}
