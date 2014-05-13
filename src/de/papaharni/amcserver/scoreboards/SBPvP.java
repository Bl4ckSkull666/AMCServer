/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.amcserver.scoreboards;

import com.pwn9.PwnFilter.util.PointManager;
import de.papaharni.amcserver.AMCServer;
import java.util.logging.Level;
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
public final class SBPvP {
    public static void setScoreboard(Player p) {
        if(!AMCServer.getInstance().getMyConfig()._sbAvailable.containsKey("pvp"))
            return;
        
        if(!AMCServer.getInstance().getMyConfig()._sbAvailable.get("pvp"))
            return;
        
        Scoreboard board = p.getScoreboard();
        if(board == null)
            board = Bukkit.getScoreboardManager().getNewScoreboard();
        
        String sb_name = p.getEntityId() + "AMCServer";
        if(sb_name.length() > 16)
            sb_name = sb_name.substring(0, 15);
        
        Objective obj = board.getObjective(sb_name.toLowerCase());
        if(obj == null)
            obj = board.registerNewObjective(sb_name.toLowerCase(), "dummy");
        
        String titleColor = AMCServer.getInstance().getMyConfig()._sbColors.containsKey("statistik")?AMCServer.getInstance().getMyConfig()._sbColors.get("statistik"):"";
        if(obj != null && !obj.getDisplayName().contains("PvP-Stats")) {
            obj.unregister();
            obj = board.registerNewObjective(sb_name.toLowerCase(), "dummy");
            obj.setDisplayName(setColors(titleColor + "PvP-Stats"));
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        }
        
        if(AMCServer.getInstance().getSBMain().getStatus(p)) {
            if(obj.getDisplaySlot() == null)
                obj.setDisplaySlot(DisplaySlot.SIDEBAR);
            if(!obj.getDisplaySlot().equals(DisplaySlot.SIDEBAR))
                obj.setDisplaySlot(DisplaySlot.SIDEBAR);
            
            String winColor = AMCServer.getInstance().getMyConfig()._sbColors.containsKey("wins")?AMCServer.getInstance().getMyConfig()._sbColors.get("wins"):"";
            String loseColor = AMCServer.getInstance().getMyConfig()._sbColors.containsKey("lose")?AMCServer.getInstance().getMyConfig()._sbColors.get("lose"):"";
            String scoColor = AMCServer.getInstance().getMyConfig()._sbColors.containsKey("score")?AMCServer.getInstance().getMyConfig()._sbColors.get("score"):"";
            
            obj.getScore(Bukkit.getOfflinePlayer(setColors(winColor + "Gewonnen"))).setScore(AMCServer.getInstance().getPvPCs().getPvPCounter(p.getName()).getWins());
            obj.getScore(Bukkit.getOfflinePlayer(setColors(loseColor + "Verloren"))).setScore(AMCServer.getInstance().getPvPCs().getPvPCounter(p.getName()).getLose());
            obj.getScore(Bukkit.getOfflinePlayer(setColors(scoColor + "Score"))).setScore(AMCServer.getInstance().getPvPCs().getPvPCounter(p.getName()).getScore());
            
            //Economy Use
            int money = (int)Math.floor((float)AMCServer.getInstance().getEconomy().getBalance("pvp" + p.getName()));
            String ecoColor = AMCServer.getInstance().getMyConfig()._sbColors.containsKey("pvpm")?AMCServer.getInstance().getMyConfig()._sbColors.get("pvpm"):"";
            if(obj.getScore(Bukkit.getOfflinePlayer(setColors(ecoColor + "PvP-Muenzen"))).getScore() != money) {
                obj.getScore(Bukkit.getOfflinePlayer(setColors(ecoColor + "PvP-Muenzen"))).setScore(money);
            }
            
            if(AMCServer.getInstance().isPwnFilter()) {
                String pwnfColor = AMCServer.getInstance().getMyConfig()._sbColors.containsKey("pwnfilter")?AMCServer.getInstance().getMyConfig()._sbColors.get("pwnfilter"):"";
                int points = PointManager.getInstance().getPlayerPoints(p).intValue();
                obj.getScore(Bukkit.getOfflinePlayer(setColors(pwnfColor + "Wort-Punkte"))).setScore(points);
            }
            
            if(AMCServer.getInstance().getPlayerOnlineSince().containsKey(p.getName())) {
                String onColor = AMCServer.getInstance().getMyConfig()._sbColors.containsKey("online")?AMCServer.getInstance().getMyConfig()._sbColors.get("online"):"";
                long onlineMSecs = System.currentTimeMillis()-AMCServer.getInstance().getPlayerOnlineSince().get(p.getName());
                int minOnline = (int)((onlineMSecs/1000)/60);
                obj.getScore(Bukkit.getOfflinePlayer(setColors(onColor + "Onl.Minuten"))).setScore(minOnline);
            }
            
            if(AMCServer.getInstance().getMyConfig()._tmysql.containsKey("bb1_users")) {
                int[] fvsp = AMCServer.getInstance().getMySQL().getStats().getFromBB(p);
                String vColor = AMCServer.getInstance().getMyConfig()._sbColors.containsKey("votes")?AMCServer.getInstance().getMyConfig()._sbColors.get("votes"):"";
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
    
    public static String setColors(String str) {
        return ChatColor.translateAlternateColorCodes('&',str);
    }
}
