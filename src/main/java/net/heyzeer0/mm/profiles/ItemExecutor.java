package net.heyzeer0.mm.profiles;

import lombok.Getter;
import net.heyzeer0.mm.gui.MarketGUI;
import org.bukkit.inventory.ItemStack;


/**
 * Created by HeyZeer0 on 17/08/2017.
 * Copyright © HeyZeer0 - 2016
 */
@Getter
public class ItemExecutor {

    ItemStack i;
    MarketGUI.ClickEvent ex;

    public ItemExecutor(ItemStack i, MarketGUI.ClickEvent ex) {
        this.i = i;
        this.ex = ex;
    }

}
