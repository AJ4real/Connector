package me.aj4real.connector.discord.commands;

public class Command {
    private final String name, description;
    private CommandExecutor executor;
    private CommandLayout layout;
    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public CommandExecutor getExecutor() {
        return this.executor;
    }

    public CommandLayout getLayout() {
        return this.layout;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        //TODO
        private String name, description;
        private CommandExecutor executor;
        private CommandLayout layout;
        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setExecutor(CommandExecutor executor) {
            this.executor = executor;
            return this;
        }

        public Builder setLayout(CommandLayout layout) {
            this.layout = layout;
            return this;
        }

        public String getName() {
            return this.name;
        }
        public String getDescription() {
            return this.description;
        }
        public CommandExecutor getExecutor() {
            return this.executor;
        }
        public CommandLayout getLayout() {
            return this.layout;
        }
    }

}
