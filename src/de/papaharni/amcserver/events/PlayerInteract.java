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
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK)
            if(plugin.getMyConfigConf().furnace_use && !plugin.getMyConfigConf().furnace_message.isEmpty() && (block.getType().equals(Material.FURNACE) || block.getType().equals(Material.BURNING_FURNACE)))
            {
                if(plugin.isBlock(block.getLocation()) && (!player.hasPermission("pvprew.protect.bypass") || player.isOp()))
                {
                    player.sendMessage(plugin.getMyConfigConf().furnace_message);
                    event.setCancelled(true);
                }
            } else
            if(plugin.getMyConfigConf().brewer_use && !plugin.getMyConfigConf().brewer_message.isEmpty() && block.getType().equals(Material.BREWING_STAND) && plugin.isBlock(block.getLocation()) && (!player.hasPermission("pvprew.protect.bypass") || player.isOp()))
            {
                player.sendMessage(plugin.getMyConfigConf().brewer_message);
                event.setCancelled(true);
            }
    }
}
