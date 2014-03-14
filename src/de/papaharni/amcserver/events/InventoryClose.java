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
    public void onInventoryClose(InventoryCloseEvent event) {
        if(event.getPlayer() instanceof Player  && _plugin.getMyConfig()._protect_onuse_use) {
            Player player = (Player)event.getPlayer();
            Inventory inv = event.getInventory();
            if(inv instanceof FurnaceInventory) {
                FurnaceInventory finv = (FurnaceInventory)inv;
                if(_plugin.getProtect().isBlock(finv.getHolder().getLocation())) {
                    _plugin.getProtect().delBlock(finv.getHolder().getLocation());
                    player.sendMessage(_plugin.getMyConfig()._protect_onuse_furnace_free);
                }
            }
            if(inv instanceof BrewerInventory) {
                BrewerInventory binv = (BrewerInventory)inv;
                if(_plugin.getProtect().isBlock(binv.getHolder().getLocation())) {
                    _plugin.getProtect().delBlock(binv.getHolder().getLocation());
                    player.sendMessage(_plugin.getMyConfig()._protect_onuse_brewer_free);
                }
            }
        }
    }
    
}
