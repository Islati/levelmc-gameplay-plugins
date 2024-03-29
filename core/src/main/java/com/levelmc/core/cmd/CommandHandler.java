package com.levelmc.core.cmd;

import com.levelmc.core.LevelCore;
import com.levelmc.core.api.ApiMessages;
import com.levelmc.core.chat.Chat;
import com.levelmc.core.cmd.commands.CommandInfo;
import com.levelmc.core.cmd.handlers.*;
import org.bukkit.*;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.joor.Reflect;

import java.lang.reflect.Method;
import java.util.*;


public class CommandHandler implements CommandExecutor {

    private JavaPlugin plugin;
    private Map<Class<?>, ArgumentHandler<?>> argumentHandlers = new HashMap<>();
    private Map<org.bukkit.command.Command, RootCommand> rootCommands = new HashMap<>();

    private HelpHandler helpHandler = new HelpHandler() {
        private String formatArgument(CommandArgument argument) {
            String def = argument.getDefault();
            if (def.equals(" ")) {
                def = "";
            } else if (def.startsWith("?")) {
                String varName = def.substring(1);
                def = argument.getHandler().getVariableUserFriendlyName(varName);
                if (def == null) {
                    throw new IllegalArgumentException("The ArgumentVariable '" + varName + "' is not registered.");
                }
                def = ChatColor.GOLD + " | " + ChatColor.WHITE + def;
            } else {
                def = ChatColor.GOLD + " | " + ChatColor.WHITE + def;
            }

            return ChatColor.AQUA + "[" + argument.getName() + def + ChatColor.AQUA + "] " + ChatColor.DARK_AQUA + argument.getDescription();
        }

        @Override
        public String[] getHelpMessage(RegisteredCommand command) {
            //todo implement option to input a HelpScreen and move it to an array of strings.
            ArrayList<String> message = new ArrayList<String>();

            if (command.isSet()) {
                message.add(ChatColor.AQUA + command.getDescription());
            }

            message.add(getUsage(command));

            if (command.isSet()) {
                for (CommandArgument argument : command.getArguments()) {
                    message.add(formatArgument(argument));
                }
                if (command.getWildcard() != null) {
                    message.add(formatArgument(command.getWildcard()));
                }
                List<Flag> flags = command.getFlags();
                if (flags.size() > 0) {
                    message.add(ChatColor.GOLD + "Flags:");
                    for (Flag flag : flags) {
                        StringBuilder args = new StringBuilder();
                        for (FlagArgument argument : flag.getArguments()) {
                            args.append(" [" + argument.getName() + "]");
                        }
                        message.add("-" + flag.getIdentifier() + ChatColor.AQUA + args.toString());
                        for (FlagArgument argument : flag.getArguments()) {
                            message.add(formatArgument(argument));
                        }
                    }
                }
            }


            List<RegisteredCommand> subCommands = command.getSuffixes();
            if (subCommands.size() > 0) {
                message.add(ChatColor.GOLD + "Subcommands:");
                for (RegisteredCommand scommand : subCommands) {
                    message.add(scommand.getUsage());
                }
            }

            return message.toArray(new String[0]);
        }

        @Override
        //todo Change this to a help screen! Also, use JSON to make hoverable / clickable
        //info.
        public String getUsage(RegisteredCommand command) {
            StringBuilder usage = new StringBuilder();
            usage.append(command.getLabel());

            RegisteredCommand parent = command.getParent();
            while (parent != null) {
                usage.insert(0, parent.getLabel() + " ");
                parent = parent.getParent();
            }

            usage.insert(0, "/");

            if (!command.isSet()) {
                return usage.toString();
            }

            usage.append(ChatColor.AQUA);

            for (CommandArgument argument : command.getArguments()) {
                usage.append(" [" + argument.getName() + "]");
            }

            usage.append(ChatColor.WHITE);

            for (Flag flag : command.getFlags()) {
                usage.append(" (-" + flag.getIdentifier() + ChatColor.AQUA);
                for (FlagArgument arg : flag.getArguments()) {
                    usage.append(" [" + arg.getName() + "]");
                }
                usage.append(ChatColor.WHITE + ")");
            }

            if (command.getWildcard() != null) {
                usage.append(ChatColor.AQUA + " [" + command.getWildcard().getName() + "]");
            }

            return usage.toString();
        }
    };

    private String helpSuffix = "help";

    public CommandHandler(JavaPlugin plugin) {
        this.plugin = plugin;

        registerArgumentHandler(String.class, new StringArgumentHandler());

        registerArgumentHandler(int.class, new IntegerArgumentHandler());

        registerArgumentHandler(double.class, new DoubleArgumentHandler());

        registerArgumentHandler(boolean.class, new BooleanArgumentHandler());

        registerArgumentHandler(Player.class, new PlayerArgumentHandler());

        registerArgumentHandler(World.class, new WorldArgumentHandler());

        registerArgumentHandler(EntityType.class, new EntityTypeArgumentHandler());

        registerArgumentHandler(Material.class, new MaterialArgumentHandler());

        //Allow creation of ItemStacks via arguments, and also has variables so you
        //can default to an itemstack using item names, ids, and id:data values!
        registerArgumentHandler(ItemStack.class, new ItemStackArgumentHandler());

        registerArgumentHandler(Sound.class, new SoundArgumentHandler());

    }

    @SuppressWarnings("unchecked")
    public <T> ArgumentHandler<? extends T> getArgumentHandler(Class<T> clazz) {
        return (ArgumentHandler<? extends T>) argumentHandlers.get(clazz);
    }

    public HelpHandler getHelpHandler() {
        return helpHandler;
    }

    public <T> void registerArgumentHandler(Class<? extends T> clazz, ArgumentHandler<T> argHandler) {
        if (argumentHandlers.get(clazz) != null) {
            throw new IllegalArgumentException("The is already a ArgumentHandler bound to the class " + clazz.getName() + ".");
        }

        argHandler.handler = this;
        argumentHandlers.put(clazz, argHandler);
    }

    public void registerCommands(Object... commands) {
        for (Object o : commands) {
            try {
                registerCommand(o);
            } catch (RegisterCommandMethodException e) {
                e.printStackTrace();
            }
        }
    }

    public void registerCommand(Object commands) {
        for (Method method : commands.getClass().getDeclaredMethods()) {
            Command commandAnno = method.getAnnotation(Command.class);
            if (commandAnno == null) {
                continue;
            }

            String[] identifiers = commandAnno.identifier().split(" ");
            if (identifiers.length == 0) {
                throw new RegisterCommandMethodException(method, "Invalid identifiers");
            }

            PluginCommand rootPcommand = plugin.getCommand(identifiers[0]);

            if (rootPcommand == null) {
                if (!commands.getClass().isAnnotationPresent(CommandInfo.class)) {
                    throw new RegisterCommandMethodException(method, "Commands in class " + commands.getClass().getSimpleName() + " are not present in plugin.yml and do not have a @CommandInfo annotation");
                } else {
                    CommandInfo info = commands.getClass().getAnnotation(CommandInfo.class);
                    PluginCommand cmd = Reflect.onClass(PluginCommand.class).create(info.name(),plugin).get();
                    rootPcommand = cmd;

                    plugin.getLogger().info("Registering commands in " + commands.getClass().getSimpleName());

                    registerCommandInfo(info, rootPcommand);
                }
            }

            if (rootPcommand.getExecutor() != this) {
                rootPcommand.setExecutor(this);
            }

            RootCommand rootCommand = rootCommands.get(rootPcommand);

            if (rootCommand == null) {
                rootCommand = new RootCommand(rootPcommand, this);
                rootCommands.put(rootPcommand, rootCommand);
            }

            RegisteredCommand mainCommand = rootCommand;

            for (int i = 1; i < identifiers.length; i++) {
                String suffix = identifiers[i];
                if (mainCommand.doesSuffixCommandExist(suffix)) {
                    mainCommand = mainCommand.getSuffixCommand(suffix);
                } else {
                    RegisteredCommand newCommand = new RegisteredCommand(suffix, this, mainCommand);
                    mainCommand.addSuffixCommand(suffix, newCommand);
                    mainCommand = newCommand;
                }
            }

            mainCommand.set(commands, method);
        }
    }

    private void registerCommandInfo(CommandInfo info, PluginCommand cmd) {

        if (info == null)
            throw new IllegalArgumentException("The command " + cmd.getClass().getName() + " cannot be registered as it has no CommandInfo annotation!");


        cmd.setAliases(Arrays.asList(info.aliases()));
        cmd.setUsage(info.usage());
        cmd.setDescription(info.description());

        try {
            Reflect.on(Bukkit.getPluginManager()).field("commandMap").call("register", info.name(), cmd);
        } catch (Exception ex) {
            Reflect.on(Bukkit.getPluginManager()).field("server").field("commandMap").call("register",info.name(),cmd);
            plugin.getLogger().info("Entering commands into test env");
        }

        Bukkit.getConsoleSender().sendMessage(Chat.colorize("&7Command &e/" + info.name() + "&7 registered."));

    }

    public void setHelpHandler(HelpHandler helpHandler) {
        this.helpHandler = helpHandler;
    }

    public String getHelpSuffix() {
        return helpSuffix;
    }

    public void setHelpSuffix(String suffix) {
        this.helpSuffix = suffix;
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        RootCommand rootCommand = rootCommands.get(command);
        if (rootCommand == null) {
            return false;
        }

        if (rootCommand.onlyPlayers() && !(sender instanceof Player)) {
            Chat.msg(sender, ApiMessages.get("player-only-command"));
            return true;
        }

        rootCommand.execute(sender, args);

        return true;
    }
}
