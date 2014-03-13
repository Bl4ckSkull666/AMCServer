package de.papaharni.amcserver.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Pappi
 */
public class tpplayer implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("tpplayer")) {
            if(!sender.hasPermission("amcserver.tpplayer") && !sender.isOp()) {
                sender.sendMessage("Du hast keine Rechte um diesen Befehl zu verwenden.");
                return true;
            }
            
            if(args.length != 5) {
                sender.sendMessage("Bitte verwende /tpplayer {playername} {welt} {x} {y} {z}");
                return true;
            }
            
            Player pl = Bukkit.getPlayer(args[0]);
            if(pl == null) {
                sender.sendMessage("Konnte den angegebenen Spieler nicht finden.");
                sender.sendMessage("Bitte verwende /tpplayer {playername} {welt} {x} {y} {z}");
                return true;
            }
            World w = Bukkit.getWorld(args[1]);
            if(w == null) {
                sender.sendMessage("Konnte die angegebene Welt nicht finden.");
                sender.sendMessage("Bitte verwende /tpplayer {playername} {welt} {x} {y} {z}");
                return true;
            }
            
            if(!isNumeric(args[2]) || !isNumeric(args[3]) || !isNumeric(args[4])) {
                sender.sendMessage("Du hast einen Fehler in den Koordinaten gemacht.");
                sender.sendMessage("Bitte verwende /tpplayer {playername} {welt} {x} {y} {z}");
                return true;
            }
            
            int x = Integer.parseInt(args[2]);
            int y = Integer.parseInt(args[3]);
            int z = Integer.parseInt(args[4]);
            for(int i = y; i <= 254; i++) {
                Block b1 = w.getBlockAt(x, i, z);
                Block b2 = w.getBlockAt(x, i+1, z);
                if(b1.isEmpty() && b2.isEmpty()) {
                    Location loc = new Location(w, x, i, z);
                    pl.teleport(loc);
                    pl.sendMessage("Du wurdest von " + sender.getName() + " Teleportiert.");
                    sender.sendMessage("Spieler " + pl.getName() + " nach Welt:" + w.getName() + " - X:" + x + " - Y:" + i + " - Z:" + z + " teleportiert.");
                    return true;
                }
            }
            sender.sendMessage("Konnte Spieler " + pl.getName() + " leider nicht nach Welt:" + w.getName() + " - X:" + x + " - Y:" + y + " - Z:" + z + " teleportieren.");
            return true;
        }
        return false;
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
