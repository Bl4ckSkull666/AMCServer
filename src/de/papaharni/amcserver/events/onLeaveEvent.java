package de.papaharni.amcserver.events;

import de.papaharni.amcserver.AMCServer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
        event.setQuitMessage(event.getPlayer().getName() + " hat den Server verlassen.");
    }
}
