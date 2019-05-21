package net.heyzeer0.mm.profiles;

import net.heyzeer0.mm.Main;
import net.heyzeer0.mm.database.entities.AnnounceProfile;
import net.heyzeer0.mm.gui.guis.GlobalGUI;
import net.heyzeer0.mm.utils.ChatUtils;
import net.wesjd.anvilgui.AnvilGUI;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by HeyZeer0 on 18/08/2017.
 * Copyright © HeyZeer0 - 2016
 */
public class AnnounceCreationProfile {

    private AnnounceProfile ex;
    private Player p;

    private int price = -1;

    public AnnounceCreationProfile(Player p , AnnounceProfile ex) {
        this.ex = ex;
        this.p = p;
    }

    public void start() {
        p.closeInventory();
        MarketAnnounce i = ex.getAnnounce();

        if(!Main.cauldron) {
            new AnvilGUI(Main.main, p, "Insira o preço", (player, msg) -> {

                if(price != -1) {
                    if(!NumberUtils.isNumber(msg.replace(".", "").replace(",", ""))) return "Quantidade Invalida";
                    Integer value = Integer.valueOf(msg.replace(".", "").replace(",", ""));

                    if(i.getStock() < value) return "Sem estoque";

                    p.sendMessage("§eValor escolhido: §f" + price);
                    p.sendMessage("§eQuantidade escolhida: §f" + value);
                    p.sendMessage("§aAnuncio publicado com sucesso!");

                    i.setAmountAndPriceAndMarket(value, price, true);
                    Main.getData().db().getAnnounce(ex.getId()).updateChanges(i);
                    Main.getData().db().getServerMarket("global").addMarketAnnounce(ex.getId());
                    return null;
                }

                if(!NumberUtils.isNumber(msg.replace(".", "").replace(",", ""))) return "Preço Invalido";

                Integer price = Integer.valueOf(msg.replace(".", "").replace(",", ""));
                if(price <= 0) return "Preço Invalido";

                this.price = price;

                return "Insira a quantidade";
            });
            return;
        }

        ChatUtils.waitForResponse(p, "§aDigite no chat o preço que deseja anunciar", (e, l) -> {
            try{
                Integer price = (Integer.valueOf(l.replace(".", "").replace(",", "")));
                if(price <= 0) {
                    p.sendMessage("§cO preço tem que ser superior que zero!");
                    return;
                }
                p.sendMessage("§eValor escolhido: §f" + price);

                ChatUtils.waitForResponse(p, "§aDigite no chat a quantidade que deseja anunciar", (e2, l2) -> {
                    try{
                        Integer value = Integer.valueOf(l2.replace(".", "").replace(",", ""));

                        if(i.getStock() < value) {
                            p.sendMessage("§cVocê não possui estoque suficiente!");
                            return;
                        }

                        p.sendMessage("§eValor escolhido: §f" + value);
                        p.sendMessage("§aAnuncio publicado com sucesso!");
                        i.setAmountAndPriceAndMarket(value, price, true);
                        Main.getData().db().getAnnounce(ex.getId()).updateChanges(i);
                        Main.getData().db().getServerMarket("global").addMarketAnnounce(ex.getId());

                    }catch (Exception ex) {
                        p.sendMessage("§cA quantidade inserida é invalida, tente novamente.");
                    }
                });

            }catch (Exception ex) {
                p.sendMessage("§cO preço inserido é invalido, tente novamente.");
            }
        });

    }



}
