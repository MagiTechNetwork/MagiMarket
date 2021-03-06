package net.heyzeer0.mm.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by HeyZeer0 on 17/08/2017.
 * Copyright © HeyZeer0 - 2016
 */
public class MarketManager implements Listener {

    private static HashMap<UUID, MarketGUI> guis = new HashMap<>();
    private static ArrayList<UUID> change = new ArrayList<>();

    public static void openGui(Player p, MarketGUI g) {
        if(guis.containsKey(p.getUniqueId())) {
            guis.replace(p.getUniqueId(), g);
            change.add(p.getUniqueId());
            g.sendGui(p);
            return;
        }
        guis.put(p.getUniqueId(), g);
        g.sendGui(p);
    }

    @EventHandler
    public static void click(InventoryClickEvent e) {
        if(guis.containsKey(e.getWhoClicked().getUniqueId())) {
            guis.get(e.getWhoClicked().getUniqueId()).handleClick(e);
        }
    }

    @EventHandler
    public static void close(InventoryCloseEvent e) {
        if(guis.containsKey(e.getPlayer().getUniqueId())) {
            if(change.contains(e.getPlayer().getUniqueId())) {
                change.remove(e.getPlayer().getUniqueId());
                return;
            }
            guis.remove(e.getPlayer().getUniqueId());
        }
    }

}
