package net.heyzeer0.mm.utils;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by HeyZeer0 on 20/08/2017.
 * Copyright © HeyZeer0 - 2016
 */
public class ChatUtils implements Listener {

    public static Map<UUID, Long> timeleft = new HashMap<>();
    public static Map<UUID, ChatListener> users = new HashMap<>();

    public static void waitForResponse(Player p, String default_msg, ChatListener response) {
        p.sendMessage(default_msg);
        users.put(p.getUniqueId(), response);
        timeleft.put(p.getUniqueId(), System.currentTimeMillis());
    }

    @EventHandler
    public static void onChat(AsyncPlayerChatEvent e) {
        if(users.containsKey(e.getPlayer().getUniqueId())) {
            if(System.currentTimeMillis() - timeleft.get(e.getPlayer().getUniqueId()) > 5000) {
                users.remove(e.getPlayer().getUniqueId());
                timeleft.remove(e.getPlayer().getUniqueId());
                return;
            }
            ChatListener ch = users.get(e.getPlayer().getUniqueId());
            users.remove(e.getPlayer().getUniqueId());
            timeleft.remove(e.getPlayer().getUniqueId());
            ch.onChatComplete(e.getPlayer(), e.getMessage());
            e.setCancelled(true);
        }
    }

    public interface ChatListener {
        void onChatComplete(Player player, String message);
    }

}
