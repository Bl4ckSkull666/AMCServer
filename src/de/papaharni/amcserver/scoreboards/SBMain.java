/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.amcserver.scoreboards;

import de.papaharni.amcserver.AMCServer;
import java.util.HashMap;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

/**
 *
 * @author Pappi
 */
public final class SBMain {
    private static HashMap<String, Boolean> _visible = new HashMap<>();
    
    public static void setVisible(Player p) {
        _visible.put(p.getName(), true);
    }
    
    public static void setInvisible(Player p) {
        _visible.put(p.getName(), false);
    }
    
    public static boolean getStatus(Player p) {
        return _visible.containsKey(p.getName())?_visible.get(p.getName()):false;
    }
    
    public static void setScoreboard(Player p, Location loc) {
        if(getStatus(p)) {
            if(loc == null)
                loc = p.getLocation();
            if(AMCServer.getInstance().getMyConfig()._sbOnWorld.containsKey(loc.getWorld().getName().toLowerCase())) {
                setScoreboard(p, AMCServer.getInstance().getMyConfig()._sbOnWorld.get(loc.getWorld().getName().toLowerCase()));
            } else if(AMCServer.getInstance().getMyConfig()._sbOnWorld.containsKey("default")) {
                setScoreboard(p, AMCServer.getInstance().getMyConfig()._sbOnWorld.get("default"));
            } else {
                clearScoreboard(p);
            }
        }
    }
    
    public static void setScoreboard(Player p, String sb) {
        switch(sb.toLowerCase()) {
            case "pvp":
              SBPvP.setScoreboard(p);
              break;
            case "jumpnrun":
              SBJumpnRun.setScoreboard(p);
              break;
            case "statistik":
              SBStatistik.setScoreboard(p);
              break;
            default:
              clearScoreboard(p);
              break;
        }
    }
    
    public static void clearScoreboard(Player p) {
        Scoreboard board = null;
        board = p.getScoreboard();
        
        if(board == null)
            return;
        
        String sb_name = p.getEntityId() + "AMCServer";
        if(sb_name.length() > 16)
            sb_name = sb_name.substring(0, 15);
        
        Objective obj = board.getObjective(sb_name.toLowerCase());
        if(obj != null)
            obj.unregister();
        board.clearSlot(DisplaySlot.SIDEBAR);
        p.setScoreboard(board);
    }
}
