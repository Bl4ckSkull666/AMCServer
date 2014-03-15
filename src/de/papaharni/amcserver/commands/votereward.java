/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.amcserver.commands;

import de.papaharni.amcserver.AMCServer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Pappi
 */
public class votereward implements CommandExecutor{
    
    private AMCServer _plugin;
    
    public votereward(AMCServer plugin) {
        _plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player)sender;
        
        if(p == null) {
            sender.sendMessage("Nur ein Spieler kann diesen Befehl ausfuehren.");
            return true;
        }
        
        if(!_plugin.getMyConfig()._vrUseable.containsKey(p.getLocation().getWorld().getName().toLowerCase())) {
            sender.sendMessage("Dieser Befehl ist auf der Welt in der du dich befindest nicht verfügbar.");
            return true;
        }
        
        
        if(args.length < 1) {
            getHelpPage(p);
        } else if(args.length == 1) {
            switch(args[0]) {
                case "shop":
                    getBuyAble(p);
                    break;
                case "":
                    break;
                default:
                    getHelpPage(p);
            }
        } else if(args.length == 2) {
            //Args[0] == Shop ID
            //Args[1] == Menge
            if(!isNumeric(args[0]) && !isNumeric(args[1])) {
                p.sendMessage("Bitte verwende /votereward {Shop Id} {Menge}");
                return true;
            }
            
        }
        return true;
    }

    private boolean isNumeric(String str) {
        try {
            int num = Integer.valueOf(str);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }
    
    private void getHelpPage(Player p) {
        p.sendMessage("Dies ist die Hilfe-Seite des Vote Rewards Shops.");
        p.sendMessage("Verwende /votereward shop um dir den Verfügbaren Shop anzeigen zu lassen.");
        p.sendMessage("Benutze /votereward {shop Id} {menge} um einzukaufen.");
    }
    
    private void getBuyAble(Player p) {
        
    }
    
    private void getAvailibleOnWorld(Player p) {
        
    }
    
}
