package net.heyzeer0.mm.configs;

import net.heyzeer0.mm.interfaces.annotation.YamlConfig;

/**
 * Created by HeyZeer0 on 14/08/2017.
 * Copyright © HeyZeer0 - 2016
 */
@YamlConfig(name = "main")
public class MainConfig {

    public static String main_command_prefix = "market";
    public static String main_lang = "pt_BR";
    public static Integer max_user_stock = 15;

}
