package me.aj4real.connector.discord.commands;

import me.aj4real.connector.Logger;
import me.aj4real.connector.Response;
import me.aj4real.connector.discord.DiscordConnector;
import me.aj4real.connector.discord.DiscordEndpoints;
import me.aj4real.connector.discord.events.interactions.InteractionCreateEvent;
import me.aj4real.connector.discord.exceptions.CommandRegistrationException;
import me.aj4real.connector.discord.objects.Guild;
import me.aj4real.connector.discord.objects.Snowflake;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandRegistry {

    private Map<String, Command> commands = new HashMap<>();
    private Map<Long, List<Command>> guildCommands = new HashMap<>();
    private Map<String, CommandExecutor> commandActions = new HashMap<>();

    DiscordConnector c;
    public CommandRegistry(DiscordConnector c) {
        this.c = c;
        try {
            Response r = c.readJson(DiscordEndpoints.GET_APPLICATION_COMMANDS.fulfil("application_id", c.getAppId()));
            System.out.println(r.getData());
        } catch (Exception e) {}
    }

    private Map<String, Command> getCommands() {
        return this.commands;
    }

    private void addCommandHandler(Command command) {
        this.commands.put(command.getName(), command);
        this.commandActions.put(command.getName(), command.getExecutor());
        c.getHandler().subscribe(InteractionCreateEvent.class, (e) -> {
            commandActions.get(e.getCommandName()).onCommand(e);
        });
    }

    public void registerGuildCommand(Command.Builder command, Guild guild) throws CommandRegistrationException {
        registerGuildCommand(command, guild.getId().asLong());
    }
    public void registerGuildCommand(Command.Builder command, Snowflake guild) throws CommandRegistrationException {
        registerGuildCommand(command, guild.asLong());
    }
    public void registerGuildCommand(Command.Builder command, long guildId) throws CommandRegistrationException {
        JSONArray arr = new JSONArray();
        try {
            arr = (JSONArray) c.readJson(DiscordEndpoints.GET_GUILD_COMMANDS.fulfil("application_id", c.getAppId()).fulfil("guild_id", String.valueOf(guildId))).getData();
        } catch (Exception e) {}
//TODO?
//        commands.put(command.getName(), command);
//        if(guildCommands.containsKey(guildId)) {
//            guildCommands.get(guildId).add(command);
//        } else {
//            List<Command> cmds = new ArrayList<>();
//            cmds.add(command);
//            guildCommands.put(guildId, cmds);
//        }
        Response r = null;
        try {
            r = c.readJson(DiscordEndpoints.ADD_GUILD_COMMANDS.fulfil("application_id", c.getAppId()).fulfil("guild_id", String.valueOf(guildId)), command.getLayout().getJSON(command.getName(), command.getDescription()).toJSONString());
            System.out.println(r.getData().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(r.getResponseCode() < 300) {
            Logger.log(Logger.Level.INFO, "Guild command " + command.getName() + " added to registry.");
//            addCommandHandler(command);
        } else {
            Logger.log(Logger.Level.ERROR, "Error registering global command " + command.getName() + ". Code: " + r.getResponseCode() + System.lineSeparator() + r.getData());
//            analyzeError(command, (JSONObject) r.getData());
        }
    }

    public void registerApplicationCommand(Command command) throws CommandRegistrationException {
        commands.put(command.getName(), command);
        Response r = null;
        try {
            r = c.readJson(DiscordEndpoints.ADD_APPLICATION_COMMANDS.fulfil("application_id", c.getAppId()), command.getLayout().getJSON(command.getName(), command.getDescription()).toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(r.getResponseCode() < 300) {
            Logger.log(Logger.Level.INFO, "Global command " + command.getName() + " added to registry.");
            addCommandHandler(command);
        } else {
            Logger.log(Logger.Level.ERROR, "Error registering global command " + command.getName() + ". Code: " + r.getResponseCode() + System.lineSeparator() + r.getData());
//            analyzeError(command, (JSONObject) r.getData());
        }
    }

    public void removeExistingGlobalCommands() {
        Response r = null;
        try {
            r = c.readJson(DiscordEndpoints.GET_APPLICATION_COMMANDS.fulfil("application_id", c.getAppId()));
            JSONArray cmds = (JSONArray) r.getData();
            for (Object o : cmds) {
                try {
                    JSONObject cmd = (JSONObject) o;
                    r = c.readJson(DiscordEndpoints.DELETE_APPLICATION_COMMAND.fulfil("applcation_id", c.getAppId()).fulfil("command_id", (String) cmd.get("id")));
                    Logger.log(Logger.Level.INFO, "Cleared application command " + cmd.get("name") + " for application " + c.getAppId() + ". " + r.getResponseCode());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeExistingGuildCommands(Guild guild) {
        removeExistingGuildCommands(guild.getId().asLong());
    }
    public void removeExistingGuildCommands(Snowflake guild) {
        removeExistingGuildCommands(guild.asLong());
    }
    public void removeExistingGuildCommands(long guildId) {
        Response r = null;
        try {
            r = c.readJson(DiscordEndpoints.GET_GUILD_COMMANDS.fulfil("application_id", c.getAppId()).fulfil("guild_id", String.valueOf(guildId)));
            JSONArray cmds = (JSONArray) r.getData();
            for (Object o : cmds) {
                try {
                    JSONObject cmd = (JSONObject) o;
                    r = c.readJson(DiscordEndpoints.DELETE_GUILD_COMMAND.fulfil("application_id", c.getAppId()).fulfil("guild_id", String.valueOf(guildId)).fulfil("command_id", (String) cmd.get("id")));
                    Logger.log(Logger.Level.INFO, "Cleared guild command " + cmd.get("name") + " for guild " + guildId + ". " + r.getResponseCode());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//TODO
//    private void analyzeError(Command command, JSONObject o) {
//        JSONObject cmdJson = command.getLayout().getJSON(command.getName(), command.getDescription());
//        long primaryCode = (long) o.get("code");
//        String primaryMessage = (String) o.get("message");
//        List<Integer> pathNums = op1((JSONObject) o.get("errors"), null);
//        String pathToError = op2(cmdJson, pathNums, (String) cmdJson.get("name"));
//    }
//    public String op2(JSONObject jsonObject, List<Integer> i, String current) {
//        JSONArray array = (JSONArray) jsonObject.get("options");
//        String name = (String) ((JSONObject) array.get(i.get(0))).get("name");
//        i.remove(0);
//        try {
//            current = current + "." + name;
//            return op2((JSONObject) array.get(i.get(0)), i, current);
//        } catch (Exception e) {}
//        return current;
//    }
//    private List<Integer> op1(JSONObject jsonObject, List<Integer> l) {
//        if(l == null) {
//            l = new ArrayList<>();
//        }
//        JSONObject opt = (JSONObject) jsonObject.get("options");
//        try {
//            String i = (String) opt.keySet().toArray()[0];
//            l.add(Integer.valueOf(i));
//            l = op1((JSONObject) opt.get(i), l);
//        } catch (Exception e) {}
//        return l;
//    }

}
