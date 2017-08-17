package net.heyzeer0.mm.database.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import net.heyzeer0.mm.Main;
import net.heyzeer0.mm.database.interfaces.ManagedObject;
import net.heyzeer0.mm.profiles.MarketAnnounce;
import org.bukkit.entity.Player;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.UUID;
import static com.rethinkdb.RethinkDB.r;

/**
 * Created by HeyZeer0 on 17/08/2017.
 * Copyright © HeyZeer0 - 2016
 */
@Getter
public class UserProfile implements ManagedObject {

    public static final String DB_TABLE = "users";

    UUID UUID;
    ArrayList<MarketAnnounce> announceList = new ArrayList<>();

    public UserProfile(Player p) {
        UUID = p.getUniqueId();
    }

    @ConstructorProperties({"UUID", "announceList"})
    public UserProfile(UUID uuid, ArrayList<MarketAnnounce> announceList) {
        this.UUID = uuid;
        this.announceList = announceList;
    }

    @JsonIgnore
    public boolean addMarketAnnounce(MarketAnnounce ann) {
        if(announceList.contains(ann)) {
            return false;
        }
        announceList.add(ann);
        saveAsync();
        return true;
    }

    @JsonIgnore
    public boolean removeMarketAnnounce(MarketAnnounce ann) {
        if(!announceList.contains(ann)) {
            return false;
        }
        announceList.remove(ann);
        saveAsync();
        return true;
    }

    @Override
    public void delete() {
        r.table(DB_TABLE).get(getUUID()).delete().runNoReply(Main.getData().conn);
    }

    @Override
    public void save() {
        r.table(DB_TABLE).insert(this).optArg("conflict", "replace").runNoReply(Main.getData().conn);
    }

}