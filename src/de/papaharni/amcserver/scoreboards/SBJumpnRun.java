/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.amcserver.scoreboards;

import de.papaharni.amcserver.AMCServer;
import de.papaharni.amcserver.util.JumpArena;
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
public final class SBJumpnRun {
    public static void setScoreboard(Player p) {
        AMCServer.getInstance().getLogger().log(Level.INFO, "Setze Jumpn Run Scoreboard auf Spieler " + p.getName());
        if(!AMCServer.getInstance().getMyConfig()._sbAvailable.containsKey("jnr"))
            return;
        
        if(!AMCServer.getInstance().getMyConfig()._sbAvailable.get("jnr"))
            return;
        
        AMCServer.getInstance().getLogger().log(Level.INFO, "Beginne mit dem Setzen des SB fuer JnR");
        Scoreboard board = p.getScoreboard();
        if(board == null)
            board = Bukkit.getScoreboardManager().getNewScoreboard();
        
        String sb_name = p.getEntityId() + "AMCServer";
        if(sb_name.length() > 16)
            sb_name = sb_name.substring(0, 15);
        
        Objective obj = board.getObjective(sb_name.toLowerCase());
        if(obj == null)
            obj = board.registerNewObjective(sb_name.toLowerCase(), "dummy");
        
        AMCServer.getInstance().getLogger().log(Level.INFO, "Objekt im SB erstellt fuer JnR");
        String titleColor = AMCServer.getInstance().getMyConfig()._sbColors.containsKey("statistik")?AMCServer.getInstance().getMyConfig()._sbColors.get("statistik"):"";
        if(obj != null && !obj.getDisplayName().contains("Jump-Stats")) {
            obj.unregister();
            obj = board.registerNewObjective(sb_name.toLowerCase(), "dummy");
            obj.setDisplayName(setColors(titleColor + "Jump-Stats"));
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);
            AMCServer.getInstance().getLogger().log(Level.INFO, "Title wurde gesetzt in JnR");
        }

        if(AMCServer.getInstance().getSBMain().getStatus(p)) {
            AMCServer.getInstance().getLogger().log(Level.INFO, "SB ist aktiv fuer spieler");
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
            
            for(JumpArena ja: AMCServer.getInstance().getJumps().getArenas(p.getName())) {
                AMCServer.getInstance().getLogger().log(Level.INFO, "Ermittle Werte fuer JnR " + ja.getArena());
                versuche += ja.getPlayed();
                gewonnen += ja.getWins();
                
                if(ja.getWins() > best_wins) {
                    best_wins = ja.getWins();
                    best_arena = ja.getArena();
                }
                if(ja.getPlayed() > most_played) {
                    most_played = ja.getPlayed();
                    most_arena = ja.getArena();
                }
            }
            if(!most_arena.isEmpty())
                obj.getScore(Bukkit.getOfflinePlayer(setColors((AMCServer.getInstance().getMyConfig()._sbColors.containsKey("jnr_top_play")?AMCServer.getInstance().getMyConfig()._sbColors.get("jnr_top_play"):"") + "Arena " + most_arena))).setScore(most_played);
            
            if(!best_arena.isEmpty())
                obj.getScore(Bukkit.getOfflinePlayer(setColors((AMCServer.getInstance().getMyConfig()._sbColors.containsKey("jnr_top_wins")?AMCServer.getInstance().getMyConfig()._sbColors.get("jnr_top_wins"):"") + "Arena " + best_arena))).setScore(best_wins);
            
            String versuchColor = AMCServer.getInstance().getMyConfig()._sbColors.containsKey("jnr_tot_play")?AMCServer.getInstance().getMyConfig()._sbColors.get("jnr_tot_play"):"";
            obj.getScore(Bukkit.getOfflinePlayer(setColors(versuchColor + "Versuche"))).setScore(versuche);
            
            String gewonnColor = AMCServer.getInstance().getMyConfig()._sbColors.containsKey("jnr_tot_wins")?AMCServer.getInstance().getMyConfig()._sbColors.get("jnr_tot_wins"):"";
            obj.getScore(Bukkit.getOfflinePlayer(setColors(gewonnColor + "Gewonnen"))).setScore(gewonnen);
            
            if(AMCServer.getInstance().getPlayerOnlineSince().containsKey(p.getName())) {
                String onColor = AMCServer.getInstance().getMyConfig()._sbColors.containsKey("online")?AMCServer.getInstance().getMyConfig()._sbColors.get("online"):"";
                long onlineMSecs = System.currentTimeMillis()-AMCServer.getInstance().getPlayerOnlineSince().get(p.getName());
                int minOnline = (int)((onlineMSecs/1000)/60);
                obj.getScore(Bukkit.getOfflinePlayer(setColors(onColor + "Onl.Minuten"))).setScore(minOnline);
            }
            
            
        }
    }
    
    public static String setColors(String str) {
        return ChatColor.translateAlternateColorCodes('&',str);
    }
}
