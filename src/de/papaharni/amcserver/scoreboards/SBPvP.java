/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.amcserver.scoreboards;

import com.pwn9.PwnFilter.util.PointManager;
import de.papaharni.amcserver.AMCServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

/**
 *
 * @author Pappi
 */
public class SBPvP {
    private AMCServer _plugin;
    
    public SBPvP(AMCServer plugin) {
        _plugin = plugin;
    }
    
    public void setScoreboard(Player p) {
        Scoreboard board = p.getScoreboard();
        if(board == null)
            board = Bukkit.getScoreboardManager().getNewScoreboard();
        
        Objective obj = board.getObjective((p.getEntityId() + "AMCServer").substring(0, 15).toLowerCase());

        if(obj != null && !obj.getDisplayName().contains("PvP-Stats"))
            obj.unregister();

        if(obj == null)
            obj = board.registerNewObjective((p.getEntityId() + "AMCServer").substring(0, 15).toLowerCase(), "dummy");
        
        if(!_plugin.getSBMain().getStatus(p)) {    
            if(obj.getDisplaySlot() != DisplaySlot.SIDEBAR) {
                obj.setDisplaySlot(DisplaySlot.SIDEBAR);
                String titleColor = _plugin.getMyConfig()._sbColors.containsKey("statistik")?_plugin.getMyConfig()._sbColors.get("statistik"):"";
                obj.setDisplayName(setColors(titleColor + "PvP-Stats"));
            }
            
            String winColor = _plugin.getMyConfig()._sbColors.containsKey("wins")?_plugin.getMyConfig()._sbColors.get("wins"):"";
            String loseColor = _plugin.getMyConfig()._sbColors.containsKey("lose")?_plugin.getMyConfig()._sbColors.get("lose"):"";
            String scoColor = _plugin.getMyConfig()._sbColors.containsKey("score")?_plugin.getMyConfig()._sbColors.get("score"):"";
            
            obj.getScore(Bukkit.getOfflinePlayer(setColors(winColor + "Gewonnen"))).setScore(_plugin.getPvPCs().getPvPCounter(p.getName()).getWins());
            obj.getScore(Bukkit.getOfflinePlayer(setColors(loseColor + "Verloren"))).setScore(_plugin.getPvPCs().getPvPCounter(p.getName()).getLose());
            obj.getScore(Bukkit.getOfflinePlayer(setColors(scoColor + "Score"))).setScore(_plugin.getPvPCs().getPvPCounter(p.getName()).getScore());
            
            //Economy Use
            int money = (int)Math.floor((float)_plugin.getEconomy().getBalance("pvp" + p.getName()));
            String ecoColor = _plugin.getMyConfig()._sbColors.containsKey("pvpm")?_plugin.getMyConfig()._sbColors.get("pvpm"):"";
            if(obj.getScore(Bukkit.getOfflinePlayer(setColors(ecoColor + "PvP-Muenzen"))).getScore() != money) {
                obj.getScore(Bukkit.getOfflinePlayer(setColors(ecoColor + "PvP-Muenzen"))).setScore(money);
            }
            
            if(_plugin.isPwnFilter()) {
                String pwnfColor = _plugin.getMyConfig()._sbColors.containsKey("pwnfilter")?_plugin.getMyConfig()._sbColors.get("pwnfilter"):"";
                int points = PointManager.getInstance().getPlayerPoints(p).intValue();
                obj.getScore(Bukkit.getOfflinePlayer(setColors(pwnfColor + "Wort-Punkte"))).setScore(points);
            }
            
            if(_plugin.getPlayerOnlineSince().containsKey(p.getName())) {
                String onColor = _plugin.getMyConfig()._sbColors.containsKey("online")?_plugin.getMyConfig()._sbColors.get("online"):"";
                long onlineMSecs = System.currentTimeMillis()-_plugin.getPlayerOnlineSince().get(p.getName());
                int minOnline = (int)((onlineMSecs/1000)/60);
                obj.getScore(Bukkit.getOfflinePlayer(setColors(onColor + "Onl.Minuten"))).setScore(minOnline);
            }
            
            if(_plugin.getMyConfig()._tmysql.containsKey("bb1_users")) {
                int[] fvsp = _plugin.getMySQL().getStats().getFromBB(p);
                String vColor = _plugin.getMyConfig()._sbColors.containsKey("votes")?_plugin.getMyConfig()._sbColors.get("votes"):"";
                obj.getScore(Bukkit.getOfflinePlayer(setColors(vColor + "Vote-Punkte"))).setScore(fvsp[2]);
            }

            if(p.getScoreboard() != board)
                p.setScoreboard(board);
        } else {
            obj.unregister();
            board.clearSlot(DisplaySlot.SIDEBAR);
            p.setScoreboard(board);
        }
    }
    
    public String setColors(String str) {
        return ChatColor.translateAlternateColorCodes('&',str);
    }
}
