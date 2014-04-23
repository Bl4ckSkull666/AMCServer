package de.papaharni.amcserver.events;

import de.papaharni.amcserver.AMCServer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 *
 * @author Pappi
 */
public class onJoinEvent implements Listener {
    private final AMCServer _plugin;
    
    public onJoinEvent(AMCServer plugin) {
        _plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        //Lade das passende ScoreBoard
        _plugin.getSBMain().setVisible(event.getPlayer());
        _plugin.getSBMain().setScoreboard(event.getPlayer(), event.getPlayer().getLocation());
        _plugin.getPvPCs().loadPvPCounter(event.getPlayer().getName());
        _plugin.getPlayerOnlineSince().put(event.getPlayer().getName(), System.currentTimeMillis());
    }
}
