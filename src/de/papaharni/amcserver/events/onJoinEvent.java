package de.papaharni.amcserver.events;

import de.papaharni.amcserver.AMCServer;
import org.bukkit.ChatColor;
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
        String na = _plugin.getMySQL().isPlayerNotActivatedYet(event.getPlayer());
        if(!na.isEmpty()) {
            event.getPlayer().kickPlayer(ChatColor.translateAlternateColorCodes('&',na));
        }
        
        event.setJoinMessage(_plugin.getMyConfig()._joinMessage.replaceAll("%player%", event.getPlayer().getName()));
        
        //Lade das passende ScoreBoard
        _plugin.getSBMain().setVisible(event.getPlayer());
        _plugin.getSBMain().setScoreboard(event.getPlayer());
    }
}
