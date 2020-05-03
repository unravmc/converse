package net.novelmc.commands.loader;

import net.novelmc.util.ConverseBase;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;


public class CommandLoader extends ConverseBase {
    private final List<Commander> commandList;

    private CommandLoader() {
        commandList = new ArrayList<>();
    }

    public void scan() {
        CommandMap map = getCommandMap();
        if (map == null) {
            Bukkit.getLogger().severe("Error loading command map!");
            return;
        }
        commandList.clear();
        commandList.addAll(getCommands());

        commandList.stream().forEach(command -> {
           Dynamic dynamic = new Dynamic(command);
           Command existing = map.getCommand(dynamic.getName());
           if (existing != null) {
               unregisterCommand(existing, map);
           }
           map.register(plugin.getDescription().getName(), dynamic);
        });
        Bukkit.getLogger().info("Successfully loaded all commands!");
    }

    public void unregisterCommand(String commandName) {
        CommandMap map = getCommandMap();
        if (map != null) {
            Command command = map.getCommand(commandName.toLowerCase());
            if (command != null) {
                unregisterCommand(command, map);
            }
        }
    }

    public void unregisterCommand(Command command, CommandMap commandMap) {
        try {
            command.unregister(commandMap);
            HashMap<String, Command> knownCommands= getKnownCommands(commandMap);
            if (knownCommands != null) {
                knownCommands.remove(command.getName());
                command.getAliases().forEach(knownCommands::remove);
            }
        } catch (Exception ex) {
            Bukkit.getLogger().severe(ex.getLocalizedMessage());
        }
    }

    public CommandMap getCommandMap() {
        Object commandMap = plugin.reflect.getField(Bukkit.getServer().getPluginManager(), "commandMap");
        if (commandMap != null) {
            if (commandMap instanceof CommandMap) {
                return (CommandMap) commandMap;
            }
        }
        return null;
    }

    private Collection<? extends Commander> getCommands() {
        List<Commander> commanderList = new ArrayList<>();
        plugin.reflect.getAnnotatedClasses(CommandParameters.class).forEach(clazz -> {
            CommandParameters cp = clazz.getAnnotation(CommandParameters.class);
            if (cp != null) {
                Commander commander = new Commander(
                        clazz,
                        clazz.getSimpleName(),
                        cp.description(),
                        cp.usage(),
                        cp.aliases());
                commanderList.add(commander);
            }
        });
        return Collections.unmodifiableCollection(commanderList);
    }

    @SuppressWarnings("unchecked")
    public HashMap<String, Command> getKnownCommands(CommandMap commandMap) {
        Object knownCommands = plugin.reflect.getField(commandMap, "knownCommands");
        if (knownCommands != null) {
            if (knownCommands instanceof HashMap) {
                return (HashMap<String, Command>) knownCommands;
            }
        }
        return null;
    }



    public static class Commander {
        private final String commandName;
        private final Class<?> commandClass;
        private final String description;
        private final String usage;
        private final List<String> aliases;

        public Commander(Class<?> clazz, String name, String description, String usage, String aliases) {
            this.commandName = name;
            this.commandClass = clazz;
            this.description = description;
            this.usage = usage;
            this.aliases = ("".equals(aliases) ? new ArrayList<>() : Arrays.asList(aliases.split(", ")));
        }

        public List<String> getAliases() {
            return Collections.unmodifiableList(aliases);
        }

        public Class<?> getCommandClass() {
            return commandClass;
        }

        public String getCommandName() {
            return commandName;
        }

        public String getDescription() {
            return description;
        }

        public String getUsage() {
            return usage;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("commandName: ").append(commandName);
            sb.append("\ncommandClass: ").append(commandClass.getName());
            sb.append("\ndescription: ").append(description);
            sb.append("\nusage: ").append(usage);
            sb.append("\naliases: ").append(aliases);
            return sb.toString();
        }
    }

    public static class Dynamic extends Command implements PluginIdentifiableCommand {
        private final Commander commander;

        private Dynamic(Commander commander) {
            super(commander.getCommandName(), commander.getDescription(), commander.getUsage(), commander.getAliases());

            this.commander = commander;
        }

        @Override
        public boolean execute(@NotNull CommandSender sender, @NotNull String lbl, @NotNull String[] args) {
            boolean success;

            if (!getPlugin().isEnabled()) {
                return false;
            }

            try {
                success = getPlugin().onCommand(sender, this, lbl, args);
            } catch (Throwable ex) {
                throw new CommandException("Unhandled exception executing command '" + lbl + "'in plugin " + getPlugin().getDescription().getFullName(), ex);
            }

            if (!success && getUsage().length() > 0) {
                for (String string : getUsage().replace("<command>", lbl).split(" \n")) {
                    sender.sendMessage(string);
                }
            }
            return success;
        }

        @NotNull
        @Override
        public Plugin getPlugin() {
            return plugin;
        }

        public Commander getCommander() {
            return commander;
        }
    }

    public static CommandLoader getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        private static final CommandLoader INSTANCE = new CommandLoader();
    }
}
