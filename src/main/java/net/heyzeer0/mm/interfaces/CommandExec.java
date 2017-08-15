package net.heyzeer0.mm.interfaces;

import net.heyzeer0.mm.exception.CommandMessage;
import org.bukkit.entity.Player;

/**
 * Created by HeyZeer0 on 14/08/2017.
 * Copyright © HeyZeer0 - 2016
 */
public interface CommandExec {

    void runCommand(Player m, String args[]) throws CommandMessage;

}
