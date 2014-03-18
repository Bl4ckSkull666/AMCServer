/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.amcserver.scoreboards;

import de.papaharni.amcserver.AMCServer;
import de.papaharni.amcserver.util.JumpArena;
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
public class SBJumpnRun {
    private AMCServer _plugin;
    
    public SBJumpnRun(AMCServer plugin) {
        _plugin = plugin;
    }
    
    public void setScoreboard(Player p) {
        Scoreboard board = p.getScoreboard();
        if(board == null)
            board = Bukkit.getScoreboardManager().getNewScoreboard();
        
        Objective obj = board.getObjective((p.getEntityId() + "AMCServer").substring(0, 15).toLowerCase());

        if(obj != null && !obj.getDisplayName().contains("Jump-Stats"))
            obj.unregister();

        if(obj == null)
            obj = board.registerNewObjective((p.getEntityId() + "AMCServer").substring(0, 15).toLowerCase(), "dummy");
        
        if(!_plugin.getSBMain().getStatus(p)) {    
            if(obj.getDisplaySlot() != DisplaySlot.SIDEBAR) {
                obj.setDisplaySlot(DisplaySlot.SIDEBAR);
                String titleColor = _plugin.getMyConfig()._sbColors.containsKey("statistik")?_plugin.getMyConfig()._sbColors.get("statistik"):"";
                obj.setDisplayName(setColors(titleColor + "Jump-Stats"));
            }
            
            int versuche = 0;
            int gewonnen = 0;
            
            int best_arenaid = 0;
            int best_wins = 0;
            String best_color = "";
            
            int most_arenaid = 0;
            int most_played = 0;
            String most_color = "";
            
            for(JumpArena ja: _plugin.getJumps().getArenas(p)) {
                versuche += ja.getPlayed();
                gewonnen += ja.getWins();
                
                if(ja.getWins() > best_wins) {
                    best_wins = ja.getWins();
                    best_arenaid = ja.getArenaId();
                    best_color = _plugin.getMyConfig()._sbColors.containsKey("arena" + ja.getArenaId())?_plugin.getMyConfig()._sbColors.get("arena" + ja.getArenaId()):"";
                }
                if(ja.getPlayed() > most_played) {
                    most_played = ja.getPlayed();
                    most_arenaid = ja.getArenaId();
                    most_color = _plugin.getMyConfig()._sbColors.containsKey("arena" + ja.getArenaId())?_plugin.getMyConfig()._sbColors.get("arena" + ja.getArenaId()):"";
                }
            }
            if(most_arenaid > 0)
                obj.getScore(Bukkit.getOfflinePlayer(setColors(most_color + "Arena " + most_arenaid))).setScore(most_played);
            
            if(best_arenaid > 0)
                obj.getScore(Bukkit.getOfflinePlayer(setColors(best_color + "Arena " + best_arenaid))).setScore(best_wins);
            
            String versuchColor = _plugin.getMyConfig()._sbColors.containsKey("jnr_versuch")?_plugin.getMyConfig()._sbColors.get("jnr_versuch"):"";
            obj.getScore(Bukkit.getOfflinePlayer(setColors(versuchColor + "Versuche"))).setScore(versuche);
            
            String gewonnColor = _plugin.getMyConfig()._sbColors.containsKey("jnr_gewonn")?_plugin.getMyConfig()._sbColors.get("jnr_gewonn"):"";
            obj.getScore(Bukkit.getOfflinePlayer(setColors(gewonnColor + "Gewonnen"))).setScore(gewonnen);
            
            if(_plugin.getPlayerOnlineSince().containsKey(p.getName())) {
                String onColor = _plugin.getMyConfig()._sbColors.containsKey("online")?_plugin.getMyConfig()._sbColors.get("online"):"";
                long onlineMSecs = System.currentTimeMillis()-_plugin.getPlayerOnlineSince().get(p.getName());
                int minOnline = (int)((onlineMSecs/1000)/60);
                obj.getScore(Bukkit.getOfflinePlayer(setColors(onColor + "Onl.Minuten"))).setScore(minOnline);
            }
        }
    }
    
    public String setColors(String str) {
        return ChatColor.translateAlternateColorCodes('&',str);
    }
}
