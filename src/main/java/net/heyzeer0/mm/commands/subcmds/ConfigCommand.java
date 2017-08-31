package net.heyzeer0.mm.commands.subcmds;

import net.heyzeer0.mm.Main;
import net.heyzeer0.mm.configs.MainConfig;
import net.heyzeer0.mm.interfaces.CommandExec;
import net.heyzeer0.mm.interfaces.annotation.Command;
import net.heyzeer0.mm.configs.Lang;
import net.heyzeer0.mm.managers.ConfigManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.logging.Level;

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
            m.sendMessage(String.format(Lang.command_list_format, "Lang", Lang.command_config_lang_description));
            return;
        }
        if(args[1].equalsIgnoreCase("main")) {
            try{
                ConfigManager.lockAndLoad(MainConfig.class);
                m.sendMessage(Lang.command_config_success);
                m.sendMessage(Lang.command_config_success_warn);

                Main.materials.clear();
                if(!MainConfig.blacklist.contains(",")) {
                    try{
                        Main.materials.add(Material.valueOf(MainConfig.blacklist));
                    }catch (Exception ex) {
                        ex.printStackTrace();
                        Main.main.getLogger().log(Level.SEVERE, "An error ocurred while trying to add material " + MainConfig.blacklist + " at blacklist");
                    }
                }else{
                    for(String s : MainConfig.blacklist.split(",")) {
                        try{
                            Main.materials.add(Material.valueOf(s));
                        }catch (Exception ex) {
                            ex.printStackTrace();
                            Main.main.getLogger().log(Level.SEVERE, "An error ocurred while trying to add material " + s + " at blacklist");
                        }
                    }
                }

            }catch (Exception ex) {
                m.sendMessage(Lang.command_config_error);
            }
            return;
        }
        if(args[1].equalsIgnoreCase("lang")) {
            try{
                ConfigManager.updateLang(Lang.class);
                m.sendMessage(Lang.command_config_success);
            }catch (Exception ex) {
                m.sendMessage(Lang.command_config_error);
            }
            return;
        }
        m.sendMessage(Lang.command_config_prefix);
        m.sendMessage(String.format(Lang.command_list_format, "Main", Lang.command_config_maincfg_description));
    }

}