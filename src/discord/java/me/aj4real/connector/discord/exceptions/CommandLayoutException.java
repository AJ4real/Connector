package me.aj4real.connector.discord.exceptions;

public class CommandLayoutException extends CommandRegistrationException {
//    private final CommandLayout layout;  //TODO
    public CommandLayoutException(String message) {
        super(message);
//        this.layout = layout; //TODO
    }
}
