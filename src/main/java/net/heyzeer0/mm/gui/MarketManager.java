package net.heyzeer0.mm.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by HeyZeer0 on 17/08/2017.
 * Copyright © HeyZeer0 - 2016
 */
public class MarketManager implements Listener {

    public static HashMap<UUID, MarketGUI> guis = new HashMap<>();

    public static void openGui(Player p, MarketGUI g) {
        if(guis.containsKey(p.getUniqueId())) {
            guis.replace(p.getUniqueId(), g);
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
            guis.remove(e.getPlayer().getUniqueId());
        }
    }

}
