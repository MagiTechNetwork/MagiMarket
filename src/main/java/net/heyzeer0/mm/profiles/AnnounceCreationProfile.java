package net.heyzeer0.mm.profiles;

import net.heyzeer0.mm.Main;
import net.heyzeer0.mm.database.entities.AnnounceProfile;
import org.bukkit.entity.Player;

/**
 * Created by HeyZeer0 on 18/08/2017.
 * Copyright © HeyZeer0 - 2016
 */
public class AnnounceCreationProfile {

    AnnounceProfile ex;
    Player p;

    public AnnounceCreationProfile(Player p , AnnounceProfile ex) {
        this.ex = ex;
        this.p = p;
    }

    public void start() {
        p.closeInventory();
        MarketAnnounce i = ex.getAnnounce();

        Main.sign.open(p, new String[] {"", "^^^^", "Digite acima o", "preço"}, (p, l) -> {
            try{
                Integer price = (Integer.valueOf(l[0].replace(".", "").replace(",", "")));

                Main.sign.open(p, new String[] {"", "^^^^", "Digite acima a", "quantidade"}, (p2, l2) -> {
                    try{
                        Integer value = Integer.valueOf(l2[0].replace(".", "").replace(",", ""));

                        if(i.getStock() < value) {
                            p.sendMessage("§cVocê não possui estoque suficiente!");
                            return;
                        }
                        p.sendMessage("§aAnuncio publicado com sucesso!");
                        i.setAmountAndPriceAndMarket(value, price, true);
                        Main.getData().db().getAnnounce(ex.getId()).updateChanges(i);
                        Main.getData().db().getServerMarket("global").addMarketAnnounce(ex.getId());

                    }catch (Exception ex) {
                        p2.sendMessage("§cA quantidade inserida é invalida, tente novamente.");
                    }
                });

            }catch (Exception ex) {
                p.sendMessage("§cO preço inserido é invalido, tente novamente.");
            }
        });
    }



}
