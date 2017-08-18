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
public class ServerGUI {

    public static void openGui(Player p) {
        p.closeInventory();

        MarketGUI gui = new MarketGUI("MagiMarket - Anuncios Servidor");
        gui.setLeftCorner(ItemUtils.getCustomItem(Material.ENDER_CHEST, 1, "§eSeu Estoque", Arrays.asList("§7Clique aqui para ver", "§7seu estoque.")), e -> {StockGUI.openGui(p); ((Player)e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ORB_PICKUP, 4f, 4f);});
        gui.setMainButtom(ItemUtils.getCustomItem(Material.COMMAND, 1, "§2Anuncios do Servidor", Arrays.asList("§7Clique aqui para ver", "§7os anuncios globais.", "§f", "§7Seu dinheiro: §a" + Main.eco.getBalance(p))), e -> {GlobalGUI.openGui((Player)e.getWhoClicked()); ((Player)e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ORB_PICKUP, 4f, 4f);});

        int id = 0;
        MarketProfile pr = Main.getData().db.getServerMarket("server");
        if(pr.getAnnounceList().size() >= 1) {
            for(AnnounceProfile ap : pr.getMarketAnnounces()) {
                MarketAnnounce i = ap.getAnnounce();
                ItemStack item = ItemUtils.getCustomItem(i.getStack().getItemStack(), Arrays.asList(
                        "§7Preço: §e" + i.getPrice(),
                        "§7Quantidade: §e" + i.getAmount(),
                        "§8Id: " + id,
                        "§f",
                        !i.isSell() ? "§a<clique esquerdo para comprar>" : "§a<clique esquerdo para vender>",
                        p.hasPermission("magimarket.owner") ? "§4<clique direito para remover>" : ""));

                id++;

                gui.addItem(item, e -> {
                    if(e.getClick() == ClickType.RIGHT) {
                        if(e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasLore()) {
                            List<String> lore = e.getCurrentItem().getItemMeta().getLore();
                            if(lore.size() >= 6) {
                                if(lore.get(5).equalsIgnoreCase("§4<clique direito para remover>")) {
                                    lore.set(5,  "§e<clique direito novamente para remover>");
                                    ItemStack x = e.getCurrentItem();
                                    ItemMeta y = x.getItemMeta();
                                    y.setLore(lore);
                                    x.setItemMeta(y);
                                    e.getInventory().setItem(e.getSlot(), x);
                                    ((Player)e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ORB_PICKUP, 4f, 4f);
                                    return;
                                }
                                if(lore.get(5).equalsIgnoreCase("§e<clique direito novamente para remover>")) {
                                    gui.replaceClick(e.getSlot(), null, ev -> {});
                                    ((Player)e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.VILLAGER_DEATH, 4f, 4f);
                                    Main.getData().db().getServerMarket("server").removeMarketAnnounce(ap.getId());
                                    Main.getData().db().getAnnounce(ap.getId()).deleteAsync();
                                    return;
                                }
                            }
                        }
                    }
                    if(e.getClick() == ClickType.LEFT) {
                        if(e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasLore()) {
                            List<String> lore = e.getCurrentItem().getItemMeta().getLore();
                            //VENDER
                            if(lore.get(4).equalsIgnoreCase("§a<clique esquerdo para vender>") || lore.get(4).equalsIgnoreCase("§c<você não possui itens para vender>")) {
                                lore.set(4,  "§e<clique esquerdo novamente para vender>");
                                ItemStack x = e.getCurrentItem();
                                ItemMeta y = x.getItemMeta();
                                y.setLore(lore);
                                x.setItemMeta(y);
                                e.getInventory().setItem(e.getSlot(), x);
                                ((Player)e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ORB_PICKUP, 4f, 4f);
                                return;
                            }
                            if(lore.get(4).equalsIgnoreCase("§e<clique esquerdo novamente para vender>")) {
                                if(i.sellItem(p) == 0) {
                                    lore.set(4,  "§c<você não possui itens para vender>");
                                    ItemStack x = e.getCurrentItem();
                                    ItemMeta y = x.getItemMeta();
                                    y.setLore(lore);
                                    x.setItemMeta(y);
                                    e.getInventory().setItem(e.getSlot(), x);
                                    ((Player)e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.IRONGOLEM_HIT, 4f, 4f);
                                    return;
                                }
                                p.sendMessage("§aVocê vendeu todos os itens do seu inventário.");

                                lore.set(4,  "§a<clique esquerdo para vender>");
                                ItemStack x = e.getCurrentItem();
                                ItemMeta y = x.getItemMeta();
                                y.setLore(lore);
                                x.setItemMeta(y);
                                e.getInventory().setItem(e.getSlot(), x);
                                ((Player)e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.VILLAGER_YES, 4f, 4f);
                                return;
                            }
                            //COMPRAR
                            if(lore.get(4).equalsIgnoreCase("§a<clique esquerdo para comprar>")) {
                                lore.set(4,  "§e<clique esquerdo novamente para comprar>");
                                ItemStack x = e.getCurrentItem();
                                ItemMeta y = x.getItemMeta();
                                y.setLore(lore);
                                x.setItemMeta(y);
                                e.getInventory().setItem(e.getSlot(), x);
                                ((Player)e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ORB_PICKUP, 4f, 4f);
                                return;
                            }
                            if(lore.get(4).equalsIgnoreCase("§e<clique esquerdo novamente para comprar>")) {
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

                                lore.set(4,  "§a<clique esquerdo para comprar>");
                                ItemStack x = e.getCurrentItem();
                                ItemMeta y = x.getItemMeta();
                                y.setLore(lore);
                                x.setItemMeta(y);
                                e.getInventory().setItem(e.getSlot(), x);
                                ((Player)e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.VILLAGER_YES, 4f, 4f);
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