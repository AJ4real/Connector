package me.aj4real.connector.discord.events.interactions;

import me.aj4real.connector.Logger;
import me.aj4real.connector.Task;
import me.aj4real.connector.discord.DiscordConnector;
import me.aj4real.connector.discord.DiscordEndpoints;
import me.aj4real.connector.discord.commands.CommandLayout;
import me.aj4real.connector.discord.events.DiscordEvent;
import me.aj4real.connector.discord.objects.Channel;
import me.aj4real.connector.discord.objects.Guild;
import me.aj4real.connector.discord.objects.Role;
import me.aj4real.connector.discord.objects.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InteractionCreateEvent extends DiscordEvent {

    private final String name, interactionToken;
    private List<String> args;
    private List<Parameter> parameters;
    private final long id, guildId, channelId, cmdId;
    private final User user;

    public InteractionCreateEvent(DiscordConnector c, JSONObject data) {
        super(c, data);
        this.args = new ArrayList<>();
        this.parameters = new ArrayList<>();
        System.out.println(data);
        JSONObject d = (JSONObject) data.get("d");
        this.user = new User(c, (JSONObject) d.get("member"));
        this.id = Long.valueOf((String) d.get("id"));
        this.guildId = Long.valueOf((String) d.get("guild_id"));
        this.channelId = Long.valueOf((String) d.get("channel_id"));
        this.interactionToken = (String) d.get("token");
        JSONObject cmd = (JSONObject) d.get("data");
        this.name = (String) cmd.get("name");
        this.cmdId = Long.valueOf((String) d.get("id"));
        args.add((String) cmd.get("name"));
        if(cmd.get("options") != null) {
            JSONArray arr = (JSONArray) cmd.get("options");
            for (Object o : arr) {
                parse((JSONObject) o);
            }
        }
    }

    public String getCommandName() {
        return this.name;
    }

    public String getInteractionToken() {
        return this.interactionToken;
    }

    public long getId() {
        return this.id;
    }

    public long getGuildId() {
        return this.guildId;
    }

    public long getChannelId() {
        return this.channelId;
    }

    public long getCommandId() {
        return this.cmdId;
    }

    public Task<Guild> getGuild() {
        return Task.of(() -> {
            try {
                JSONObject o = (JSONObject) c.readJson(DiscordEndpoints.GUILDS.fulfil("guild_id", String.valueOf(this.guildId))).getData();
                return new Guild((DiscordConnector) c, o);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    public Task<Channel> getChannel() {
        return Task.of(() -> {
            try {
                JSONObject o = (JSONObject) c.readJson(DiscordEndpoints.CHANNEL.fulfil("channel_id", String.valueOf(this.channelId))).getData();
                return Channel.valueOf((DiscordConnector) c, o);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    public User getUser() {
        return this.user;
    }

    public Parameter[] getParameters() {
        //make params immutable
        Parameter[] r = new Parameter[parameters.size()];
        for (int i = 0; i < parameters.size(); i++) {
            r[i] = parameters.get(i);
        }
        return r;
    }

    public String[] getArguments() {
        //make args immutable
        String[] r = new String[args.size()];
        for (int i = 0; i < args.size(); i++) {
            r[i] = args.get(i);
        }
        return r;
    }

    private void parse(JSONObject o) {
        long type = ((Long) o.get("type")).intValue();
        if(type == 1 || type == 2) {
            args.add((String) o.get("name"));
            JSONArray options = (JSONArray) o.get("options");
            if(options != null) {
                for (Object option : options) {
                    parse((JSONObject) option);
                }
            }
        } else {
            String value = String.valueOf(o.get("value"));
            switch (CommandLayout.Parameter.Type.of(((Long)type).intValue())) {
                case BOOLEAN:
                    Parameter b = new Parameter((String) o.get("name"), (Boolean) o.get("value"));
                    parameters.add(b);
                    break;
                case INTEGER:
                    Parameter i = new Parameter((String) o.get("name"), ((Long) o.get("value")).intValue());
                    parameters.add(i);
                    break;
                case STRING:
                    Parameter s = new Parameter((String) o.get("name"), (String) o.get("value"));
                    parameters.add(s);
                    break;
                case ROLE:
                    try {
                        //discord has no endpoint to get specific roles
                        JSONArray a = (JSONArray) c.readJson(DiscordEndpoints.GUILD_ROLES.fulfil("guild_id", String.valueOf(guildId))).getData();
                        for (Object o1 : a) {
                            JSONObject o2 = (JSONObject) o1;
                            if(((String)o2.get("id")).equalsIgnoreCase(value)) {
                                Role role = new Role((DiscordConnector) c, o2);
                                Parameter r = new Parameter((String) o.get("name"), role);
                                parameters.add(r);
                            }
                        }
                    } catch (IOException e) {
                        Logger.handle(e);
                    }
                    break;
                case USER:
                    try {
                        User user = new User((DiscordConnector) c, (JSONObject) c.readJson(DiscordEndpoints.USER.fulfil("user_id", value)).getData());
                        Parameter r = new Parameter((String) o.get("name"), user);
                        parameters.add(r);
                    } catch (IOException e) {
                        Logger.handle(e);
                    }
                    break;
                case CHANNEL:
                    try {
                        Channel chan = Channel.valueOf((DiscordConnector) c, (JSONObject) c.readJson(DiscordEndpoints.CHANNEL.fulfil("channel_id", value)).getData());
                        Parameter r = new Parameter((String) o.get("name"), chan);
                        parameters.add(r);
                    } catch (IOException e) {
                        Logger.handle(e);
                    }
                    break;
            }
        }
    }
    public static class Parameter<T> {
        private String name;
        private T value;
        public Parameter(String name, T value) {
            this.name = name;
            this.value = value;
        }
        public String getName() {
            return this.name;
        }
        public T getValue() {
            return value;
        }
        public Class getType() {
            return value.getClass();
        }
    }

}
