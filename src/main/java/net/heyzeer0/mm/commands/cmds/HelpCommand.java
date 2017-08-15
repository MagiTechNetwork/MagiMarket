package net.heyzeer0.mm.commands.cmds;

import net.heyzeer0.mm.commands.CommandManager;
import net.heyzeer0.mm.configs.MainConfig;
import net.heyzeer0.mm.exception.CommandMessage;
import net.heyzeer0.mm.interfaces.CommandExec;
import net.heyzeer0.mm.interfaces.annotation.Command;
import net.heyzeer0.mm.configs.Lang;
import net.heyzeer0.mm.profiles.CommandProfile;
import org.bukkit.entity.Player;

/**
 * Created by HeyZeer0 on 14/08/2017.
 * Copyright © HeyZeer0 - 2016
 */

@Command(name = "help", permission = "magimarket.cmd.help", description = "Lista todos os comandos.")
public class HelpCommand implements CommandExec {

    @Override
    public void runCommand(Player m, String[] args) throws CommandMessage {
        m.sendMessage(Lang.command_help_prefix);
        System.out.println(args);
        for(String pr : CommandManager.cmds.keySet()) {
            CommandProfile cmd = CommandManager.cmds.get(pr);
            m.sendMessage(String.format(Lang.command_list_format, "/" + MainConfig.main_command_prefix + " " + cmd.getAnnotation().name(), cmd.getAnnotation().description()));
        }
    }

}
