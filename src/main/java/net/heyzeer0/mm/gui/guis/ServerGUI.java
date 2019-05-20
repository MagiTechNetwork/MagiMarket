package net.heyzeer0.mm.gui.guis;

import net.heyzeer0.mm.Main;
import net.heyzeer0.mm.configs.Lang;
import net.heyzeer0.mm.utils.ItemUtils;
import net.heyzeer0.mm.database.entities.AnnounceProfile;
import net.heyzeer0.mm.database.entities.MarketProfile;
import net.heyzeer0.mm.gui.MarketGUI;
import net.heyzeer0.mm.gui.MarketManager;
import net.heyzeer0.mm.profiles.MarketAnnounce;
import org.bukkit.Bukkit;
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
        //p.closeInventory();

        MarketGUI gui = new MarketGUI(Lang.market_gui_server_name);
        gui.setLeftCorner(ItemUtils.getCustomItem(Material.ENDER_CHEST, 1, "§eSeu Estoque", Arrays.asList("§7Clique aqui para ver", "§7seu estoque.")), e -> {StockGUI.openGui(p); ((Player)e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 4f, 4f);});
        gui.setMainButtom(ItemUtils.getCustomItem(Material.COMMAND_BLOCK, 1, "§2Anuncios do Servidor", Arrays.asList("§7Clique aqui para ver", "§7os anuncios globais.", "§f", "§7Seu dinheiro: §a" + Main.numberFormat.format(Main.eco.getBalance(p)))), e -> {GlobalGUI.openGui((Player)e.getWhoClicked()); ((Player)e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 4f, 4f);});


        gui.setRightCorner(ItemUtils.getCustomItem(Material.EMERALD, 1, "§dComprando Itens", Arrays.asList("§7Clique aqui para alterar para", "§7vender itens.")), e -> {
            if(e.getCurrentItem().getType() == Material.EMERALD) {
                p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 4f, 4f);
                gui.cleanItems();

                sellItems(gui, p);
                gui.sendGui(p);

                e.getInventory().setItem(53, ItemUtils.getCustomItem(Material.HOPPER, 1, "§dVendendo Itens", Arrays.asList("§7Clique aqui para alterar para", "§7comprar itens.")));

                return;
            }
            if(e.getCurrentItem().getType() == Material.HOPPER) {
                p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 4f, 4f);
                gui.cleanItems();

                buyItems(gui, p);
                gui.sendGui(p);

                e.getInventory().setItem(53, ItemUtils.getCustomItem(Material.EMERALD, 1, "§dComprando Itens", Arrays.asList("§7Clique aqui para alterar para", "§7vender itens.")));

                return;
            }
        });

        buyItems(gui, p);

        MarketManager.openGui(p, gui);
    }

    public static void sellItems(MarketGUI gui, Player p) {
        MarketProfile pr = Main.getData().db.getServerMarket("server");
        if(pr.getAnnounceList().size() >= 1) {
            for(AnnounceProfile ap : pr.getMarketAnnounces()) {
                MarketAnnounce i = ap.getAnnounce();

                if(!i.isSell()) {
                    continue;
                }

                ItemStack item = ItemUtils.getCustomItem(i.getStack().getItemStack(), Arrays.asList(
                        "§7Preço: §e" + i.getPrice(),
                        "§7Quantidade: §e" + i.getAmount(),
                        "§f",
                        !i.isSell() ? "§a<clique esquerdo para comprar>" : "§a<clique esquerdo para vender>",
                        p.hasPermission("magimarket.owner") ? "§4<clique direito para remover>" : ""));

                gui.addItem(item, e -> {
                    if(e.getClick() == ClickType.RIGHT) {
                        if(e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasLore()) {
                            List<String> lore = e.getCurrentItem().getItemMeta().getLore();
                            if(lore.size() >= 5) {
                                if(lore.get(4).equalsIgnoreCase("§4<clique direito para remover>")) {
                                    lore.set(4,  "§e<clique direito novamente para remover>");
                                    ItemStack x = e.getCurrentItem();
                                    ItemMeta y = x.getItemMeta();
                                    y.setLore(lore);
                                    x.setItemMeta(y);
                                    e.getInventory().setItem(e.getSlot(), x);
                                    ((Player)e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 4f, 4f);
                                    return;
                                }
                                if(lore.get(4).equalsIgnoreCase("§e<clique direito novamente para remover>")) {
                                    gui.replaceClick(e.getSlot(), null, ev -> {});
                                    ((Player)e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ENTITY_VILLAGER_DEATH, 4f, 4f);
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
                            if(lore.get(3).equalsIgnoreCase("§a<clique esquerdo para vender>") || lore.get(3).equalsIgnoreCase("§c<você não possui itens para vender>")) {
                                lore.set(3,  "§e<clique esquerdo novamente para vender>");
                                ItemStack x = e.getCurrentItem();
                                ItemMeta y = x.getItemMeta();
                                y.setLore(lore);
                                x.setItemMeta(y);
                                e.getInventory().setItem(e.getSlot(), x);
                                ((Player)e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 4f, 4f);
                                return;
                            }
                            if(lore.get(3).equalsIgnoreCase("§e<clique esquerdo novamente para vender>")) {
                                if(i.sellItem(p) == 0) {
                                    lore.set(3,  "§c<você não possui itens para vender>");
                                    ItemStack x = e.getCurrentItem();
                                    ItemMeta y = x.getItemMeta();
                                    y.setLore(lore);
                                    x.setItemMeta(y);
                                    e.getInventory().setItem(e.getSlot(), x);
                                    ((Player)e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ENTITY_IRON_GOLEM_HURT, 4f, 4f);
                                    return;
                                }
                                p.sendMessage("§aVocê vendeu todos os itens do seu inventário.");

                                e.getInventory().setItem(49, ItemUtils.getCustomItem(Material.COMMAND_BLOCK, 1, "§2Anuncios do Servidor", Arrays.asList("§7Clique aqui para ver", "§7os anuncios globais.", "§f", "§7Seu dinheiro: §a" + Main.eco.getBalance(p))));

                                lore.set(3,  "§a<clique esquerdo para vender>");
                                ItemStack x = e.getCurrentItem();
                                ItemMeta y = x.getItemMeta();
                                y.setLore(lore);
                                x.setItemMeta(y);
                                e.getInventory().setItem(e.getSlot(), x);
                                ((Player)e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ENTITY_VILLAGER_YES, 4f, 4f);
                                return;
                            }
                        }
                        return;
                    }
                });
            }
        }
    }

    public static void buyItems(MarketGUI gui, Player p) {
        MarketProfile pr = Main.getData().db.getServerMarket("server");
        if(pr.getAnnounceList().size() >= 1) {
            for(AnnounceProfile ap : pr.getMarketAnnounces()) {
                MarketAnnounce i = ap.getAnnounce();

                if(i.isSell()) {
                    continue;
                }

                ItemStack item = ItemUtils.getCustomItem(i.getStack().getItemStack(), Arrays.asList(
                        "§7Preço: §e" + i.getPrice(),
                        "§7Quantidade: §e" + i.getAmount(),
                        "§f",
                        !i.isSell() ? "§a<clique esquerdo para comprar>" : "§a<clique esquerdo para vender>",
                        p.hasPermission("magimarket.owner") ? "§4<clique direito para remover>" : ""));

                gui.addItem(item, e -> {
                    if(e.getClick() == ClickType.RIGHT) {
                        if(e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasLore()) {
                            List<String> lore = e.getCurrentItem().getItemMeta().getLore();
                            if(lore.size() >= 5) {
                                if(lore.get(4).equalsIgnoreCase("§4<clique direito para remover>")) {
                                    lore.set(4,  "§e<clique direito novamente para remover>");
                                    ItemStack x = e.getCurrentItem();
                                    ItemMeta y = x.getItemMeta();
                                    y.setLore(lore);
                                    x.setItemMeta(y);
                                    e.getInventory().setItem(e.getSlot(), x);
                                    ((Player)e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 4f, 4f);
                                    return;
                                }
                                if(lore.get(4).equalsIgnoreCase("§e<clique direito novamente para remover>")) {
                                    gui.replaceClick(e.getSlot(), null, ev -> {});
                                    ((Player)e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ENTITY_VILLAGER_DEATH, 4f, 4f);
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
                            //COMPRAR
                            if(lore.get(3).equalsIgnoreCase("§a<clique esquerdo para comprar>")) {
                                lore.set(3,  "§e<clique esquerdo novamente para comprar>");
                                ItemStack x = e.getCurrentItem();
                                ItemMeta y = x.getItemMeta();
                                y.setLore(lore);
                                x.setItemMeta(y);
                                e.getInventory().setItem(e.getSlot(), x);
                                ((Player)e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 4f, 4f);
                                return;
                            }
                            if(lore.get(3).equalsIgnoreCase("§e<clique esquerdo novamente para comprar>")) {
                                if(!Main.eco.has(p, i.getPrice())) {
                                    lore.set(3,  "§c<moedas insuficientes>");
                                    ItemStack x = e.getCurrentItem();
                                    ItemMeta y = x.getItemMeta();
                                    y.setLore(lore);
                                    x.setItemMeta(y);
                                    e.getInventory().setItem(e.getSlot(), x);
                                    ((Player)e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ENTITY_IRON_GOLEM_HURT, 4f, 4f);
                                    return;
                                }
                                if(p.getInventory().firstEmpty() == -1) {
                                    lore.set(3,  "§c<inventário cheio>");
                                    ItemStack x = e.getCurrentItem();
                                    ItemMeta y = x.getItemMeta();
                                    y.setLore(lore);
                                    x.setItemMeta(y);
                                    e.getInventory().setItem(e.getSlot(), x);
                                    ((Player)e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ENTITY_IRON_GOLEM_HURT, 4f, 4f);
                                    return;
                                }

                                if(!i.buyItem(p)) {
                                    lore.set(3,  "§c<ocorreu um erro>");
                                    ItemStack x = e.getCurrentItem();
                                    ItemMeta y = x.getItemMeta();
                                    y.setLore(lore);
                                    x.setItemMeta(y);
                                    e.getInventory().setItem(e.getSlot(), x);
                                    ((Player)e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ENTITY_IRON_GOLEM_HURT, 4f, 4f);
                                    return;
                                }
                                p.sendMessage("§aO item comprado foi colocado em seu inventário.");

                                e.getInventory().setItem(49, ItemUtils.getCustomItem(Material.COMMAND_BLOCK, 1, "§2Anuncios do Servidor", Arrays.asList("§7Clique aqui para ver", "§7os anuncios globais.", "§f", "§7Seu dinheiro: §a" + Main.eco.getBalance(p))));

                                lore.set(3,  "§a<clique esquerdo para comprar>");
                                ItemStack x = e.getCurrentItem();
                                ItemMeta y = x.getItemMeta();
                                y.setLore(lore);
                                x.setItemMeta(y);
                                e.getInventory().setItem(e.getSlot(), x);
                                ((Player)e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ENTITY_VILLAGER_YES, 4f, 4f);
                                return;
                            }
                        }
                        return;
                    }
                });
            }
        }
    }

}
