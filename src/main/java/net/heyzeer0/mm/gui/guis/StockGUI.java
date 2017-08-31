package net.heyzeer0.mm.gui.guis;

import net.heyzeer0.mm.Main;
import net.heyzeer0.mm.configs.Lang;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by HeyZeer0 on 17/08/2017.
 * Copyright © HeyZeer0 - 2016
 */
public class StockGUI {

    public static void openGui(Player p) {
        //p.closeInventory();

        MarketGUI gui = new MarketGUI(Lang.market_gui_stock_name);
        gui.setLeftCorner(ItemUtils.getCustomItem(Material.CHEST, 1, "§eAnuncios do servidor", Arrays.asList("§7Clique aqui para ver", "§7os anuncios do servidor.")), e -> {GlobalGUI.openGui(p); ((Player)e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ORB_PICKUP, 4f, 4f);});
        gui.setMainButtom(ItemUtils.getCustomItem(Material.GRASS, 1, "§2Mostrando seus anuncios", Arrays.asList("§7Clique aqui para fechar", "§7esta janela.", "§f", "§7Seu dinheiro: §a" + Main.eco.getBalance(p))), e -> {e.getWhoClicked().closeInventory();});

        UserProfile pr = Main.getData().db.getUserProfile(p);

        ArrayList<String> bank = (ArrayList<String>) pr.getBank_statement().clone();
        Collections.reverse(bank);

        List<String> lore = new ArrayList<>();
        lore.add("§7Mostrando as 10 ultimas movimentações");
        lore.add("§7Clique para mais informações");
        lore.add("§f");

        if(bank.size() >= 1) {
            for(int i = 0; i < 10; i++) {
                if(bank.size() <= i) {
                    break;
                }
                lore.add(bank.get(i));
            }
        }else{
            lore.add("§cSem informações recentes");
        }


        gui.setRightCorner(ItemUtils.getCustomItem(Material.PAPER, 1, "§2Extrato bancario", lore), e -> {
            ArrayList<String> bank2 = (ArrayList<String>) pr.getBank_statement().clone();
            if(bank2.size() <= 0) {
                p.sendMessage("§cVocê não teve movimentações bancarias");
                return;
            }
            p.sendMessage(" ");
            p.sendMessage("§8===========================");
            p.sendMessage("§7Ultimas 30 movimentações bancarias");
            p.sendMessage("§f");
            for(String x : bank2) {
                p.sendMessage(x);
            }
            p.sendMessage("§f---------------");
            p.sendMessage("§7Saldo Atual: §a" + Main.eco.getBalance(p));
            p.sendMessage("§8===========================");
            p.sendMessage(" ");
        });

        if(pr.getAnnounceList().size() >= 1) {
            for(AnnounceProfile ap : pr.getUserAnnounces()) {
                MarketAnnounce i = ap.getAnnounce();
                ItemStack item;
                if(i.isOnmarket()) {
                    item = ItemUtils.getCustomItem(i.getStack().getItemStack(), Arrays.asList(
                            "§7Estoque: §e" + i.getStock(),
                            "§7Preço: §e" + i.getPrice(),
                            "§7Quantidade: §e" + i.getAmount(),
                            "§f",
                            "§a<arraste items para ca afim de depositar>",
                            "§c<clique direito para desativar>"));

                    item.setAmount(i.getAmount());
                }else{
                    item = ItemUtils.getCustomItem(i.getStack().getItemStack(), Arrays.asList(
                            "§7Estoque: §e" + i.getStock(),
                            "§f",
                            "§a<clique esquerdo para ativar>",
                            "§a<arraste items para ca afim de depositar>",
                            "§c<clique direito para retirar items>"));

                    item.setAmount(1);
                }


                gui.addItem(item, e -> {
                    placeAnnounce(gui, p, e, i, ap);
                });
            }
        }

        for(int slot = 0; slot < ( (p.hasPermission("magimarket.user.premium") ? MainConfig.max_premium_stock : MainConfig.max_user_stock) - pr.getAnnounceList().size()); slot++) {
            gui.addItem(ItemUtils.getCustomItem(Material.STAINED_GLASS_PANE, 1, "§aSlot livre", Arrays.asList("§7Arraste um item até aqui", "§7para adiciona-lo ao seu estoque", "§8Id: " + slot), (short)7), e -> {
                removeAnnounce(gui, p, e);
            });
        }

        MarketManager.openGui(p, gui);
    }

    private static void placeAnnounce(MarketGUI g, Player p, InventoryClickEvent e, MarketAnnounce i, AnnounceProfile ap) {
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
                if(lore.size() >= 3) {
                    if(lore.get(2).equalsIgnoreCase("§a<clique esquerdo para ativar>")) {
                        if(MainConfig.tax_per_annouce != 0) {
                            if(!Main.eco.has(p, MainConfig.tax_per_annouce)) {
                                lore.set(2,  "§c<você não possui dinheiro suficiente>");
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
                if(lore.size() >= 6) {
                    if(lore.get(5).equalsIgnoreCase("§c<clique direito para desativar>")) {
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

                        MarketAnnounce a = ap.getAnnounce();
                        a.setOnMarket(false);

                        Main.getData().db().getAnnounce(ap.getId()).updateChanges(a);

                        ItemStack item = ItemUtils.getCustomItem(i.getStack().getItemStack(), Arrays.asList(
                                "§7Estoque: §e" + i.getStock(),
                                "§f",
                                "§a<clique esquerdo para ativar>",
                                "§a<arraste items para ca afim de depositar>",
                                "§c<clique direito para retirar items>"));

                        item.setAmount(1);

                        e.getInventory().setItem(e.getSlot(), item);
                        return;
                    }
                }
                if(e.getCursor().getType() == Material.AIR && lore.size() == 5 && !i.isOnmarket()) {
                    i.removeStock(e);
                    if(i.getStock() <= 0) {
                        e.getInventory().setItem(e.getSlot(), ItemUtils.getCustomItem(Material.STAINED_GLASS_PANE, 1, "§aSlot livre", Arrays.asList("§7Arraste um item até aqui", "§7para adiciona-lo ao seu estoque", "§8Id: " + e.getSlot()), (short)7));
                        g.replaceClick(e.getSlot(), ItemUtils.getCustomItem(Material.STAINED_GLASS_PANE, 1, "§aSlot livre", Arrays.asList("§7Arraste um item até aqui", "§7para adiciona-lo ao seu estoque", "§8Id: " + e.getSlot()), (short)7), ev -> {
                            removeAnnounce(g, p, ev);
                        });
                        Main.getData().db().getUserProfile(p).removeMarketAnnounce(ap.getId());
                        Main.getData().db().getAnnounce(ap.getId()).deleteAsync();
                        p.playSound(p.getLocation(), Sound.VILLAGER_NO, 4f, 4f);
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

    private static void removeAnnounce(MarketGUI g, Player p, InventoryClickEvent e) {
        if(e.getClick() == ClickType.LEFT) {
            if(e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasLore()) {
                List<String> lore = e.getCurrentItem().getItemMeta().getLore();
                if(lore.get(0).equalsIgnoreCase("§7Arraste um item até aqui")) {
                    if(e.getCursor().getType() != Material.AIR) {
                        ItemStack click = e.getCursor();

                        if(Main.materials.contains(click.getType())) {
                            return;
                        }

                        p.playSound(p.getLocation(), Sound.VILLAGER_YES, 4f, 4f);

                        MarketAnnounce i = new MarketAnnounce(click.getAmount(), new WrappedStack(click), 0, p.getUniqueId(), false, false, click.getAmount(), System.currentTimeMillis(), false);
                        AnnounceProfile pf = new AnnounceProfile(i);
                        pf.saveAsync();

                        Main.getData().db().getUserProfile(p).addMarketAnnounce(pf.getId());

                        ItemStack item = ItemUtils.getCustomItem(i.getStack().getItemStack(), Arrays.asList(
                                "§7Estoque: §e" + i.getStock(),
                                "§f",
                                "§a<clique esquerdo para ativar>",
                                "§a<arraste items para ca afim de depositar>",
                                "§c<clique direito para retirar items>"));

                        item.setAmount(1);

                        e.getInventory().setItem(e.getSlot(), item);

                        g.replaceClick(e.getSlot(), item, ev -> {
                            placeAnnounce(g, p, ev, i, pf);
                        });

                        e.setCursor(null);
                    }
                }
            }
        }
    }

}
