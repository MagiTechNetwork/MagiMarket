package net.heyzeer0.mm.database.manager;

import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;
import net.heyzeer0.mm.database.entities.AnnounceProfile;
import net.heyzeer0.mm.database.entities.MarketProfile;
import net.heyzeer0.mm.database.entities.UserProfile;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.error.Mark;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.rethinkdb.RethinkDB.r;

/**
 * Created by HeyZeer0 on 15/08/2017.
 * Copyright © HeyZeer0 - 2016
 */
public class DatabaseManager {

    private Connection conn;

    public static HashMap<UUID, UserProfile> users = new HashMap<>();
    public static HashMap<String, MarketProfile> markets = new HashMap<>();
    public static HashMap<String, AnnounceProfile> announces = new HashMap<>();

    public DatabaseManager(Connection conn) {
        this.conn = conn;

        try{
            r.tableCreate("users").run(conn);
            r.tableCreate("listings").run(conn);
            r.tableCreate("announces").run(conn);
        }catch (Exception ignored) {}

        Cursor<AnnounceProfile> ann = r.table(AnnounceProfile.DB_TABLE).run(conn, AnnounceProfile.class);
        while(ann.hasNext()) {
            AnnounceProfile a = ann.next();
            announces.put(a.getId(), a);
        }

        Cursor<MarketProfile> mark = r.table(MarketProfile.DB_TABLE).run(conn, MarketProfile.class);
        while(mark.hasNext()) {
            MarketProfile a = mark.next();
            markets.put(a.getId(), a);
        }

        Cursor<UserProfile> user = r.table(UserProfile.DB_TABLE).run(conn, UserProfile.class);
        while(user.hasNext()) {
            UserProfile a = user.next();

            users.put(UUID.fromString(a.getId()), a);
        }

    }

    public UserProfile getUserProfile(Player p) {
        UserProfile data = users.getOrDefault(p.getUniqueId(), null);
        return data != null ? data : new UserProfile(p);
    }

    public UserProfile getUserProfile(UUID p) {
        UserProfile data = users.getOrDefault(p, null);
        return data;
    }

    public MarketProfile getServerMarket(String name) {
        MarketProfile data = markets.getOrDefault(name, null);
        return data != null ? data : new MarketProfile(name);
    }

    public AnnounceProfile getAnnounce(String uuid) {
        AnnounceProfile data = announces.getOrDefault(uuid, null);
        return data;
    }

}
