package net.heyzeer0.mm;

import net.heyzeer0.mm.commands.CommandManager;
import net.heyzeer0.mm.configs.Lang;
import net.heyzeer0.mm.configs.MainConfig;
import net.heyzeer0.mm.managers.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by HeyZeer0 on 14/08/2017.
 * Copyright © HeyZeer0 - 2016
 */
public class Main extends JavaPlugin {

    public static Main main;

    public void onEnable() {
        main = this;
        try{
            ConfigManager.lockAndLoad(MainConfig.class);
            ConfigManager.updateLang(Lang.class);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        CommandManager.registerCommands();
    }

    public void onDisable() {

    }

}
