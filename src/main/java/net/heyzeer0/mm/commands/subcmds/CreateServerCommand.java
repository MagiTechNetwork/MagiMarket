package net.heyzeer0.mm.commands.subcmds;

import net.heyzeer0.mm.Main;
import net.heyzeer0.mm.configs.Lang;
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
@Command(name = "createserver", permission = "magimarket.cmd.createserver", description = "Crie um novo anuncio no servidor com o item na sua mão")
public class CreateServerCommand implements CommandExec {

    @Override
    public void runCommand(Player m, String[] args) {
        if(args.length < 4) {
            m.sendMessage(String.format(Lang.command_createserver_notenought_parameters, MainConfig.main_command_prefix));
            return;
        }
        if(m.getItemInHand() == null) {
            m.sendMessage(Lang.command_create_not_holding_item);
            return;
        }

        try{
            Integer amount = Integer.valueOf(args[1]);
            Integer price = Integer.valueOf(args[2]);
            Boolean vender = Boolean.valueOf(args[3]);

            if(amount > m.getItemInHand().getMaxStackSize()) {
                m.sendMessage(Lang.command_create_item_stacklimit);
                return;
            }
            if(price <= 0) {
                m.sendMessage(Lang.command_create_price_morethan0);
                return;
            }
            if(amount <= 0) {
                m.sendMessage(Lang.command_create_amount_morethan0);
                return;
            }
            if(m.getItemInHand().getAmount() < amount) {
                m.sendMessage(Lang.command_create_notenought_items);
                return;
            }
            ItemStack x = m.getItemInHand().clone();
            if(m.getItemInHand().getAmount() == 1) {
                m.setItemInHand(null);
            }else{
                m.getItemInHand().setAmount(m.getItemInHand().getAmount() - amount);
            }

            x.setAmount(amount);

            MarketAnnounce announce = new MarketAnnounce(amount, new WrappedStack(x), price, m.getUniqueId(), true, vender, amount, System.currentTimeMillis(), true);
            AnnounceProfile ann = new AnnounceProfile(announce);
            ann.save();

            Main.getData().db().getServerMarket("server").addMarketAnnounce(ann.getId());

            m.sendMessage(Lang.command_create_sucess);

        }catch (Exception ex) {
            ex.printStackTrace();
            m.sendMessage(Lang.command_create_error_wrongvalues);
        }
    }

}
