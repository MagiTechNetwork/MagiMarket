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

    String uuid;
    MarketAnnounce announce;

    public AnnounceProfile(MarketAnnounce announce) {
        this.uuid = UUID.randomUUID().toString();
        this.announce = announce;
    }

    @ConstructorProperties({"uuid", "announce"})
    public AnnounceProfile(String uuid, MarketAnnounce announce) {
        this.uuid = uuid;
        this.announce = announce;
        System.out.println("constructor");
    }

    public void updateChanges(MarketAnnounce ci) {
        announce = ci;
        saveAsync();
    }

    @Override
    public void delete() {
        r.table(DB_TABLE).get(getUuid()).delete().runNoReply(Main.getData().conn);
    }

    @Override
    public void save() {
        System.out.println("salvando");
        System.out.println("" + r.table(DB_TABLE).insert(this).optArg("conflict", "replace").run(Main.getData().conn));
    }

}
