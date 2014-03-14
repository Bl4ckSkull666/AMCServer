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
    public void onInventoryOpen(InventoryOpenEvent event) {
        if(event.getPlayer() instanceof Player && _plugin.getMyConfig()._protect_onuse_use) {
            Player player = (Player)event.getPlayer();
            Inventory inv = event.getInventory();
            if((inv instanceof FurnaceInventory) && (player.hasPermission("pvprew.protect.furnace") || player.hasPermission("pvprew.protect.*") || player.isOp()) && _plugin.getMyConfig()._protect_onuse_furnace) {
                FurnaceInventory finv = (FurnaceInventory)inv;
                if(!_plugin.getProtect().isBlock(finv.getHolder().getLocation()) && _plugin.getMyConfig()._protect_onuse_world.contains(finv.getHolder().getLocation().getWorld().getName())) {
                    _plugin.getProtect().addBlock(finv.getHolder().getLocation());
                    player.sendMessage(_plugin.getMyConfig()._protect_onuse_furnace_save);
                }
            }
            if((inv instanceof BrewerInventory) && (player.hasPermission("pvprew.protect.brewer") || player.hasPermission("pvprew.protect.*") || player.isOp()) && _plugin.getMyConfig()._protect_onuse_brewer) {
                BrewerInventory binv = (BrewerInventory)inv;
                if(!_plugin.getProtect().isBlock(binv.getHolder().getLocation()) && _plugin.getMyConfig()._protect_onuse_world.contains(binv.getHolder().getLocation().getWorld().getName())) {
                    _plugin.getProtect().addBlock(binv.getHolder().getLocation());
                    player.sendMessage(_plugin.getMyConfig()._protect_onuse_brewer_save);
                }
            }
        }
    }
}
