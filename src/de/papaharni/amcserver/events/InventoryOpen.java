package de.papaharni.amcserver.events;

import de.papaharni.amcserver.AMCServer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.Inventory;

/**
 *
 * @author Pappi
 */
public class InventoryOpen implements Listener {
    
    private final AMCServer _plugin;
    
    public InventoryOpen(AMCServer plugin) {
        _plugin = plugin;
    }
    
    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event)
    {
        if(event.getPlayer() instanceof Player)
        {
            Player player = (Player)event.getPlayer();
            Inventory inv = event.getInventory();
            if((inv instanceof FurnaceInventory) && (player.hasPermission("pvprew.protect.furnace") || player.hasPermission("pvprew.protect.*") || player.isOp()) && plugin.getMyConfigConf().furnace_use)
            {
                FurnaceInventory finv = (FurnaceInventory)inv;
                if(!_plugin.isBlock(finv.getHolder().getLocation()))
                {
                    _plugin.addBlock(finv.getHolder().getLocation());
                    player.sendMessage(_plugin.getMyConfig()._protect_furnance);
                }
            }
            if((inv instanceof BrewerInventory) && (player.hasPermission("pvprew.protect.brewer") || player.hasPermission("pvprew.protect.*") || player.isOp()) && plugin.getMyConfigConf().brewer_use)
            {
                BrewerInventory binv = (BrewerInventory)inv;
                if(!plugin.isBlock(binv.getHolder().getLocation()))
                {
                    plugin.addBlock(binv.getHolder().getLocation());
                    player.sendMessage((new StringBuilder()).append(plugin.getMyConfigConf().pluginPrefix).append(plugin.getMyConfigConf().brewer_saved).toString());
                }
            }
        }
    }
    
}
