package de.papaharni.amcserver.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Pappi
 */
public class run implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player)sender;
        if(p == null) {
            sender.sendMessage("Dieser Befehl kann nur von Spielern ausgeführt werden.");
            return true;
        }
        
        boolean hasAnyPermissions = p.isOp()?true:false;
        for(int i = 1; i <= 10; i++) {
            if(p.hasPermission("run.stufe." + i)) {
                hasAnyPermissions = true;
            }
        }
        
        if(!hasAnyPermissions) {
            p.sendMessage("Du hast keine Rechte um diesen Befehl zu verwenden.");
            return true;
        }
        
        if(args.length < 1) {
            p.sendMessage("Bitte gib eine Geschwindigkeit zwischen 1 und 10 an");
            return true;
        }
        
        if(!isNumeric(args[0])) {
            p.sendMessage("Bitte gib eine Geschwindigkeit zwischen 1 und 10 an");
            return true;
        }
        
        if(!p.hasPermission("run.stufe." + args[0]) && !p.isOp()) {
            p.sendMessage("Du hast keine Rechte um diese Geschwindigkeits Stufe zu verwenden.");
            return true;
        }

        int spd = Integer.parseInt(args[0]);
        if(spd < 1 || spd > 10) {
            p.sendMessage("Die Geschwindigkeits Stufe muss zwischen 1 und 10 liegen.");
            return true;
        }

        float f = ((float)spd/10);
        p.setFlySpeed(f);
        p.setWalkSpeed(f);
        p.sendMessage("Du Rennst und Fliegst nun mit Geschwindigkeits Stufe " + args[0]);
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
}
