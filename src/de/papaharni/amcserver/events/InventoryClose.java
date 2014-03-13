package de.papaharni.amcserver.events;

import de.papaharni.amcserver.AMCServer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.Inventory;

/**
 *
 * @author Pappi
 */
public class InventoryClose implements Listener {
    
    private final AMCServer _plugin;
    
    public InventoryClose(AMCServer plugin) {
        _plugin = plugin;
    }
    
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event)
    {
        if(event.getPlayer() instanceof Player)
        {
            Player player = (Player)event.getPlayer();
            Inventory inv = event.getInventory();
            if(inv instanceof FurnaceInventory)
            {
                FurnaceInventory finv = (FurnaceInventory)inv;
                if(plugin.isBlock(finv.getHolder().getLocation()))
                {
                    plugin.delBlock(finv.getHolder().getLocation());
                    player.sendMessage((new StringBuilder()).append(plugin.getMyConfigConf().pluginPrefix).append(plugin.getMyConfigConf().furnace_unsaved).toString());
                }
            }
            if(inv instanceof BrewerInventory)
            {
                BrewerInventory binv = (BrewerInventory)inv;
                if(plugin.isBlock(binv.getHolder().getLocation()))
                {
                    plugin.delBlock(binv.getHolder().getLocation());
                    player.sendMessage((new StringBuilder()).append(plugin.getMyConfigConf().pluginPrefix).append(plugin.getMyConfigConf().brewer_unsaved).toString());
                }
            }
        }
    
}
