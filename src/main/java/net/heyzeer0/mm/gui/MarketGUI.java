package net.heyzeer0.mm.gui;

import net.heyzeer0.mm.profiles.ItemExecutor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * Created by HeyZeer0 on 17/08/2017.
 * Copyright © HeyZeer0 - 2016
 */
public class MarketGUI {

    String title;

    ArrayList<ItemExecutor> itemStacks = new ArrayList<>();
    ItemExecutor left_corner;
    ItemExecutor main_buttom;

    Integer page = 1;
    Inventory inv;
    Player p;

    public MarketGUI(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public Player getPlayer() {
        return p;
    }

    public void setLeftCorner(ItemStack i, Callable<InventoryClickEvent> ix) {
        left_corner = new ItemExecutor(i, ix);
    }

    public void setMainButtom(ItemStack i, Callable<InventoryClickEvent> ix) {
        main_buttom = new ItemExecutor(i, ix);
    }

    public void addItem(ItemStack i, Callable<InventoryClickEvent> ix) {
        itemStacks.add(new ItemExecutor(i, ix));
    }

    public void sendGui(Player p) {
        this.p = p;

        boolean created = false;
        if(inv == null) {
            created = true;
            inv = Bukkit.createInventory(null, 54, title);
        }else{
            inv.clear();
        }
        //max 35 p/pag
        Integer max = page * 36;
        for(int i = ((page - 1) * 36); i < max; i++) {
            if(itemStacks.size() < i) {
                break;
            }
            inv.addItem(itemStacks.get(i).getI());
        }

        if(itemStacks.size() > 36) {
            ItemStack x = new ItemStack(Material.ARROW, 1);
            ItemMeta i = x.getItemMeta();
            i.setDisplayName("§aPróxima Página");
            x.setItemMeta(i);
            inv.setItem(51, x);
        }
        if(page != 0) {
            ItemStack x = new ItemStack(Material.ARROW, 1);
            ItemMeta i = x.getItemMeta();
            i.setDisplayName("§cPágina Anterior");
            x.setItemMeta(i);
            inv.setItem(49, x);
        }
        inv.setItem(50, main_buttom.getI());
        inv.setItem(46, left_corner.getI());

        if(created) {
            p.openInventory(inv);
        }
    }

    public void handleClick(InventoryClickEvent e) {
        if(e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName() && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aPróxima Página")) {
            page++;
            sendGui((Player)e.getWhoClicked());
            return;
        }
        if(e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName() && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cPágina Anterior")) {
            page--;
            sendGui((Player)e.getWhoClicked());
            return;
        }
        if(e.getSlot() == 50) {
            try{
                main_buttom.getEx().call();
            }catch (Exception ex) {
                ex.printStackTrace();
            }
            return;
        }
        if(e.getSlot() == 46) {
            try{
                left_corner.getEx().call();
            }catch (Exception ex) {
                ex.printStackTrace();
            }
            return;
        }

        try{
            itemStacks.get(((page - 1) * 36) + e.getSlot()).getEx().call();
        }catch (Exception ex) {ex.printStackTrace();}
    }

}
