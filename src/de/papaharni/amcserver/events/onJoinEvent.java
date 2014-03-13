package de.papaharni.amcserver.events;

import de.papaharni.amcserver.AMCServer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
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
        event.setJoinMessage(event.getPlayer().getName() + " ist dem Server beigetreten.");
    }
    
    @EventHandler
    public void onInvOpen(InventoryOpenEvent event) {
        
    }
}
