package net.heyzeer0.mm;

import net.heyzeer0.mm.commands.CommandManager;
import net.heyzeer0.mm.configs.DatabaseConfig;
import net.heyzeer0.mm.configs.Lang;
import net.heyzeer0.mm.configs.MainConfig;
import net.heyzeer0.mm.database.MarketData;
import net.heyzeer0.mm.managers.ConfigManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

/**
 * Created by HeyZeer0 on 14/08/2017.
 * Copyright © HeyZeer0 - 2016
 */
public class Main extends JavaPlugin {

    public static Main main;
    public static Economy eco;
    private static MarketData data;

    public void onEnable() {
        main = this;

        //Vault
        if(!getServer().getPluginManager().isPluginEnabled("Vault")) {
            getLogger().log(Level.SEVERE, "MagiMarket needs Vault to run.");
            onDisable();
            return;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            getLogger().log(Level.SEVERE, "MagiMarket needs Vault to run.");
            onDisable();
            return;
        }
        eco = rsp.getProvider();

        //Configs
        try{
            ConfigManager.lockAndLoad(MainConfig.class);
            ConfigManager.lockAndLoad(DatabaseConfig.class);
            ConfigManager.updateLang(Lang.class);
        }catch (Exception ex) {
            ex.printStackTrace();
        }

        //Database
        try{
            data = new MarketData();
        }catch (Exception ex) {
            ex.printStackTrace();
            getLogger().log(Level.SEVERE, "An error ocurred while trying to connect to the database.");
            return;
        }

        //Cmds
        CommandManager.registerCommands();
    }

    public void onDisable() {

    }

    public static MarketData getData() {
        return data;
    }

}
