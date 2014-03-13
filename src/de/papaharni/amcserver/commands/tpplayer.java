package de.papaharni.amcserver.commands;

import net.minecraft.server.v1_6_R3.Material;
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
            
            int x = Integer.parseInt(args[3]);
            int y = Integer.parseInt(args[4]);
            int z = Integer.parseInt(args[5]);
            for(int i = z; i <= 254; i++) {
                Block b1 = w.getBlockAt(x, y, i);
                Block b2 = w.getBlockAt(x, y, i+1);
                if(b1.getType().equals(Material.AIR) && b2.getType().equals(Material.AIR)) {
                    Location loc = new Location(w, x, y, i);
                    pl.teleport(loc);
                    sender.sendMessage("Spieler " + pl.getName() + " nach Welt:" + w.getName() + " - X:" + x + " - Y:" + y + " - Z:" + i);
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
