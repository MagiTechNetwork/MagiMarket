package net.heyzeer0.mm.database.entities;

import lombok.Getter;
import net.heyzeer0.mm.Main;
import net.heyzeer0.mm.database.interfaces.ManagedObject;
import net.heyzeer0.mm.profiles.MarketAnnounce;

import java.beans.ConstructorProperties;
import java.util.UUID;

import static com.rethinkdb.RethinkDB.r;

/**
 * Created by HeyZeer0 on 17/08/2017.
 * Copyright © HeyZeer0 - 2016
 */
@Getter
public class AnnounceProfile implements ManagedObject {

    public static final String DB_TABLE = "announces";

    String id;
    MarketAnnounce announce;

    public AnnounceProfile(MarketAnnounce announce) {
        this.id = UUID.randomUUID().toString();
        this.announce = announce;
    }

    @ConstructorProperties({"id", "announce"})
    public AnnounceProfile(String id, MarketAnnounce announce) {
        this.id = id;
        this.announce = announce;
    }

    public void updateChanges(MarketAnnounce ci) {
        announce = ci;
        saveAsync();
    }

    @Override
    public void delete() {
        r.table(DB_TABLE).get(getId()).delete().runNoReply(Main.getData().conn);
    }

    @Override
    public void save() {
        r.table(DB_TABLE).insert(this).optArg("conflict", "replace").runNoReply(Main.getData().conn);
    }

}
