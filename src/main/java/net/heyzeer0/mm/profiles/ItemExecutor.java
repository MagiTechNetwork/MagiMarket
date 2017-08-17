package net.heyzeer0.mm.profiles;

import lombok.Getter;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.Callable;

/**
 * Created by HeyZeer0 on 17/08/2017.
 * Copyright © HeyZeer0 - 2016
 */
@Getter
public class ItemExecutor {

    ItemStack i;
    Callable<InventoryClickEvent> ex;

    public ItemExecutor(ItemStack i, Callable<InventoryClickEvent> ex) {
        this.i = i;
        this.ex = ex;
    }

}
