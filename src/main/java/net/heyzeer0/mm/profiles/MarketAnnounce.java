package net.heyzeer0.mm.profiles;

import lombok.Getter;
import org.bukkit.event.inventory.InventoryClickEvent;

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

    @ConstructorProperties({"amount", "stack", "price", "owner", "server", "sell"})
    public MarketAnnounce(Integer amount, WrappedStack stack, Integer price, UUID owner, boolean server, boolean sell) {
        this.amount = amount;
        this.stack = stack;
        this.price = price;
        this.owner = owner;
        this.server = server;
        this.sell = sell;
    }

    public boolean addStock(InventoryClickEvent e) {
        WrappedStack add = new WrappedStack(e.getCursor());
        if(add.base64.equals(stack.base64)) {
            stock += add.getAmount();
            e.setCursor(null);
        }
        return false;
    }

}
