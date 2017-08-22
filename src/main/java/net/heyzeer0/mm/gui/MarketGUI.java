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

/**
 * Created by HeyZeer0 on 17/08/2017.
 * Copyright © HeyZeer0 - 2016
 */
public class MarketGUI {

    String title;

    ArrayList<ItemExecutor> itemStacks = new ArrayList<>();
    ItemExecutor left_corner;
    ItemExecutor main_buttom;
    ItemExecutor right_corner;

    Integer page = 1;
    Inventory inv;

    public MarketGUI(String title) {
        this.title = title;
    }

    public void setLeftCorner(ItemStack i, ClickEvent ix) {
        left_corner = new ItemExecutor(i, ix);
    }

    public void setMainButtom(ItemStack i, ClickEvent ix) {
        main_buttom = new ItemExecutor(i, ix);
    }

    public void setRightCorner(ItemStack i, ClickEvent ix) {
        right_corner = new ItemExecutor(i, ix);
    }

    public void addItem(ItemStack i, ClickEvent ix) {
        itemStacks.add(new ItemExecutor(i, ix));
    }

    public void cleanItems() {
        itemStacks.clear();
    }

    public void sendGui(Player p) {
        boolean created = false;
        if(inv == null) {
            created = true;
            inv = Bukkit.createInventory(null, 54, title);
        }else{
            inv.clear();
        }
        //max 35 p/pag
        int slot = 0;
        Integer max = page * 36;
        for(int i = ((page - 1) * 36); i < max; i++) {
            if(itemStacks.size() <= i) {
                break;
            }
            inv.setItem(slot, itemStacks.get(i).getI());
            slot++;
        }

        if(itemStacks.size() > (page * 36)) {
            ItemStack x = new ItemStack(Material.ARROW, 1);
            ItemMeta i = x.getItemMeta();
            i.setDisplayName("§aPróxima Página");
            x.setItemMeta(i);
            inv.setItem(50, x);
        }
        if(page != 1) {
            ItemStack x = new ItemStack(Material.ARROW, 1);
            ItemMeta i = x.getItemMeta();
            i.setDisplayName("§cPágina Anterior");
            x.setItemMeta(i);
            inv.setItem(48, x);
        }
        inv.setItem(49, main_buttom.getI());
        inv.setItem(45, left_corner.getI());
        if(right_corner != null)
            inv.setItem(53, right_corner.getI());

        if(created) {
            p.openInventory(inv);
        }
    }

    protected void handleClick(InventoryClickEvent e) {
        if(e.getCurrentItem() == null) {
            return;
        }
        if(!e.getClickedInventory().getTitle().equalsIgnoreCase(e.getView().getTopInventory().getTitle())) {
            return;
        }
        e.setCancelled(true);
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
        if(e.getSlot() == 49) {
            main_buttom.getEx().userClicked(e);
            return;
        }
        if(e.getSlot() == 45) {
            left_corner.getEx().userClicked(e);
            return;
        }
        if(e.getSlot() == 53 && right_corner != null) {
            right_corner.getEx().userClicked(e);
            return;
        }

        try{
            int px = ((page - 1) * 36) + e.getSlot();
            if(itemStacks.size() >= px) {
                itemStacks.get(px).getEx().userClicked(e);
            }
        }catch (Exception ex) {ex.printStackTrace();}
    }

    public void replaceClick(int slot, ItemStack x, ClickEvent i) {
        int px = ((page - 1) * 36) + slot;
        if(itemStacks.size() >= px) {
            itemStacks.set(px, new ItemExecutor(x, i));
            inv.setItem(slot, x);
        }
    }

    public interface ClickEvent {

        void userClicked(InventoryClickEvent e);

    }

}
