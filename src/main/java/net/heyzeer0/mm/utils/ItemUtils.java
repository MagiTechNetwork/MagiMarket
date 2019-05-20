package net.heyzeer0.mm.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * Created by HeyZeer0 on 17/08/2017.
 * Copyright © HeyZeer0 - 2016
 */
public class ItemUtils {

    public static ItemStack getCustomItem(Material m, Integer amount, String name, List<String> lore) {
        ItemStack x = new ItemStack(m, amount);
        ItemMeta y = x.getItemMeta();
        y.setDisplayName(name);
        y.setLore(lore);
        x.setItemMeta(y);

        return x;
    }

    public static ItemStack getCustomItem(Material m, Integer amount, String name) {
        ItemStack x = new ItemStack(m, amount);
        ItemMeta y = x.getItemMeta();
        y.setDisplayName(name);
        x.setItemMeta(y);

        return x;
    }

    public static ItemStack getCustomItem(ItemStack i, List<String> lore) {
        ItemStack x = i.clone();
        ItemMeta y = x.getItemMeta();
        y.setLore(lore);
        x.setItemMeta(y);

        return x;
    }

    public static ItemStack getCustomItem(ItemStack i, String name) {
        ItemStack x = i.clone();
        ItemMeta y = x.getItemMeta();
        y.setDisplayName(name);
        x.setItemMeta(y);

        return x;
    }

}
