package net.heyzeer0.mm.commands.subcmds;

import net.heyzeer0.mm.Main;
import net.heyzeer0.mm.configs.Lang;
import net.heyzeer0.mm.configs.MainConfig;
import net.heyzeer0.mm.database.entities.AnnounceProfile;
import net.heyzeer0.mm.interfaces.CommandExec;
import net.heyzeer0.mm.interfaces.annotation.Command;
import net.heyzeer0.mm.profiles.MarketAnnounce;
import net.heyzeer0.mm.profiles.WrappedStack;
import org.bukkit.Material;
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
        if(m.getInventory().getItemInMainHand().getType() == Material.AIR) {
            m.sendMessage(Lang.command_create_not_holding_item);
            return;
        }

        try{
            Integer amount = Integer.valueOf(args[1]);
            Integer price = Integer.valueOf(args[2]);
            boolean sell = Boolean.valueOf(args[3]);

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
            if(m.getInventory().getItemInMainHand().getAmount() < amount) {
                m.sendMessage(Lang.command_create_notenought_items);
                return;
            }
            ItemStack x = m.getInventory().getItemInMainHand().clone();
            if(m.getInventory().getItemInMainHand().getAmount() == 1) {
                m.getInventory().setItemInMainHand(null);
            }else{
                m.getInventory().getItemInMainHand().setAmount(m.getInventory().getItemInMainHand().getAmount() - amount);
            }

            x.setAmount(amount);

            MarketAnnounce announce = new MarketAnnounce(amount, new WrappedStack(x), price, m.getUniqueId(), true, sell, amount, System.currentTimeMillis(), true);
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
