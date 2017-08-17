package net.heyzeer0.mm.commands;

import net.heyzeer0.mm.Utils;
import net.heyzeer0.mm.commands.subcmds.ConfigCommand;
import net.heyzeer0.mm.commands.subcmds.CreateCommand;
import net.heyzeer0.mm.commands.subcmds.CreateServerCommand;
import net.heyzeer0.mm.commands.subcmds.HelpCommand;
import net.heyzeer0.mm.configs.Lang;
import net.heyzeer0.mm.configs.MainConfig;
import net.heyzeer0.mm.gui.MarketGUI;
import net.heyzeer0.mm.gui.MarketManager;
import net.heyzeer0.mm.gui.guis.GlobalGUI;
import net.heyzeer0.mm.interfaces.CommandExec;
import net.heyzeer0.mm.interfaces.annotation.Command;
import net.heyzeer0.mm.profiles.CommandProfile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by HeyZeer0 on 14/08/2017.
 * Copyright © HeyZeer0 - 2016
 */
public class CommandManager {

    public static HashMap<String, CommandProfile> cmds = new HashMap<>();
    private static String last_command;

    public static void registerCommand(CommandExec x) {
        Command ann = x.getClass().getAnnotation(Command.class);
        if(ann != null) {
            cmds.put(ann.name(), new CommandProfile(x, ann));
        }
    }

    public static boolean handleCommand(Player sender, String args[]) {
        if(cmds.containsKey(args[0])) {
            CommandProfile prf = cmds.get(args[0]);
            if(!sender.hasPermission(prf.getAnnotation().permission())) {
                sender.sendMessage(Lang.command_no_permission);
                return true;
            }
            prf.getExecutor().runCommand(sender, args);
            return true;
        }
        return false;
    }

    public static void registerCommands() {
        BukkitCommand cmd = new BukkitCommand(MainConfig.main_command_prefix) {
            @Override
            public boolean execute(CommandSender commandSender, String s, String[] strings) {
                if(commandSender instanceof Player) {
                    if(strings.length <= 0) {
                        GlobalGUI.openGui((Player)commandSender);
                        return true;
                    }
                    if(!handleCommand((Player)commandSender, strings)) {
                        handleCommand((Player)commandSender, new String[] {"help"});
                    }
                }
                return false;
            }
        };

        try {
            Field comMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            comMap.setAccessible(true);
            CommandMap map = (CommandMap) comMap.get(Bukkit.getServer());
            map.register(MainConfig.main_command_prefix, cmd);

            last_command = MainConfig.main_command_prefix;
        }
        catch(Exception ignored) {}

        registerCommand(new HelpCommand());
        registerCommand(new ConfigCommand());
        registerCommand(new CreateCommand());
        registerCommand(new CreateServerCommand());
    }



}
