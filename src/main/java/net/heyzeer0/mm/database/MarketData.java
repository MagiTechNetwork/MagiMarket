package net.heyzeer0.mm.database;

import com.rethinkdb.net.Connection;
import net.heyzeer0.mm.Main;
import net.heyzeer0.mm.configs.DatabaseConfig;
import net.heyzeer0.mm.database.entities.AnnounceProfile;
import net.heyzeer0.mm.database.manager.DatabaseManager;
import org.bukkit.Bukkit;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static com.rethinkdb.RethinkDB.r;

/**
 * Created by HeyZeer0 on 15/08/2017.
 * Copyright © HeyZeer0 - 2016
 */
public class MarketData {

    public Connection conn;
    public DatabaseManager db;

    public MarketData() {
        conn = r.connection().hostname(DatabaseConfig.db_adress).port(DatabaseConfig.db_port).db(DatabaseConfig.db_name).connect();
        db = new DatabaseManager(conn);
    }

    public DatabaseManager db() {
        return db;
    }

    public static void queue(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.main, runnable::run);
    }

}
