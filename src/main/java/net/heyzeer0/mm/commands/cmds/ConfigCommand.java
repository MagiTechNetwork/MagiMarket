package net.heyzeer0.mm.commands.cmds;

import net.heyzeer0.mm.configs.MainConfig;
import net.heyzeer0.mm.interfaces.CommandExec;
import net.heyzeer0.mm.interfaces.annotation.Command;
import net.heyzeer0.mm.configs.Lang;
import net.heyzeer0.mm.managers.ConfigManager;
import org.bukkit.entity.Player;

/**
 * Created by HeyZeer0 on 14/08/2017.
 * Copyright © HeyZeer0 - 2016
 */
@Command(name = "config", permission = "magimarket.cmd.config", description = "Permite recarregar configurações")
public class ConfigCommand implements CommandExec {

    @Override
    public void runCommand(Player m, String[] args) {
        if(args.length <= 1) {
            m.sendMessage(Lang.command_config_prefix);
            m.sendMessage(String.format(Lang.command_list_format, "Main", Lang.command_config_maincfg_description));
            return;
        }
        if(args[1].equalsIgnoreCase("main")) {
            try{
                ConfigManager.lockAndLoad(MainConfig.class);
                m.sendMessage(Lang.command_config_success);
                m.sendMessage(Lang.command_config_success_warn);
            }catch (Exception ex) {
                m.sendMessage(Lang.command_config_error);
            }
            return;
        }
        m.sendMessage(Lang.command_config_prefix);
        m.sendMessage(String.format(Lang.command_list_format, "Main", Lang.command_config_maincfg_description));
    }

}