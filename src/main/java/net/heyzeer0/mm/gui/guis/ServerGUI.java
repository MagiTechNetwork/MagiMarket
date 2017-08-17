package net.heyzeer0.mm.gui.guis;

import net.heyzeer0.mm.Main;
import net.heyzeer0.mm.Utils;
import net.heyzeer0.mm.database.entities.AnnounceProfile;
import net.heyzeer0.mm.database.entities.MarketProfile;
import net.heyzeer0.mm.gui.MarketGUI;
import net.heyzeer0.mm.gui.MarketManager;
import net.heyzeer0.mm.profiles.MarketAnnounce;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

/**
 * Created by HeyZeer0 on 17/08/2017.
 * Copyright © HeyZeer0 - 2016
 */
public class ServerGUI {

    public static void openGui(Player p) {
        p.closeInventory();

        MarketGUI gui = new MarketGUI("MagiMarket - Anuncios Servidor");
        gui.setLeftCorner(Utils.getCustomItem(Material.ENDER_CHEST, 1, "§eSeu Estoque", Arrays.asList("§7Clique aqui para ver", "§7seu estoque.")), e -> {StockGUI.openGui(p);});
        gui.setMainButtom(Utils.getCustomItem(Material.COMMAND, 1, "§2Anuncios do Servidor", Arrays.asList("§7Clique aqui para ver", "§7os anuncios do servidor.")), e -> {GlobalGUI.openGui((Player)e.getWhoClicked());});

        int id = 0;
        MarketProfile pr = Main.getData().db.getServerMarket("server");
        if(pr.getAnnounceList().size() >= 1) {
            for(AnnounceProfile ap : pr.getMarketAnnounces()) {
                MarketAnnounce i = ap.getAnnounce();
                ItemStack item = Utils.getCustomItem(i.getStack().getItemStack(), Arrays.asList(
                        "§7Preço: §e" + i.getPrice(),
                        "§7Quantidade: §e" + i.getAmount(),
                        "§8Id: " + id,
                        "§f",
                        !i.isSell() ? "§a<clique esquerdo para comprar>" : "§a<clique esquerdo para vender>",
                        p.hasPermission("magimarket.owner") ? "§c<clique esquerdo para desativar>" : ""));

                gui.addItem(item, e -> {
                    if(e.getClick() == ClickType.LEFT) {
                        if(e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasLore()) {
                            List<String> lore = e.getCurrentItem().getItemMeta().getLore();
                            if(lore.get(4).equalsIgnoreCase("§a<clique esquerdo para comprar>")) {
                                lore.set(4,  "§a§l<clique novamente para comprar>");
                                ItemStack x = e.getCurrentItem();
                                ItemMeta y = x.getItemMeta();
                                y.setLore(lore);
                                x.setItemMeta(y);
                                e.getInventory().setItem(e.getSlot(), x);
                                return;
                            }
                            if(lore.get(4).equalsIgnoreCase("§a§l<clique novamente para comprar>")) {
                                if(!Main.eco.has(p, i.getPrice())) {
                                    lore.set(4,  "§c<moedas insuficientes>");
                                    ItemStack x = e.getCurrentItem();
                                    ItemMeta y = x.getItemMeta();
                                    y.setLore(lore);
                                    x.setItemMeta(y);
                                    e.getInventory().setItem(e.getSlot(), x);
                                    return;
                                }
                                if(p.getInventory().firstEmpty() == -1) {
                                    lore.set(4,  "§c<inventário cheio>");
                                    ItemStack x = e.getCurrentItem();
                                    ItemMeta y = x.getItemMeta();
                                    y.setLore(lore);
                                    x.setItemMeta(y);
                                    e.getInventory().setItem(e.getSlot(), x);
                                    return;
                                }
                                i.buyItem(p);
                                p.sendMessage("§aO item comprado foi colocado em seu inventário.");

                                lore.set(4,  "§a<clique esquerdo para comprar>");
                                ItemStack x = e.getCurrentItem();
                                ItemMeta y = x.getItemMeta();
                                y.setLore(lore);
                                x.setItemMeta(y);
                                e.getInventory().setItem(e.getSlot(), x);
                                return;
                            }
                        }
                        return;
                    }
                });
            }
        }

        MarketManager.openGui(p, gui);
    }

}
