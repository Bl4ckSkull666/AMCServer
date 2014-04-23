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
        
        String sb_name = p.getEntityId() + "AMCServer";
        if(sb_name.length() > 16)
            sb_name = sb_name.substring(0, 15);
        
        Objective obj = board.getObjective(sb_name.toLowerCase());
        if(obj == null)
            obj = board.registerNewObjective(sb_name.toLowerCase(), "dummy");
        
        String titleColor = _plugin.getMyConfig()._sbColors.containsKey("statistik")?_plugin.getMyConfig()._sbColors.get("statistik"):"";
        if(obj != null && !obj.getDisplayName().contains("Jump-Stats")) {
            obj.unregister();
            obj = board.registerNewObjective(sb_name.toLowerCase(), "dummy");
            obj.setDisplayName(setColors(titleColor + "Jump-Stats"));
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        }
        
        if(!_plugin.getSBMain().getStatus(p)) {
            if(obj.getDisplaySlot() == null)
                obj.setDisplaySlot(DisplaySlot.SIDEBAR);
            if(!obj.getDisplaySlot().equals(DisplaySlot.SIDEBAR))
                obj.setDisplaySlot(DisplaySlot.SIDEBAR);
            
            int versuche = 0;
            int gewonnen = 0;
            
            String best_arena = "";
            int best_wins = 0;
            String best_color = "";
            
            String most_arena = "";
            int most_played = 0;
            String most_color = "";
            
            for(JumpArena ja: _plugin.getJumps().getArenas(p)) {
                versuche += ja.getPlayed();
                gewonnen += ja.getWins();
                
                if(ja.getWins() > best_wins) {
                    best_wins = ja.getWins();
                    best_arena = ja.getArena();
                    best_color = _plugin.getMyConfig()._sbColors.containsKey("arena" + ja.getArena())?_plugin.getMyConfig()._sbColors.get("arena" + ja.getArena()):"";
                }
                if(ja.getPlayed() > most_played) {
                    most_played = ja.getPlayed();
                    most_arena = ja.getArena();
                    most_color = _plugin.getMyConfig()._sbColors.containsKey("arena" + ja.getArena())?_plugin.getMyConfig()._sbColors.get("arena" + ja.getArena()):"";
                }
            }
            if(!most_arena.isEmpty())
                obj.getScore(Bukkit.getOfflinePlayer(setColors(most_color + "Arena " + most_arena))).setScore(most_played);
            
            if(!best_arena.isEmpty())
                obj.getScore(Bukkit.getOfflinePlayer(setColors(best_color + "Arena " + best_arena))).setScore(best_wins);
            
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
