/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.amcserver.scoreboards;

import com.dmgkz.mcjobs.playerdata.PlayerCache;
import com.pwn9.PwnFilter.util.PointManager;
import de.papaharni.amcserver.AMCServer;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

/**
 *
 * @author Pappi
 */
public class SBStatistik {
    
    private AMCServer _plugin;
    
    public SBStatistik(AMCServer plugin) {
        _plugin = plugin;
    }
    
    public void setScoreboard(Player p) {
        Scoreboard board = p.getScoreboard();
        if(board == null)
            board = Bukkit.getScoreboardManager().getNewScoreboard();
        
        Objective obj = board.getObjective((p.getEntityId() + "AMCServer").substring(0, 15).toLowerCase());
        if(obj == null || !obj.getDisplayName().contains("Statistik"))
            obj = board.registerNewObjective((p.getEntityId() + "AMCServer").substring(0, 15).toLowerCase(), "dummy");
        
        if(!_plugin.getSBMain().getStatus(p)) {
            boolean isChange = false;
            if(obj.getDisplaySlot() != DisplaySlot.SIDEBAR)
            {
                obj.setDisplaySlot(DisplaySlot.SIDEBAR);
                String titleColor = _plugin.getMyConfig()._sbColors.containsKey("statistik")?_plugin.getMyConfig()._sbColors.get("statistik"):"";
                obj.setDisplayName(setColors(titleColor + "Statistik"));
            }
            
            //Economy Use
            int money = (int)Math.floor((float)_plugin.getEconomy().getBalance(p.getName()));
            String ecoColor = _plugin.getMyConfig()._sbColors.containsKey("money")?_plugin.getMyConfig()._sbColors.get("money"):"";
            if(obj.getScore(Bukkit.getOfflinePlayer(setColors(ecoColor + _plugin.getEconomy().currencyNamePlural()))).getScore() != money) {
                obj.getScore(Bukkit.getOfflinePlayer(setColors(ecoColor + _plugin.getEconomy().currencyNamePlural()))).setScore(money);
            }
            
            //McJobs Use
            if(_plugin.isMcJobs() && PlayerCache.getJobCount(p.getName()) > 0)
            {
                String mcColor = _plugin.getMyConfig()._sbColors.containsKey("mcjobs")?_plugin.getMyConfig()._sbColors.get("mcjobs"):"";
                
                for(String jobname: PlayerCache.getPlayerJobs(p.getName())) {
                    int lvl = PlayerCache.getJobLevel(p.getName(), jobname);
                    jobname = Character.toUpperCase(jobname.charAt(0)) + jobname.substring(1);
                    obj.getScore(Bukkit.getOfflinePlayer(setColors(mcColor + jobname))).setScore(lvl);
                }
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
                String fColor = _plugin.getMyConfig()._sbColors.containsKey("foins")?_plugin.getMyConfig()._sbColors.get("foins"):"";
                String spbColor = _plugin.getMyConfig()._sbColors.containsKey("sparbuch")?_plugin.getMyConfig()._sbColors.get("sparbuch"):"";
                String vColor = _plugin.getMyConfig()._sbColors.containsKey("votes")?_plugin.getMyConfig()._sbColors.get("votes"):"";
                String spColor = _plugin.getMyConfig()._sbColors.containsKey("spenden")?_plugin.getMyConfig()._sbColors.get("spenden"):"";

                obj.getScore(Bukkit.getOfflinePlayer(setColors(fColor + "Foins"))).setScore(fvsp[0]);
                obj.getScore(Bukkit.getOfflinePlayer(setColors(spbColor + "Sparbuch"))).setScore(fvsp[1]);
                obj.getScore(Bukkit.getOfflinePlayer(setColors(vColor + "Vote-Punkte"))).setScore(fvsp[2]);
                obj.getScore(Bukkit.getOfflinePlayer(setColors(spColor + "SP-Punkte"))).setScore(fvsp[3]);
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
