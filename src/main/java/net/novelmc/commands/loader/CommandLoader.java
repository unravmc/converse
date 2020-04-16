package net.novelmc.commands.loader;

import net.novelmc.ConversePlugin;
import net.novelmc.commands.Adminchat;
import net.novelmc.commands.Converse;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Pattern;

public class CommandLoader {
    public static final Pattern COMMAND_PATTERN;
    private final List<Commander> commandList;

    static {
        COMMAND_PATTERN = Pattern.compile(CommandHandler.COMMAND_PATH.replace('.', '/')
                + "/([^$]+)\\.class");
    }

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
           map.register(ConversePlugin.plugin.getDescription().getName(), dynamic);
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
        Object commandMap = getField(Bukkit.getServer().getPluginManager(), "commandMap");
        if (commandMap != null) {
            if (commandMap instanceof CommandMap) {
                return (CommandMap) commandMap;
            }
        }
        return null;
    }

    /*
    public Collection<? extends String> getPermissions() {
        List<String> permissions = new ArrayList<>();
        try {
            CodeSource cs = ConversePlugin.class.getProtectionDomain().getCodeSource();
            if (cs != null) {
                ZipInputStream stream = new ZipInputStream(cs.getLocation().openStream());
                ZipEntry entry;
                while ((entry = stream.getNextEntry()) != null) {
                    String name = entry.getName();
                    Matcher matcher = COMMAND_PATTERM.matcher(name);
                    if (matcher.find()) {
                        String permissionName = matcher.group(1).replaceAll("Command", "").toLowerCase();
                        permissions.add(permissionName);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.unmodifiableCollection(permissions);
    }
     */

    private Collection<? extends Commander> getCommands() {
        List<Commander> commanderList = new ArrayList<>();
        getAnnotatedClasses(CommandHandler.COMMAND_PATH).forEach(clazz -> {
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

    public static String remove(String name, String removal) {
        StringBuilder sb = new StringBuilder();
        String fName = name.toLowerCase();
        String fRemoval = removal.toLowerCase();
           if (fName.contains(fRemoval)) {
               // Check for the word at the beginning or in the middle
               String temp = fRemoval + " ";
               String finals = fName.replaceAll(temp, "");
               // Now check the end for the word;
               temp = " " + fRemoval;
               finals = finals.replaceAll(temp, "");
               // Return finally the end result which should no longer
               // have any instances of whatever word you removed.
               sb.append(finals);
           } else {
               // This will prevent StringBuilder from returning an empty string.
               sb.append(fName);
           }
        return sb.toString().toLowerCase();
    }

    // This is a reflective method.
    @SuppressWarnings("unchecked")
    public HashMap<String, Command> getKnownCommands(CommandMap commandMap) {
        Object knownCommands = getField(commandMap, "knownCommands");
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
            this.aliases = ("".equals(aliases) ? new ArrayList<>() : Arrays.asList(aliases.split(",")));
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
            return ConversePlugin.plugin;
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

    private Set<Class<?>> getAnnotatedClasses(String location) {
        Reflections reflections = new Reflections(location);
        return reflections.getTypesAnnotatedWith(CommandParameters.class);
    }

    @SuppressWarnings("unchecked")
    private <T> T getField(Object from, String name) {
        Class<?> clazz = from.getClass();
        do {
            try {
                Field field = clazz.getDeclaredField(name);
                field.setAccessible(true);
                return (T) field.get(from);
            } catch (NoSuchFieldException | IllegalAccessException ignored) {}
        } while (clazz.getSuperclass() != Object.class
                && ((clazz = clazz.getSuperclass()) != null));

        return null;
    }
}
