package net.heyzeer0.mm.gui.guis;

import net.heyzeer0.mm.Main;
import net.heyzeer0.mm.Utils;
import net.heyzeer0.mm.database.entities.AnnounceProfile;
import net.heyzeer0.mm.database.entities.UserProfile;
import net.heyzeer0.mm.gui.MarketGUI;
import net.heyzeer0.mm.gui.MarketManager;
import net.heyzeer0.mm.profiles.MarketAnnounce;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * Created by HeyZeer0 on 17/08/2017.
 * Copyright © HeyZeer0 - 2016
 */
public class StockGUI {

    public static void openGui(Player p) {
        p.closeInventory();

        MarketGUI gui = new MarketGUI("MagiMarket - Seu Estoque");
        gui.setLeftCorner(Utils.getCustomItem(Material.CHEST, 1, "§eAnuncios do servidor", Arrays.asList("§7Clique aqui para ver", "§7os anuncios do servidor.")), e -> {GlobalGUI.openGui(p);});
        gui.setMainButtom(Utils.getCustomItem(Material.GRASS, 1, "§2Mostrando seus anuncios", Arrays.asList("§7Clique aqui para fechar", "§7esta janela.")), e -> {e.getWhoClicked().closeInventory();});

        int id = 0;
        UserProfile pr = Main.getData().db.getUserProfile(p);
        if(pr.getAnnounceList().size() >= 1) {
            for(AnnounceProfile ap : pr.getUserAnnounces()) {
                MarketAnnounce i = ap.getAnnounce();
            }
        }

        MarketManager.openGui(p, gui);
    }

}
