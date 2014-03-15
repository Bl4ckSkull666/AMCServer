/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.amcserver.util;

import de.papaharni.amcserver.AMCServer;
import org.bukkit.Bukkit;
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
}
