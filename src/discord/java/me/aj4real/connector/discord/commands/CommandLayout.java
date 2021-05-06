package me.aj4real.connector.discord.commands;

import me.aj4real.connector.discord.exceptions.CommandLayoutException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommandLayout {
    protected List<Subcommand> subcommands = new ArrayList<>();
    protected List<SubcommandCategory> subcommandCategories = new ArrayList<>();
    protected List<Parameter> parameters = new ArrayList<Parameter>();
    public CommandLayout() {
    }
    public void addSubcommandCategory(SubcommandCategory category) { this.subcommandCategories.add(category); }
    public void addParameter(Parameter parameter) {
        this.parameters.add(parameter);
    }
    public void addSubcommand(Subcommand subcommand) {
        this.subcommands.add(subcommand);
    }
    public JSONObject getJSON(String name, String description) throws CommandLayoutException {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("description", description);
        JSONArray arr = json.containsKey("options") ? (JSONArray) json.get("options") : new JSONArray();
        if(subcommands.size() != 0) {
            for(Subcommand subcommand : subcommands) {
                arr.add(subcommand.getJSON());
            }
            json.put("options", arr);
        }
        if(subcommandCategories.size() != 0) {
            for(SubcommandCategory commandChoice : subcommandCategories) {
                arr.add(commandChoice.getJSON());
            }
            json.put("options", arr);
        }
        if(parameters.size() != 0) {
            for(Parameter commandParameter : parameters) {
                arr.add(commandParameter.getJSON());
            }
            json.put("options", arr);
        }
        if(subcommandCategories.size() != 0 && subcommands.size() != 0) {
            throw new CommandLayoutException("Discord Limitation: A command cannot have subcommand groups and subcommands together. This is a discord limitation.");
        }
        return json;
    }

    public static class Parameter<T> {
        private String name, description;
        private List<ParameterChoice> commandChoices = new ArrayList<>();
        private Optional<Boolean> required = Optional.empty();
        private Type type;
        public Parameter(String name, String description, Type type) {
            this.name = name;
            this.description = description;
            this.type = type;
        }
        public Parameter(String name, String description, Type type, boolean required) {
            this.name = name;
            this.description = description;
            this.type = type;
            this.required = Optional.of(required);
        }
        public void addCommandChoice(ParameterChoice commandParameter) {
            this.commandChoices.add(commandParameter);
        }
        public JSONObject getJSON() throws CommandLayoutException {
            JSONObject json = new JSONObject();
            json.put("name", name);
            json.put("description", description);
            json.put("type", type.getTypeId());
            if(required.isPresent()) json.put("required", required.get());
            if(commandChoices.size() != 0) {
                JSONArray arr = new JSONArray();
                for(ParameterChoice commandParameter : commandChoices) {
                    arr.add(commandParameter.getJSON());
                }
                json.put("choices", arr);
            }
            if(commandChoices.size() > 10) {
                throw new CommandLayoutException("Discord Limitation: https://discord.com/developers/docs/interactions/slash-commands#a-quick-note-on-limits");
            }
            return json;
        }
        public enum Type {
            STRING(3),
            INTEGER(4),
            BOOLEAN(5),
            USER(6),
            CHANNEL(7),
            ROLE(8);
            private int i;
            Type(int i) {
                this.i = i;
            }
            public int getTypeId() {
                return this.i;
            }
            public static Type of(int i) {
                for (Type v : Type.values()) {
                    if(v.getTypeId() == i) {
                        return v;
                    }
                }
                return null;
            }
        }
    }

    public static class ParameterChoice<T> {
        String name;
        T value;
        public ParameterChoice(String name, T value) {
            this.name = name;
            this.value = value;
        }
        public JSONObject getJSON() {
            JSONObject json = new JSONObject();
            json.put("name", name);
            json.put("value", value);
            return json;
        }
    }

    public static class SubcommandCategory {
        private String name, description;
        protected List<Subcommand> subcommands = new ArrayList<Subcommand>();
        protected List<SubcommandCategory> subcommandCategories = new ArrayList<>();
        public SubcommandCategory(String name, String description) {
            this.name = name;
            this.description = description;
        }
        public JSONObject getJSON() throws CommandLayoutException {
            JSONObject json = new JSONObject();
            json.put("name", name);
            json.put("description", description);
            json.put("type", 2);
            if(subcommands.size() != 0) {
                JSONArray arr = new JSONArray();
                for(Subcommand subcommand : subcommands) {
                    arr.add(subcommand.getJSON());
                }
                json.put("options", arr);
            }
            if(subcommandCategories.size() != 0) {
                JSONArray arr = new JSONArray();
                for(SubcommandCategory commandChoice : subcommandCategories) {
                    arr.add(commandChoice.getJSON());
                }
                json.put("options", arr);
            }
            if(subcommandCategories.size() != 0 && subcommands.size() != 0) {
                throw new CommandLayoutException("Discord Limitation: A subcommand group cannot have subcommand groups and subcommands together.");
            }
            if(subcommandCategories.size() > 10 || subcommands.size() > 10) {
                throw new CommandLayoutException("Discord Limitation: https://discord.com/developers/docs/interactions/slash-commands#a-quick-note-on-limits");
            }
            return json;
        }
        public void addSubcommandCategory(SubcommandCategory category) { this.subcommandCategories.add(category); }
        public void addSubcommand(Subcommand subcommand) {
            subcommands.add(subcommand);
        }
    }
    public static class Subcommand {
        private String name, description;
        protected List<Parameter> parameters = new ArrayList<Parameter>();
        public Subcommand(String name, String description) {
            this.name = name;
            this.description = description;
        }
        public JSONObject getJSON() throws CommandLayoutException {
            JSONObject json = new JSONObject();
            json.put("name", name);
            json.put("description", description);
            json.put("type", 1);
            if(parameters.size() != 0) {
                JSONArray arr = new JSONArray();
                for(Parameter commandParameter : parameters) {
                    arr.add(commandParameter.getJSON());
                }
                json.put("options", arr);
            }
            return json;
        }
        public void addParameter(Parameter parameter) {
            this.parameters.add(parameter);
        }
    }

}
