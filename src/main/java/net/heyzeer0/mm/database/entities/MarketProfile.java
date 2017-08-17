package net.heyzeer0.mm.database.entities;

/**
 * Created by HeyZeer0 on 17/08/2017.
 * Copyright © HeyZeer0 - 2016
 */
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import net.heyzeer0.mm.Main;
import net.heyzeer0.mm.database.interfaces.ManagedObject;
import net.heyzeer0.mm.profiles.MarketAnnounce;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.List;

import static com.rethinkdb.RethinkDB.r;

/**
 * Created by HeyZeer0 on 17/08/2017.
 * Copyright © HeyZeer0 - 2016
 */
@Getter
public class MarketProfile implements ManagedObject {

    public static final String DB_TABLE = "listings";

    String id;
    ArrayList<String> announceList = new ArrayList<>();

    public MarketProfile(String name) {
        this.id = name;
    }

    @ConstructorProperties({"id", "announceList"})
    public MarketProfile(String id, ArrayList<String> announceList) {
        this.id = id;
        this.announceList = announceList;
    }

    @JsonIgnore
    public boolean addMarketAnnounce(String ann) {
        if(announceList.contains(ann)) {
            return false;
        }
        announceList.add(ann);
        saveAsync();
        return true;
    }

    @JsonIgnore
    public boolean removeMarketAnnounce(String ann) {
        if(!announceList.contains(ann)) {
            return false;
        }
        announceList.remove(ann);
        saveAsync();
        return true;
    }

    @JsonIgnore
    public List<AnnounceProfile> getMarketAnnounces() {
        ArrayList<AnnounceProfile> announces = new ArrayList<>();
        for(String ann : announceList) {
            AnnounceProfile profile = Main.getData().db().getAnnounce(ann);
            if(profile == null) {
                removeMarketAnnounce(ann);
                continue;
            }
            announces.add(profile);
        }

        return announces;
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

