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

/**
 *
 * @author Pappi
 */
public class amcregion implements CommandExecutor {
    private final AMCServer _plugin;
    
    public amcregion(AMCServer plugin) {
        _plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player)sender;
        if(p == null) {
            sender.sendMessage("Dieser Befehl kann nur von Spielern ausgeführt werden.");
            return true;
        }
        
        if(args.length >= 2) {
            switch(args[0].toLowerCase()) {
                case "create":
                    if(args.length >= 2) {
                        
                    }
                    break;
                case "redefine":
                    if(args.length >= 2) {
                        
                    }
                    break;
                case "emsg":
                    
                    break;
                case "lmsg":
                    
                    break;
                case "isjump":
                    
                    break;
                case "settp":
                    
                    break;
                case "teleport":
                    
                    break;
                default:
                    
                    break;
            }
        }
        return true;
    }
    
}
