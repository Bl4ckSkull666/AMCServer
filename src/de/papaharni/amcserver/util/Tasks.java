/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.amcserver.util;

import de.papaharni.amcserver.AMCServer;
import java.util.Calendar;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

/**
 *
 * @author Pappi
 */
public class Tasks {
    private AMCServer _plugin;
    
    public Tasks(AMCServer plugin) {
        _plugin = plugin;
        startMcJobsUpdater();
        startScoreboardUpdater();
        startWorldTimeUpdater();
    }
    
    public void startScoreboardUpdater() {
        Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(_plugin, new updateScoreboard(), (_plugin.getMyConfig()._mcjobs_interval*20));
    }
    
    public class updateScoreboard implements Runnable {
        public void run() {
            for(Player p: _plugin.getServer().getOnlinePlayers()) {
                if(_plugin.getSBMain().getStatus(p))
                    _plugin.getSBMain().setScoreboard(p);
            }
            startScoreboardUpdater();
        }
    }
    
    public void startMcJobsUpdater() {
        Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(_plugin, new updateMcJobs(), (_plugin.getMyConfig()._mcjobs_interval*20));
    }
    
    public class updateMcJobs implements Runnable {
        public void run() {
            for(Player p: _plugin.getServer().getOnlinePlayers()) {
                _plugin.getMySQL().setMcJobsByPlayer(p);
            }
            startMcJobsUpdater();
        }
    }
    
    public void startWorldTimeUpdater() {
        Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(_plugin, new updateWorldTime(), (_plugin.getMyConfig()._mcjobs_interval*20));
    }
    
    public class updateWorldTime implements Runnable {
        public void run() {
            for (Map.Entry<String, String> e : _plugin.getMyConfig()._wTimes.entrySet()) {
                if(Bukkit.getWorld(e.getKey()) == null)
                    _plugin.getLog().error("");
                else {
                    World w = Bukkit.getWorld(e.getKey());
                    if(w == null)
                                break;
                    String[] sa = e.getValue().split(";");
                    switch(sa[0]) {
                        case "24h":
                            //format 24h
                            setWorldToCurrentTime(w);
                            break;
                        case "time":
                            //Format time;15:00
                            if(sa.length <= 1)
                                break;
                            setWorldToTime(w, sa[1]);
                        default:
                            break;
                            
                    }
                }
            }
            startWorldTimeUpdater();
        }
    }
    
    private void setWorldToCurrentTime(World w) {
        Calendar today = Calendar.getInstance();
        today.setTimeInMillis(System.currentTimeMillis());
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        if(today.getTimeInMillis() >= System.currentTimeMillis())
            return;
        long diff = System.currentTimeMillis()-today.getTimeInMillis();
        long ticks = (long)Math.round(240*((100/86400)*(double)diff));
        if(ticks > 6000)
            ticks -= 6000;
        else
            ticks += 18000;
        w.setTime(ticks);
    }
    
    private void setWorldToTime(World w, String strTime) {
        String[] t = strTime.split(":");
        if(t.length <= 1)
            return;
        
        if(!isNumeric(t[0]) || !isNumeric(t[1]))
            return;
        
        Calendar today = Calendar.getInstance();
        today.setTimeInMillis(System.currentTimeMillis());
        today.set(Calendar.HOUR_OF_DAY, Integer.parseInt(t[0]));
        today.set(Calendar.MINUTE, Integer.parseInt(t[1]));
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        if(today.getTimeInMillis() >= System.currentTimeMillis())
            return;
        long diff = System.currentTimeMillis()-today.getTimeInMillis();
        long ticks = (long)Math.round(240*((100/86400)*(double)diff));
        if(ticks > 6000)
            ticks -= 6000;
        else
            ticks += 18000;
        w.setTime(ticks);
    }
    
    public boolean isNumeric(String str) {
        try {
            int n = Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }
}
