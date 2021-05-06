package me.aj4real.connector.discord.commands;

import me.aj4real.connector.discord.events.interactions.InteractionCreateEvent;

public interface CommandExecutor {
    public void onCommand(InteractionCreateEvent cmd);
}
