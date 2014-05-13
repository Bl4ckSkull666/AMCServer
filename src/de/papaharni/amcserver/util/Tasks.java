/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.amcserver.util;

import de.papaharni.amcserver.AMCServer;
import java.util.Calendar;
import java.util.Map;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

/**
 *
 * @author Pappi
 */
public final class Tasks {
    private final AMCServer _plugin;
    private BukkitTask _mcjobs;
    private BukkitTask _scoreboard;
    private BukkitTask _worldTimer;
    
    public Tasks(AMCServer plugin) {
        _plugin = plugin;
        _mcjobs = startMcJobsUpdater();
        if(_mcjobs == null)
            _plugin.getLogger().log(Level.INFO, "MCJobs Task hat einen Fehler und konnte nicht gestartet werde.");
        _scoreboard = startScoreboardUpdater();
        if(_scoreboard == null)
            _plugin.getLogger().log(Level.INFO, "Scoreboard Task hat einen Fehler und konnte nicht gestartet werde.");
        _worldTimer = startWorldTimeUpdater();
        if(_worldTimer == null)
            _plugin.getLogger().log(Level.INFO, "WeltTimer Task hat einen Fehler und konnte nicht gestartet werde.");
    }
    
    public void cancelTask(String taskname) {
        switch(taskname) {
            case "mcjobs":
                _mcjobs.cancel();
                break;
            case "scoreboard":
                _scoreboard.cancel();
                break;
            case "worldtimer":
                _worldTimer.cancel();
                break;
            default:
                _mcjobs.cancel();
                _scoreboard.cancel();
                _worldTimer.cancel();
                break;
        }
    }
    
    public BukkitTask startScoreboardUpdater() {
        //return Bukkit.getServer().getScheduler().runTaskTimer(_plugin, new updateScoreboard(), (20*20),(_plugin.getMyConfig()._mcjobs_interval*20));
        return Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(_plugin, new updateScoreboard(), (20*20));
    }
    
    public class updateScoreboard implements Runnable {
        public void run() {
            try {
                for(Player p: _plugin.getServer().getOnlinePlayers()) {
                    if(_plugin.getSBMain().getStatus(p))
                        _plugin.getSBMain().setScoreboard(p, p.getLocation());
                }
            } catch(Exception e) {
                _plugin.getLog().error("Fehler im Scoreboard Update Task", e);
            }
            if(_scoreboard != null)
                _scoreboard.cancel();
            _scoreboard = startScoreboardUpdater();
        }
    }
    
    public BukkitTask startMcJobsUpdater() {
        //return Bukkit.getServer().getScheduler().runTaskTimer(_plugin, new updateMcJobs(), (20*20), (_plugin.getMyConfig()._mcjobs_interval*20));
        return Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(_plugin, new updateMcJobs(), (_plugin.getMyConfig()._mcjobs_interval*20));
    }
    
    public class updateMcJobs implements Runnable {
        public void run() {
            try {
                for(Player p: _plugin.getServer().getOnlinePlayers()) {
                    _plugin.getMySQL().setMcJobsByPlayer(p);
                }
            } catch(Exception e) {
                _plugin.getLog().error("Fehler im Jobs Update Task", e);
            }
            if(_mcjobs != null)
                _mcjobs.cancel();
            _mcjobs = startMcJobsUpdater();
        }
    }
    
    public BukkitTask startWorldTimeUpdater() {
        //return Bukkit.getServer().getScheduler().runTaskTimer(_plugin, new updateWorldTime(), (20*20), (_plugin.getMyConfig()._wTimes_interval*20));
        return Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(_plugin, new updateWorldTime(), (_plugin.getMyConfig()._wTimes_interval*20));
    }
    
    public class updateWorldTime implements Runnable {
        public void run() {
            try {
                for (Map.Entry<String, String> e : _plugin.getMyConfig()._wTimes.entrySet()) {
                    if(Bukkit.getWorld(e.getKey()) != null) {
                        World w = Bukkit.getWorld(e.getKey());
                        if(w == null)
                            break;
                        String[] sa = e.getValue().split("\\;");
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
            } catch(Exception e) {
                _plugin.getLog().error("Fehler im Welt Update Task", e);
            }
            if(_worldTimer != null)
                _worldTimer.cancel();
            _worldTimer = startWorldTimeUpdater();
        }
    }
    
    private void setWorldToCurrentTime(World w) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        setWorldTime(w, cal);
    }
    
    private void setWorldToTime(World w, String strTime) {
        String[] t = strTime.split(":");
        if(t.length <= 1)
            return;
        
        if(!isNumeric(t[0]) || !isNumeric(t[1]))
            return;
        
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(t[0]));
        cal.set(Calendar.MINUTE, Integer.parseInt(t[1]));
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        setWorldTime(w, cal);
    }
    
    private void setWorldTime(World w, Calendar cal) {
        long timeseconds = cal.get(Calendar.SECOND)+(60*cal.get(Calendar.MINUTE))+(60*60*cal.get(Calendar.HOUR_OF_DAY));
        float pc = ((100F/86400F)*(float)timeseconds);
        int ticks = (int)((24000/100)*pc);
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
