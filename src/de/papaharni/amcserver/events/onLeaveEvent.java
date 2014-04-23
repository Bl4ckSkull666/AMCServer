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
        _plugin.getPvPCs().savePvPCounter(event.getPlayer().getName());        
    }
    
    @EventHandler
    public void onPlayereKick(PlayerKickEvent event) {
        _plugin.getPvPCs().savePvPCounter(event.getPlayer().getName());
    }
}
