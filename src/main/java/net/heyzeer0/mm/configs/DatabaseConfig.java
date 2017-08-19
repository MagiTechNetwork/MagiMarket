package net.heyzeer0.mm.configs;

import net.heyzeer0.mm.interfaces.annotation.YamlConfig;

/**
 * Created by HeyZeer0 on 15/08/2017.
 * Copyright © HeyZeer0 - 2016
 */
@YamlConfig(name = "database")
public class DatabaseConfig {

    public static String db_adress = "localhost";
    public static int db_port = 28015;
    public static String db_name = "magimarket";
    public static String db_user = "admin";
    public static String db_user_pass = "admin";

}
