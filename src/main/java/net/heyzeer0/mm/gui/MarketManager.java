package net.heyzeer0.mm.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;

/**
 * Created by HeyZeer0 on 17/08/2017.
 * Copyright © HeyZeer0 - 2016
 */
public class MarketManager implements Listener {

    public static ArrayList<MarketGUI> guis = new ArrayList<>();

    public static void openGui(Player p, MarketGUI g) {
        guis.add(g);
    }

    @EventHandler
    public static void click(InventoryClickEvent e) {
        guis.stream().filter(gd -> gd.getTitle().equalsIgnoreCase(e.getClickedInventory().getTitle())).filter(gd -> gd.getPlayer().getUniqueId().
        equals(e.getWhoClicked().getUniqueId())).forEach(gd -> gd.handleClick(e));
    }

}
