package net.heyzeer0.mm.gui.guis;

import net.heyzeer0.mm.Main;
import net.heyzeer0.mm.profiles.AnnounceCreationProfile;
import net.heyzeer0.mm.utils.ItemUtils;
import net.heyzeer0.mm.configs.MainConfig;
import net.heyzeer0.mm.database.entities.AnnounceProfile;
import net.heyzeer0.mm.database.entities.UserProfile;
import net.heyzeer0.mm.gui.MarketGUI;
import net.heyzeer0.mm.gui.MarketManager;
import net.heyzeer0.mm.profiles.MarketAnnounce;
import net.heyzeer0.mm.profiles.WrappedStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

/**
 * Created by HeyZeer0 on 17/08/2017.
 * Copyright © HeyZeer0 - 2016
 */
public class StockGUI {

    public static void openGui(Player p) {
        //p.closeInventory();

        MarketGUI gui = new MarketGUI("MagiMarket - Seu Estoque");
        gui.setLeftCorner(ItemUtils.getCustomItem(Material.CHEST, 1, "§eAnuncios do servidor", Arrays.asList("§7Clique aqui para ver", "§7os anuncios do servidor.")), e -> {GlobalGUI.openGui(p); ((Player)e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ORB_PICKUP, 4f, 4f);});
        gui.setMainButtom(ItemUtils.getCustomItem(Material.GRASS, 1, "§2Mostrando seus anuncios", Arrays.asList("§7Clique aqui para fechar", "§7esta janela.", "§f", "§7Seu dinheiro: §a" + Main.eco.getBalance(p))), e -> {e.getWhoClicked().closeInventory();});

        int id = 0;
        UserProfile pr = Main.getData().db.getUserProfile(p);
        if(pr.getAnnounceList().size() >= 1) {
            for(AnnounceProfile ap : pr.getUserAnnounces()) {
                MarketAnnounce i = ap.getAnnounce();
                ItemStack item;
                if(i.isOnmarket()) {
                    item = ItemUtils.getCustomItem(i.getStack().getItemStack(), Arrays.asList(
                            "§7Estoque: §e" + i.getStock(),
                            "§7Preço: §e" + i.getPrice(),
                            "§7Quantidade: §e" + i.getAmount(),
                            "§8Id: " + id,
                            "§f",
                            "§a<arraste items para ca afim de depositar>",
                            "§c<clique direito para desativar>"));

                    item.setAmount(i.getAmount());
                }else{
                    item = ItemUtils.getCustomItem(i.getStack().getItemStack(), Arrays.asList(
                            "§7Estoque: §e" + i.getStock(),
                            "§8Id: " + id,
                            "§f",
                            "§a<clique esquerdo para ativar>",
                            "§a<arraste items para ca afim de depositar>",
                            "§c<clique direito para retirar items>"));
                }


                gui.addItem(item, e -> {
                    placeAnnounce(gui, id, p, e, i, ap);
                });
            }
        }

        for(int slot = 0; slot < ( (p.hasPermission("magimarket.user.premium") ? MainConfig.max_premium_stock : MainConfig.max_user_stock) - pr.getAnnounceList().size()); slot++) {
            gui.addItem(ItemUtils.getCustomItem(Material.STAINED_GLASS_PANE, 1, "§aSlot livre", Arrays.asList("§7Arraste um item até aqui", "§7para adiciona-lo ao seu estoque", "§8Id: " + slot), (short)7), e -> {
                removeAnnounce(gui, id, p, e);
            });
        }

        MarketManager.openGui(p, gui);
    }

    private static void placeAnnounce(MarketGUI g, int id, Player p, InventoryClickEvent e, MarketAnnounce i, AnnounceProfile ap) {
        if(e.getClick() == ClickType.LEFT) {
            if(e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasLore()) {
                List<String> lore = e.getCurrentItem().getItemMeta().getLore();
                if(e.getCursor() != null && e.getCursor().isSimilar(i.getStack().getItemStack())) {
                    i.addStock(e);
                    lore.set(0,  "§7Estoque: §e" + i.getStock());
                    ItemStack x = e.getCurrentItem();
                    ItemMeta y = x.getItemMeta();
                    y.setLore(lore);
                    x.setItemMeta(y);
                    e.getInventory().setItem(e.getSlot(), x);
                    Main.getData().db().getAnnounce(ap.getId()).updateChanges(i);
                    return;
                }
                if(lore.size() >= 4) {
                    if(lore.get(3).equalsIgnoreCase("§a<clique esquerdo para ativar>")) {
                        if(MainConfig.tax_per_annouce != 0) {
                            if(!Main.eco.has(p, MainConfig.tax_per_annouce)) {
                                lore.set(3,  "§c<você não possui dinheiro suficiente>");
                                ItemStack x = e.getCurrentItem();
                                ItemMeta y = x.getItemMeta();
                                y.setLore(lore);
                                x.setItemMeta(y);
                                e.getInventory().setItem(e.getSlot(), x);
                                return;
                            }
                        }
                        new AnnounceCreationProfile(p, ap).start();
                    }
                    return;
                }
            }

            return;
        }
        if(e.getClick() == ClickType.RIGHT) {
            if(e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasLore()) {
                List<String> lore = e.getCurrentItem().getItemMeta().getLore();
                if(lore.size() >= 7) {
                    if(lore.get(6).equalsIgnoreCase("§c<clique direito para desativar>")) {
                        lore.set(6,  "§e<clique direito novamente para desativar>");
                        ItemStack x = e.getCurrentItem();
                        ItemMeta y = x.getItemMeta();
                        y.setLore(lore);
                        x.setItemMeta(y);
                        e.getInventory().setItem(e.getSlot(), x);
                        return;
                    }
                    if(lore.get(6).equalsIgnoreCase("§e<clique direito novamente para desativar>")) {
                        ((Player)e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.VILLAGER_DEATH, 4f, 4f);
                        Main.getData().db().getServerMarket("global").removeMarketAnnounce(ap.getId());

                        MarketAnnounce a = ap.getAnnounce();
                        a.setOnMarket(false);

                        Main.getData().db().getAnnounce(ap.getId()).updateChanges(a);

                        ItemStack item = ItemUtils.getCustomItem(i.getStack().getItemStack(), Arrays.asList(
                                "§7Estoque: §e" + i.getStock(),
                                "§8Id: " + id,
                                "§f",
                                "§a<clique esquerdo para ativar>",
                                "§a<arraste items para ca afim de depositar>",
                                "§c<clique direito para retirar items>"));

                        e.getInventory().setItem(e.getSlot(), item);
                        return;
                    }
                }
                if(e.getCursor().getType() == Material.AIR && lore.size() == 6 && !i.isOnmarket()) {
                    i.removeStock(e);
                    if(i.getStock() <= 0) {
                        e.getInventory().setItem(e.getSlot(), ItemUtils.getCustomItem(Material.STAINED_GLASS_PANE, 1, "§aSlot livre", Arrays.asList("§7Arraste um item até aqui", "§7para adiciona-lo ao seu estoque", "§8Id: " + e.getSlot()), (short)7));
                        g.replaceClick(e.getSlot(), ItemUtils.getCustomItem(Material.STAINED_GLASS_PANE, 1, "§aSlot livre", Arrays.asList("§7Arraste um item até aqui", "§7para adiciona-lo ao seu estoque", "§8Id: " + e.getSlot()), (short)7), ev -> {
                            removeAnnounce(g, ev.getSlot(), p, ev);
                        });
                        Main.getData().db().getUserProfile(p).removeMarketAnnounce(ap.getId());
                        Main.getData().db().getAnnounce(ap.getId()).deleteAsync();
                        return;
                    }

                    lore.set(0,  "§7Estoque: §e" + i.getStock());
                    ItemStack x = e.getCurrentItem();
                    ItemMeta y = x.getItemMeta();
                    y.setLore(lore);
                    x.setItemMeta(y);
                    e.getInventory().setItem(e.getSlot(), x);
                    Main.getData().db().getAnnounce(ap.getId()).updateChanges(i);
                    return;
                }
            }
        }
    }

    private static void removeAnnounce(MarketGUI g, int id, Player p, InventoryClickEvent e) {
        if(e.getClick() == ClickType.LEFT) {
            if(e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasLore()) {
                List<String> lore = e.getCurrentItem().getItemMeta().getLore();
                if(lore.get(0).equalsIgnoreCase("§7Arraste um item até aqui")) {
                    if(e.getCursor().getType() != Material.AIR) {
                        ItemStack click = e.getCursor();

                        if(Main.materials.contains(click.getType())) {
                            return;
                        }

                        MarketAnnounce i = new MarketAnnounce(click.getAmount(), new WrappedStack(click), 0, p.getUniqueId(), false, false, click.getAmount(), System.currentTimeMillis(), false);
                        AnnounceProfile pf = new AnnounceProfile(i);
                        pf.saveAsync();

                        Main.getData().db().getUserProfile(p).addMarketAnnounce(pf.getId());

                        ItemStack item = ItemUtils.getCustomItem(i.getStack().getItemStack(), Arrays.asList(
                                "§7Estoque: §e" + i.getStock(),
                                "§8Id: " + id,
                                "§f",
                                "§a<clique esquerdo para ativar>",
                                "§a<arraste items para ca afim de depositar>",
                                "§c<clique direito para retirar items>"));

                        item.setAmount(1);

                        e.getInventory().setItem(e.getSlot(), item);

                        g.replaceClick(e.getSlot(), item, ev -> {
                            placeAnnounce(g, id, p, ev, i, pf);
                        });

                        e.setCursor(null);
                    }
                }
            }
        }
    }

}
