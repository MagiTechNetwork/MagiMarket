package net.heyzeer0.mm.configs;

import net.heyzeer0.mm.interfaces.annotation.LangHelper;

/**
 * Created by HeyZeer0 on 14/08/2017.
 * Copyright © HeyZeer0 - 2016
 */
@LangHelper(default_name = "pt_BR")
public class Lang {


    public static String market_gui_global_name = "MagiMarket - Anuncios Globais";
    public static String market_gui_server_name = "MagiMarket - Anuncios Servidor";
    public static String market_gui_stock_name = "MagiMarket - Seu Estoque";
    public static String command_list_format = " §7-§e %s §7=§a %s";
    public static String command_no_permission = "§cVocê não possui permissão para realizar este comando.";
    public static String command_help_description = "Lista todos os comandos.";
    public static String command_help_prefix = "§d§lMagi§a§lMarket §6>> Comandos de ajuda";
    public static String command_config_description = "Permite recarregar configurações";
    public static String command_config_prefix = "§d§lMagi§a§lMarket §6>> Configurações disponíveis";
    public static String command_config_success = "§e> Configuração recarregada com sucesso.";
    public static String command_config_success_warn = "§7§o(alterações como comando principal requerem recarregamento do plugin)";
    public static String command_config_error = "§cUm erro ocorreu ao recarregar a configuração, veja a console para mais detalhes.";
    public static String command_config_maincfg_description = "Configurações gerais";
    public static String command_config_lang_description = "Mensagens em geral";
    public static String command_create_description = "Cria um novo anuncio com o item na sua mão";
    public static String command_create_notenought_parameters = "§cUse: /%s create [quantidade] [preço]";
    public static String command_create_not_holding_item = "§cVocê precisa estar segurando um item!";
    public static String command_create_limit_exceeded = "§cVocê excedeu a quantidade maxima de anuncios!";
    public static String command_create_tax_notenought = "§cVocê não tem dinheiro suficiente para criar um anuncio!";
    public static String command_create_tax_value = "§cValor necessário: %s";
    public static String command_create_blacklist = "§cEste item encontra-se na blacklist.";
    public static String command_create_item_stacklimit = "§cA quantidade não pode exceder uma stack!";
    public static String command_create_price_morethan0 = "§cO preço tem que ser maior que zero!";
    public static String command_create_amount_morethan0 = "§cA quantidade tem que ser maior que zero!";
    public static String command_create_notenought_items = "§cVocê não tem a quantidade de itens necessários!";
    public static String command_create_sucess = "§aAnuncio criado com sucesso!";
    public static String command_create_sucess_tax = "§2Foram retirados §f%s§2 da sua conta.";
    public static String command_create_error_wrongvalues = "§cOs valores inseridos são invalidos.";
    public static String command_createserver_description = "Cria um novo anuncio com o item na sua mão";
    public static String command_createserver_notenought_parameters = "§cUse: /%s create [quantidade] [preço] [vender]";


}
