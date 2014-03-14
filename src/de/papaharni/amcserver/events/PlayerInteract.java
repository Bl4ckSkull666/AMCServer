package de.papaharni.amcserver.events;

import de.papaharni.amcserver.AMCServer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * @author Pappi
 */
public class PlayerInteract implements Listener {
    
    private final AMCServer _plugin;
    
    public PlayerInteract(AMCServer plugin) {
        _plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if(block == null || player == null)
            return;
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(!_plugin.getMyConfig()._protect_onuse_furnace && !_plugin.getMyConfig()._protect_onuse_brewer)
                return;
            
            if(!_plugin.getProtect().isBlock(block.getLocation()))
                return;
            
            if(!player.hasPermission("pvprew.protect.bypass") || !player.isOp()) {
                if(block.getType().equals(Material.FURNACE) || block.getType().equals(Material.BURNING_FURNACE))
                    player.sendMessage(_plugin.getMyConfig()._protect_onuse_furnace_in_use);
                if(block.getType().equals(Material.BREWING_STAND))
                    player.sendMessage(_plugin.getMyConfig()._protect_onuse_brewer_in_use);
                event.setCancelled(true);
            }
        }
    }
}
