package de.papaharni.amcserver.events;

import de.papaharni.amcserver.AMCServer;
import de.papaharni.amcserver.util.PvPCounters;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 *
 * @author Pappi
 */
public class onLeaveEvent implements Listener {
    
    private final AMCServer _plugin;
    
    public onLeaveEvent(AMCServer plugin) {
        _plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        doYourJob(event.getPlayer().getName());
    }
    
    @EventHandler
    public void onPlayereKick(PlayerKickEvent event) {
        doYourJob(event.getPlayer().getName());
    }
    
    private void doYourJob(String p) {
        _plugin.getPvPCs().savePvPCounter(p);
        _plugin.getJumps().saveArenas(p);
        _plugin.getJumps().remArenas(p);
        _plugin.getPlayerOnlineSince().remove(p);
    }
}
