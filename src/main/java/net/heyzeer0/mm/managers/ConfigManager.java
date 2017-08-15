package net.heyzeer0.mm.managers;

import net.heyzeer0.mm.Main;
import net.heyzeer0.mm.configs.MainConfig;
import net.heyzeer0.mm.interfaces.annotation.LangHelper;
import net.heyzeer0.mm.interfaces.annotation.YamlConfig;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Created by HeyZeer0 on 14/08/2017.
 * Copyright © HeyZeer0 - 2016
 */
public class ConfigManager {

    public static void updateLang(Class<?> x) throws IllegalAccessException, IOException  {
        LangHelper ann = x.getAnnotation(LangHelper.class);
        if(ann != null) {
            if(!Main.main.getDataFolder().exists()) {
                Main.main.getDataFolder().mkdir();
            }

            File config_file;

            if(!new File(Main.main.getDataFolder() + File.separator + "langs").exists()) {
                new File(Main.main.getDataFolder() + File.separator + "langs").mkdir();
            }

            config_file = new File(Main.main.getDataFolder() + File.separator + "langs", MainConfig.main_lang + ".yml");

            boolean newconfig = false;

            if(!config_file.exists()) {
                newconfig = true;
                config_file.createNewFile();
            }

            YamlConfiguration config = YamlConfiguration.loadConfiguration(config_file);

            if(newconfig) {
                for(Field fd : x.getFields()) {
                    Object obj = fd.get(null);
                    if(obj.getClass() == String.class) {
                        config.set(fd.getName(), obj);
                    }
                }
                config.save(config_file);
                return;
            }

            boolean save = false;

            for(Field fd : x.getFields()) {
                if(fd.get(null).getClass() == String.class) {
                    if(!config.contains(fd.getName())) {
                        config.set(fd.getName(), fd.get(null));
                        save = true;
                    }
                    fd.set(null, config.getString(fd.getName()));
                }
            }

            if(save) {
                config.save(config_file);
            }

        }
    }

    public static void lockAndLoad(Class<?> x) throws IllegalAccessException, IOException {
        YamlConfig ann = x.getAnnotation(YamlConfig.class);
        if(ann != null) {
            if(!Main.main.getDataFolder().exists()) {
                Main.main.getDataFolder().mkdir();
            }

            File config_file;


            if(ann.folder().equalsIgnoreCase("none")) {
                config_file = new File(Main.main.getDataFolder(), ann.name() + ".yml");
            }else{

                if(!new File(Main.main.getDataFolder() + File.separator + ann.folder()).exists()) {
                    new File(Main.main.getDataFolder() + File.separator + ann.folder()).mkdir();
                }

                config_file = new File(Main.main.getDataFolder() + File.separator + ann.folder(), ann.name() + ".yml");
            }

            boolean newconfig = false;

            if(!config_file.exists()) {
                newconfig = true;
                config_file.createNewFile();
            }

            YamlConfiguration config = YamlConfiguration.loadConfiguration(config_file);

            if(newconfig) {
                for(Field fd : x.getFields()) {
                    Object obj = fd.get(null);
                    if(obj.getClass() == String.class) {
                        config.set(fd.getName(), obj);
                    }
                }
                config.save(config_file);
                return;
            }

            boolean save = false;

            for(Field fd : x.getFields()) {
                if(fd.get(null).getClass() == String.class) {
                    if(!config.contains(fd.getName())) {
                        config.set(fd.getName(), fd.get(null));
                        save = true;
                    }
                    fd.set(null, config.getString(fd.getName()));
                }
            }

            if(save) {
                config.save(config_file);
            }

        }
    }

}
