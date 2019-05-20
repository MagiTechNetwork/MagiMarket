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
@Command(name = "create", permission = "magimarket.cmd.create", description = "Cria um novo anuncio com o item na sua mão")
public class CreateCommand implements CommandExec {

    @Override
    public void runCommand(Player m, String[] args) {
        if(args.length < 3) {
            m.sendMessage(String.format(Lang.command_create_notenought_parameters, MainConfig.main_command_prefix));
            return;
        }
        if(m.getInventory().getItemInMainHand().getType() == Material.AIR) {
            m.sendMessage(Lang.command_create_not_holding_item);
            return;
        }

        if(Main.getData().db().getUserProfile(m).getAnnounceList().size() + 1 > (m.hasPermission("magimarket.user.premium") ? MainConfig.max_premium_stock : MainConfig.max_user_stock)) {
            m.sendMessage(Lang.command_create_limit_exceeded);
            return;
        }

        if(MainConfig.tax_per_annouce != 0) {
            if(!Main.eco.has(m, MainConfig.tax_per_annouce)) {
                m.sendMessage(Lang.command_create_tax_notenought);
                m.sendMessage(String.format(Lang.command_create_tax_value, MainConfig.tax_per_annouce));
                return;
            }
        }

        if(Main.materials.contains(m.getItemInHand().getType())) {
            m.sendMessage(Lang.command_create_blacklist);
            return;
        }

        try{
            Integer amount = Integer.valueOf(args[1]);
            Integer price = Integer.valueOf(args[2]);

            if(amount > m.getInventory().getItemInMainHand().getMaxStackSize()) {
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

            MarketAnnounce announce = new MarketAnnounce(amount, new WrappedStack(x), price, m.getUniqueId(), false, false, amount, System.currentTimeMillis(), true);
            AnnounceProfile ann = new AnnounceProfile(announce);
            ann.save();

            Main.getData().db().getUserProfile(m).addMarketAnnounce(ann.getId());
            Main.getData().db().getServerMarket("global").addMarketAnnounce(ann.getId());

            m.sendMessage(Lang.command_create_sucess);
            if(MainConfig.tax_per_annouce != 0) {
                m.sendMessage(String.format(Lang.command_create_sucess_tax, MainConfig.tax_per_annouce));
            }

        }catch (Exception ex) {
            ex.printStackTrace();
            m.sendMessage(Lang.command_create_error_wrongvalues);
        }
    }

}
