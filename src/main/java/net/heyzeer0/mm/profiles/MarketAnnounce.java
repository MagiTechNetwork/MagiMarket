package net.heyzeer0.mm.profiles;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import net.heyzeer0.mm.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.beans.ConstructorProperties;
import java.util.Date;
import java.util.UUID;

/**
 * Created by HeyZeer0 on 15/08/2017.
 * Copyright © HeyZeer0 - 2016
 */

@Getter
public class MarketAnnounce {

    boolean sell;
    Integer amount;
    WrappedStack stack;
    Integer price;
    boolean server;
    UUID owner;

    boolean onmarket;
    Integer stock = 0;

    long creation = System.currentTimeMillis();

    @ConstructorProperties({"amount", "stack", "price", "owner", "server", "sell", "stock", "creation", "onmarket"})
    public MarketAnnounce(Integer amount, WrappedStack stack, Integer price, UUID owner, boolean server, boolean sell, Integer stock, long creation, boolean onmarket) {
        this.amount = amount;
        this.stack = stack;
        this.price = price;
        this.owner = owner;
        this.server = server;
        this.sell = sell;
        this.creation = creation;
        this.onmarket = onmarket;
        this.stock = stock;
    }

    @JsonIgnore
    public void setOnMarket(boolean value) {
        onmarket = value;
    }

    @JsonIgnore
    public void setAmountAndPriceAndMarket(Integer amount, Integer price, boolean market) {
        this.amount = amount;
        this.price = price;
        this.onmarket = market;
    }

    @JsonIgnore
    public boolean addStock(InventoryClickEvent e) {
        if(e.getCursor().isSimilar(stack.getItemStack())) {
            stock += e.getCursor().getAmount();
            e.setCursor(null);
            ((Player)e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.BURP, 4f, 4f);
        }
        return false;
    }

    @JsonIgnore
    public void removeStock(InventoryClickEvent e) {
        if(e.getCursor().getType() == Material.AIR) {
            Integer totake = stock > stack.getItemStack().getMaxStackSize() ? stack.getItemStack().getMaxStackSize() : stock;
            ItemStack x = stack.getItemStack().clone();
            x.setAmount(totake);
            e.setCursor(x);
            stock-=totake;
        }
    }

    @JsonIgnore
    public boolean buyItem(Player p) {
        if(sell) {
            return false;
        }
        if(!Main.eco.has(p, price)) {
            return false;
        }
        if(p.getInventory().firstEmpty() == -1) {
            return false;
        }
        if(!server && stock < amount) {
            return false;
        }
        if(!server) {
            stock-=amount;
            Main.eco.depositPlayer(Bukkit.getOfflinePlayer(getOwner()), price);
            Main.getData().db().getUserProfile(getOwner()).addBankStatement("§2§lD §7- §e" + p.getName() + " §7-§a +" + price + " §7|§e " + Main.dateFormat.format(new Date()));
        }
        ItemStack item = stack.getItemStack().clone();
        item.setAmount(amount);
        p.getInventory().addItem(item);
        Main.eco.withdrawPlayer(p, price);
        Main.getData().db().getUserProfile(p).addBankStatement("§4§lS §7- §e" + (server ? "SERVER" : Bukkit.getOfflinePlayer(getOwner()).getName()) + " §7-§c -" + price + " §7|§e " + Main.dateFormat.format(new Date()));
        if(stock < amount) {
            onmarket = false;
        }
        return true;
    }

    @JsonIgnore
    public double sellItem(Player p) {
        if(!sell) {
            return 0;
        }
        if(!server) {
            return 0;
        }

        double peritem = price / amount;
        double money = 0;

        for(ItemStack i : p.getInventory().getContents()) {
            if(i == null) {
                continue;
            }
            if(i.isSimilar(stack.getItemStack())) {
                p.getInventory().remove(i);
                money += i.getAmount() * peritem;
            }
        }

        Main.getData().db().getUserProfile(p).addBankStatement("§2§lD §7- §e" + (server ? "SERVER" : Bukkit.getOfflinePlayer(getOwner()).getName()) + " §7-§a +" + price + " §7|§e " + Main.dateFormat.format(new Date()));
        Main.eco.depositPlayer(p, money);

        if(!server) {
            stock+=amount;
        }

        return money;
    }

}
