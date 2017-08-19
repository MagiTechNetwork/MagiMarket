package net.heyzeer0.mm;

import net.heyzeer0.mm.commands.CommandManager;
import net.heyzeer0.mm.configs.DatabaseConfig;
import net.heyzeer0.mm.configs.Lang;
import net.heyzeer0.mm.configs.MainConfig;
import net.heyzeer0.mm.database.MarketData;
import net.heyzeer0.mm.gui.MarketManager;
import net.heyzeer0.mm.managers.ConfigManager;
import net.heyzeer0.mm.utils.SignUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by HeyZeer0 on 14/08/2017.
 * Copyright © HeyZeer0 - 2016
 */
public class Main extends JavaPlugin {

    public static Main main;
    public static Economy eco;
    private static MarketData data;
    public static SignUtils sign;

    public static ArrayList<Material> materials = new ArrayList<>();

    public void onEnable() {
        main = this;

        //Protocol Lib
        if(!getServer().getPluginManager().isPluginEnabled("ProtocolLib")) {
            getLogger().log(Level.SEVERE, "MagiMarket needs ProtocolLib to run.");
            onDisable();
        }

        sign = new SignUtils(this);

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

            //Blacklist
            if(!MainConfig.blacklist.contains(",")) {
                try{
                    materials.add(Material.valueOf(MainConfig.blacklist));
                }catch (Exception ex) {
                    ex.printStackTrace();
                    getLogger().log(Level.SEVERE, "An error ocurred while trying to add material " + MainConfig.blacklist + " at blacklist");
                }
            }else{
                for(String s : MainConfig.blacklist.split(",")) {
                    try{
                        materials.add(Material.valueOf(s));
                    }catch (Exception ex) {
                        ex.printStackTrace();
                        getLogger().log(Level.SEVERE, "An error ocurred while trying to add material " + s + " at blacklist");
                    }
                }
            }

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

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new MarketManager(), this);

        //Cmds
        CommandManager.registerCommands();
    }

    public void onDisable() {

    }

    public static MarketData getData() {
        return data;
    }

}
