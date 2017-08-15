package net.heyzeer0.mm.database.manager;

import com.rethinkdb.net.Connection;
import static com.rethinkdb.RethinkDB.r;

/**
 * Created by HeyZeer0 on 15/08/2017.
 * Copyright © HeyZeer0 - 2016
 */
public class DatabaseManager {

    private Connection conn;

    public DatabaseManager(Connection conn) {
        this.conn = conn;

        try{
            r.tableCreate("users").run(conn);
            r.tableCreate("listings").run(conn);
        }catch (Exception ignored) {}
    }





}
