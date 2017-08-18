package net.heyzeer0.mm.commands.subcmds;

import net.heyzeer0.mm.Main;
import net.heyzeer0.mm.configs.MainConfig;
import net.heyzeer0.mm.database.entities.AnnounceProfile;
import net.heyzeer0.mm.interfaces.CommandExec;
import net.heyzeer0.mm.interfaces.annotation.Command;
import net.heyzeer0.mm.profiles.MarketAnnounce;
import net.heyzeer0.mm.profiles.WrappedStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by HeyZeer0 on 17/08/2017.
 * Copyright © HeyZeer0 - 2016
 */
@Command(name = "create", permission = "magimarket.cmd.create", description = "Crie um novo anuncio com o item na sua mão")
public class CreateCommand implements CommandExec {

    @Override
    public void runCommand(Player m, String[] args) {
        if(args.length < 3) {
            m.sendMessage("§cUse: /" + MainConfig.main_command_prefix + " create [quantidade] [preço]");
            return;
        }
        if(m.getItemInHand() == null) {
            m.sendMessage("§cVocê precisa estar segurando um item!");
            return;
        }

        if(Main.getData().db().getUserProfile(m).getAnnounceList().size() + 1 > MainConfig.max_user_stock) {
            m.sendMessage("§cVocê excedeu a quantidade maxima de anuncios!");
            return;
        }

        try{
            Integer amount = Integer.valueOf(args[1]);
            Integer price = Integer.valueOf(args[2]);

            if(amount >= m.getItemInHand().getMaxStackSize()) {
                m.sendMessage("§cA quantidade não pode exceder uma stack!");
                return;
            }
            if(price <= 0) {
                m.sendMessage("§cO preço tem que ser maior que zero!");
                return;
            }
            if(amount <= 0) {
                m.sendMessage("§cA quantidade tem que ser maior que zero!");
                return;
            }
            if(m.getItemInHand().getAmount() < amount) {
                m.sendMessage("§cVocê não tem a quantidade de itens necessários!");
                return;
            }
            ItemStack x = m.getItemInHand().clone();
            if(m.getItemInHand().getAmount() == 1) {
                m.setItemInHand(null);
            }else{
                m.getItemInHand().setAmount(m.getItemInHand().getAmount() - amount);
            }

            x.setAmount(amount);

            MarketAnnounce announce = new MarketAnnounce(amount, new WrappedStack(x), price, m.getUniqueId(), false, false, amount, System.currentTimeMillis(), true);
            AnnounceProfile ann = new AnnounceProfile(announce);
            ann.save();

            Main.getData().db().getUserProfile(m).addMarketAnnounce(ann.getId());
            Main.getData().db().getServerMarket("global").addMarketAnnounce(ann.getId());

            m.sendMessage("§aAnuncio criado com sucesso!");

        }catch (Exception ex) {
            ex.printStackTrace();
            m.sendMessage("§cOs valores inseridos são invalidos.");
        }
    }

}
