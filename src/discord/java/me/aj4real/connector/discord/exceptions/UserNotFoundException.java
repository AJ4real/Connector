package me.aj4real.connector.discord.exceptions;

import me.aj4real.connector.discord.objects.Snowflake;

public class UserNotFoundException extends Exception {
    private final Snowflake id;
    public UserNotFoundException(Snowflake id) {
        super("Failed to fetch user " + id.asString());
        this.id = id;
    }
    public Snowflake getId() {
        return this.id;
    }
}
