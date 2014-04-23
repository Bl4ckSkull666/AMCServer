/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.amcserver.scoreboards;

import com.dmgkz.mcjobs.playerdata.PlayerCache;
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
public final class SBStatistik {
    
    public static void setScoreboard(Player p) {
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
        if(obj != null && !obj.getDisplayName().contains("Statistik")) {
            obj.unregister();
            obj = board.registerNewObjective(sb_name.toLowerCase(), "dummy");
            obj.setDisplayName(setColors(titleColor + "Statistik"));
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        }
        
        if(AMCServer.getInstance().getSBMain().getStatus(p)) {
            if(obj.getDisplaySlot() == null)
                obj.setDisplaySlot(DisplaySlot.SIDEBAR);
            if(!obj.getDisplaySlot().equals(DisplaySlot.SIDEBAR))
                obj.setDisplaySlot(DisplaySlot.SIDEBAR);
            
            //Economy Use
            int money = (int)Math.floor((float)AMCServer.getInstance().getEconomy().getBalance(p.getName()));
            String ecoColor = AMCServer.getInstance().getMyConfig()._sbColors.containsKey("money")?AMCServer.getInstance().getMyConfig()._sbColors.get("money"):"";
            if(obj.getScore(Bukkit.getOfflinePlayer(setColors(ecoColor + AMCServer.getInstance().getEconomy().currencyNamePlural()))).getScore() != money) {
                obj.getScore(Bukkit.getOfflinePlayer(setColors(ecoColor + AMCServer.getInstance().getEconomy().currencyNamePlural()))).setScore(money);
            }
            
            //McJobs Use
            if(AMCServer.getInstance().isMcJobs() && PlayerCache.getJobCount(p.getName()) > 0) {
                String mcColor = AMCServer.getInstance().getMyConfig()._sbColors.containsKey("mcjobs")?AMCServer.getInstance().getMyConfig()._sbColors.get("mcjobs"):"";
                
                for(String jobname: PlayerCache.getPlayerJobs(p.getName())) {
                    int lvl = PlayerCache.getJobLevel(p.getName(), jobname);
                    jobname = Character.toUpperCase(jobname.charAt(0)) + jobname.substring(1);
                    obj.getScore(Bukkit.getOfflinePlayer(setColors(mcColor + jobname))).setScore(lvl);
                }
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
                String fColor = AMCServer.getInstance().getMyConfig()._sbColors.containsKey("foins")?AMCServer.getInstance().getMyConfig()._sbColors.get("foins"):"";
                String spbColor = AMCServer.getInstance().getMyConfig()._sbColors.containsKey("sparbuch")?AMCServer.getInstance().getMyConfig()._sbColors.get("sparbuch"):"";
                String vColor = AMCServer.getInstance().getMyConfig()._sbColors.containsKey("votes")?AMCServer.getInstance().getMyConfig()._sbColors.get("votes"):"";
                String spColor = AMCServer.getInstance().getMyConfig()._sbColors.containsKey("spenden")?AMCServer.getInstance().getMyConfig()._sbColors.get("spenden"):"";

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
    
    public static String setColors(String str) {
        return ChatColor.translateAlternateColorCodes('&',str);
    }
}
