/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.amcserver.scoreboards;

import de.papaharni.amcserver.AMCServer;
import java.util.HashMap;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

/**
 *
 * @author Pappi
 */
public class SBMain {
    private final AMCServer _plugin;
    private final SBPvP _pvp;
    private final SBJumpnRun _jnr;
    private final SBStatistik _stats;
    private HashMap<String, Boolean> _visible = new HashMap<>();
    
    public SBMain(AMCServer plugin) {
        _plugin = plugin;
        _pvp = new SBPvP(_plugin);
        _jnr = new SBJumpnRun(_plugin);
        _stats = new SBStatistik(_plugin);
    }
    
    public void setVisible(Player p) {
        _visible.put(p.getName(), true);
    }
    
    public void setInvisible(Player p) {
        _visible.put(p.getName(), false);
    }
    
    public boolean getStatus(Player p) {
        return _visible.containsKey(p.getName())?_visible.get(p.getName()):false;
    }
    
    public void setScoreboard(Player p) {
        if(getStatus(p)) {
            if(_plugin.getMyConfig()._sbOnWorld.containsKey(p.getLocation().getWorld().getName().toLowerCase())) {
                setScoreboard(p, _plugin.getMyConfig()._sbOnWorld.get(p.getLocation().getWorld().getName().toLowerCase()));
            } else if(_plugin.getMyConfig()._sbOnWorld.containsKey("default")) {
                setScoreboard(p, _plugin.getMyConfig()._sbOnWorld.get("default"));
            } else {
                clearScoreboard(p);
            }
        }
    }
    
    public void setScoreboard(Player p, String sb) {
        switch(sb.toLowerCase()) {
            case "pvp":
              getPvP().setScoreboard(p);
              break;
            case "jumpnrun":
              getJnR().setScoreboard(p);
              break;
            case "statistik":
              getStats().setScoreboard(p);
              break;
            default:
              clearScoreboard(p);
              break;
        }
    }
    
    public void clearScoreboard(Player p) {
        Scoreboard board = null;
        board = p.getScoreboard();
        
        if(board == null)
            return;
        
        Objective obj = board.getObjective((p.getEntityId() + "AMCServer").substring(0, 15).toLowerCase());
        if(obj != null)
            obj.unregister();
        board.clearSlot(DisplaySlot.SIDEBAR);
        p.setScoreboard(board);
    }
    
    public SBJumpnRun getJnR() {
        return _jnr;
    }
    
    public SBPvP getPvP() {
        return _pvp;
    }
    
    public SBStatistik getStats() {
        return _stats;
    }
}
