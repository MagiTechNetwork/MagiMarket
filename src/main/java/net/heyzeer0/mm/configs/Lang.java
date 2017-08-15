package net.heyzeer0.mm.configs;

import net.heyzeer0.mm.interfaces.annotation.LangHelper;

/**
 * Created by HeyZeer0 on 14/08/2017.
 * Copyright © HeyZeer0 - 2016
 */
@LangHelper(default_name = "pt_BR")
public class Lang {

    public static String command_list_format = " §7-§e %s §7=§a %s";
    public static String command_help_description = "Lista todos os comandos.";
    public static String command_help_prefix = "§d§lMagi§a§lMarket §6>> Comandos de ajuda";
    public static String command_config_description = "Permite recarregar configurações";
    public static String command_config_prefix = "§d§lMagi§a§lMarket §6>> Configurações disponíveis";
    public static String command_config_success = "§e> Configuração recarregada com sucesso.";
    public static String command_config_success_warn = "§7§o(alterações como comando principal requerem recarregamento do plugin)";
    public static String command_config_error = "§cUm erro ocorreu ao recarregar a configuração, veja a console para mais detalhes.";
    public static String command_config_maincfg_description = "Configurações gerais e lang";

}
