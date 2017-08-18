package net.heyzeer0.mm.gui.guis;

import net.heyzeer0.mm.Main;
import net.heyzeer0.mm.utils.ItemUtils;
import net.heyzeer0.mm.database.entities.AnnounceProfile;
import net.heyzeer0.mm.database.entities.MarketProfile;
import net.heyzeer0.mm.gui.MarketGUI;
import net.heyzeer0.mm.gui.MarketManager;
import net.heyzeer0.mm.profiles.MarketAnnounce;
import org.bukkit.Material;
import org.bukkit.Sound;
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
public class GlobalGUI {

    public static void openGui(Player p) {
        p.closeInventory();

        MarketGUI gui = new MarketGUI("MagiMarket - Anuncios Globais");
        gui.setLeftCorner(ItemUtils.getCustomItem(Material.ENDER_CHEST, 1, "§eSeu Estoque", Arrays.asList("§7Clique aqui para ver", "§7seu estoque.")), e -> {StockGUI.openGui(p); ((Player)e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ORB_PICKUP, 4f, 4f);});
        gui.setMainButtom(ItemUtils.getCustomItem(Material.GRASS, 1, "§2Anuncios Globais", Arrays.asList("§7Clique aqui para ver", "§7os anuncios do servidor.", "§f", "§7Seu dinheiro: §a" + Main.eco.getBalance(p))), e -> {ServerGUI.openGui((Player)e.getWhoClicked()); ((Player)e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ORB_PICKUP, 4f, 4f);});

        int id = 0;
        MarketProfile pr = Main.getData().db.getServerMarket("global");
        if(pr.getAnnounceList().size() >= 1) {
            for(AnnounceProfile ap : pr.getMarketAnnounces()) {
                MarketAnnounce i = ap.getAnnounce();
                ItemStack item = ItemUtils.getCustomItem(i.getStack().getItemStack(), Arrays.asList(
                        "§7Preço: §e" + i.getPrice(),
                        "§7Quantidade: §e" + i.getAmount(),
                        "§8Id: " + id,
                        "§f",
                        !i.getOwner().equals(p.getUniqueId()) ? "§a<clique esquerdo para comprar>" : "§e<você é o dono deste anuncio>",
                        p.hasPermission("magimarket.owner") || i.getOwner().equals(p.getUniqueId()) ? "§c<clique esquerdo para desativar>" : ""));

                gui.addItem(item, e -> {
                    if(e.getClick() == ClickType.RIGHT) {
                        if(e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasLore()) {
                            List<String> lore = e.getCurrentItem().getItemMeta().getLore();
                            if(lore.size() >= 6) {
                                if(lore.get(5).equalsIgnoreCase("§c<clique esquerdo para desativar>")) {
                                    lore.set(5,  "§e<clique direito novamente para desativar>");
                                    ItemStack x = e.getCurrentItem();
                                    ItemMeta y = x.getItemMeta();
                                    y.setLore(lore);
                                    x.setItemMeta(y);
                                    e.getInventory().setItem(e.getSlot(), x);
                                    return;
                                }
                                if(lore.get(5).equalsIgnoreCase("§e<clique direito novamente para desativar>")) {
                                    ((Player)e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.VILLAGER_DEATH, 4f, 4f);
                                    Main.getData().db().getServerMarket("global").removeMarketAnnounce(ap.getId());
                                    gui.replaceClick(e.getSlot(), null, ev -> {});
                                    MarketAnnounce a = ap.getAnnounce();
                                    a.setOnMarket(false);

                                    Main.getData().db().getAnnounce(ap.getId()).updateChanges(a);

                                    return;
                                }
                            }
                        }
                    }
                    if(e.getClick() == ClickType.LEFT) {
                        if(e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasLore()) {
                            List<String> lore = e.getCurrentItem().getItemMeta().getLore();
                            if(lore.get(4).equalsIgnoreCase("§a<clique esquerdo para comprar>")) {
                                lore.set(4,  "§a<clique novamente para comprar>");
                                ItemStack x = e.getCurrentItem();
                                ItemMeta y = x.getItemMeta();
                                y.setLore(lore);
                                x.setItemMeta(y);
                                e.getInventory().setItem(e.getSlot(), x);
                                return;
                            }
                            if(lore.get(4).equalsIgnoreCase("§a<clique novamente para comprar>")) {
                                if(!Main.eco.has(p, i.getPrice())) {
                                    lore.set(4,  "§c<moedas insuficientes>");
                                    ItemStack x = e.getCurrentItem();
                                    ItemMeta y = x.getItemMeta();
                                    y.setLore(lore);
                                    x.setItemMeta(y);
                                    e.getInventory().setItem(e.getSlot(), x);
                                    ((Player)e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.IRONGOLEM_HIT, 4f, 4f);
                                    return;
                                }
                                if(p.getInventory().firstEmpty() == -1) {
                                    lore.set(4,  "§c<inventário cheio>");
                                    ItemStack x = e.getCurrentItem();
                                    ItemMeta y = x.getItemMeta();
                                    y.setLore(lore);
                                    x.setItemMeta(y);
                                    e.getInventory().setItem(e.getSlot(), x);
                                    ((Player)e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.IRONGOLEM_HIT, 4f, 4f);
                                    return;
                                }

                                if(!i.buyItem(p)) {
                                    lore.set(4,  "§c<ocorreu um erro>");
                                    ItemStack x = e.getCurrentItem();
                                    ItemMeta y = x.getItemMeta();
                                    y.setLore(lore);
                                    x.setItemMeta(y);
                                    e.getInventory().setItem(e.getSlot(), x);
                                    ((Player)e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.IRONGOLEM_HIT, 4f, 4f);
                                    return;
                                }
                                p.sendMessage("§aO item comprado foi colocado em seu inventário.");

                                if(!i.isOnmarket()) {
                                    e.getInventory().setItem(e.getSlot(), null);
                                    Main.getData().db().getServerMarket("global").removeMarketAnnounce(ap.getId());
                                    ap.updateChanges(i);
                                }else{
                                    lore.set(4,  "§a<clique esquerdo para comprar>");
                                    ItemStack x = e.getCurrentItem();
                                    ItemMeta y = x.getItemMeta();
                                    y.setLore(lore);
                                    x.setItemMeta(y);
                                    e.getInventory().setItem(e.getSlot(), x);
                                }

                                Main.getData().db().getAnnounce(ap.getId()).updateChanges(i);

                                ((Player)e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.VILLAGER_YES, 4f, 4f);
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
