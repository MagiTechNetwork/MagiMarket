package net.heyzeer0.mm.database;

import com.rethinkdb.net.Connection;
import net.heyzeer0.mm.configs.DatabaseConfig;
import net.heyzeer0.mm.database.manager.DatabaseManager;

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
    public static ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
    public DatabaseManager db;

    public MarketData() {
        conn = r.connection().hostname(DatabaseConfig.db_adress).port(DatabaseConfig.db_port).db(DatabaseConfig.db_name).connect();
        db = new DatabaseManager(conn);
    }

    public DatabaseManager db() {
        return db;
    }

    public static ScheduledExecutorService getExecutor() {
        return exec;
    }

    public static void queue(Callable<?> action) {
        getExecutor().submit(action);
    }

    public static void queue(Runnable runnable) {
        getExecutor().submit(runnable);
    }

}
