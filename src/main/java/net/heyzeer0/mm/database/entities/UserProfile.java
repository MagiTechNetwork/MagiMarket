package net.heyzeer0.mm.database.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import net.heyzeer0.mm.Main;
import net.heyzeer0.mm.database.interfaces.ManagedObject;
import net.heyzeer0.mm.database.manager.DatabaseManager;
import net.heyzeer0.mm.profiles.MarketAnnounce;
import org.bukkit.entity.Player;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static com.rethinkdb.RethinkDB.r;
import static net.heyzeer0.mm.Main.getData;

/**
 * Created by HeyZeer0 on 17/08/2017.
 * Copyright © HeyZeer0 - 2016
 */
@Getter
public class UserProfile implements ManagedObject {

    public static final String DB_TABLE = "users";

    String id;
    ArrayList<String> announceList = new ArrayList<>();

    public UserProfile(Player p) {
        id = p.getUniqueId().toString();
    }

    @ConstructorProperties({"id", "announceList"})
    public UserProfile(String id, ArrayList<String> announceList) {
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
    public List<AnnounceProfile> getUserAnnounces() {
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
        DatabaseManager.users.remove(UUID.fromString(getId()));
        r.table(DB_TABLE).get(id).delete().runNoReply(getData().conn);
    }

    @Override
    public void save() {
        r.table(DB_TABLE).insert(this).optArg("conflict", "replace").runNoReply(getData().conn);
        DatabaseManager.users.put(UUID.fromString(getId()), this);
    }

}
