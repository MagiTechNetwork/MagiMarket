package net.heyzeer0.mm.commands.subcmds;

import net.heyzeer0.mm.gui.guis.StockGUI;
import net.heyzeer0.mm.interfaces.CommandExec;
import net.heyzeer0.mm.interfaces.annotation.Command;
import org.bukkit.entity.Player;

/**
 * Created by HeyZeer0 on 18/08/2017.
 * Copyright © HeyZeer0 - 2016
 */
@Command(name = "stock", permission = "magimarket.cmd.stock", description = "Abre o seu estoque")
public class StockCommand implements CommandExec {

    @Override
    public void runCommand(Player m, String[] args) {
        StockGUI.openGui(m);
    }

}
