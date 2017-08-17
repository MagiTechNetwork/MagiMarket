package net.heyzeer0.mm.profiles;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import net.heyzeer0.mm.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.beans.ConstructorProperties;
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

    boolean active = false;
    Integer stock = 0;

    long creation = System.currentTimeMillis();

    @ConstructorProperties({"amount", "stack", "price", "owner", "server", "sell", "stock", "creation", "active"})
    public MarketAnnounce(Integer amount, WrappedStack stack, Integer price, UUID owner, boolean server, boolean sell, Integer stock, long creation, boolean active) {
        this.amount = amount;
        this.stack = stack;
        this.price = price;
        this.owner = owner;
        this.server = server;
        this.sell = sell;
        this.creation = creation;
        this.active = active;
    }

    @JsonIgnore
    public boolean addStock(InventoryClickEvent e) {
        WrappedStack add = new WrappedStack(e.getCursor());
        if(add.base64.equals(stack.base64)) {
            stock += add.getAmount();
            e.setCursor(null);
        }
        return false;
    }

    @JsonIgnore
    public boolean buyItem(Player p) {
        if(sell) {
            Bukkit.broadcastMessage("sell");
            return false;
        }
        if(!Main.eco.has(p, price)) {
            Bukkit.broadcastMessage("no money");
            return false;
        }
        if(p.getInventory().firstEmpty() == -1) {
            Bukkit.broadcastMessage("no space");
            return false;
        }
        if(!server && stock < amount) {
            Bukkit.broadcastMessage("no stock " + server);
            return false;
        }
        if(!server) {
            Bukkit.broadcastMessage("não e server");
            stock-=amount;
        }
        p.getInventory().addItem(stack.getItemStack());
        Main.eco.withdrawPlayer(p, price);
        if(stock < amount) {
            active = false;
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

        Main.eco.depositPlayer(p, money);

        if(!server) {
            stock+=amount;
        }

        return money;
    }

}
