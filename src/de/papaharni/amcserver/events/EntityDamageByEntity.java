package de.papaharni.amcserver.events;

import de.papaharni.amcserver.AMCServer;
import de.papaharni.amcserver.util.Rnd;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 *
 * @author Pappi
 */
public class EntityDamageByEntity implements Listener {
    
    private final AMCServer _plugin;
    
    public EntityDamageByEntity(AMCServer plugin) {
        _plugin = plugin;
    }
    
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
    {
        if(!_plugin.getMyConfig()._protect_entity_use)
            return;
        if(!_plugin.getMyConfig()._protect_entity_list.contains(event.getEntityType()))
            return;
        if(!_plugin.getMyConfig()._protect_entity_world.contains(event.getEntity().getLocation().getWorld().getName()))
            return;
        
        boolean cantAttack = true;
        if(event.getDamager().getType().equals(EntityType.PLAYER)) {
            Player p = null;
            for(Player pl: _plugin.getServer().getOnlinePlayers()) {
                if(pl.getEntityId() == event.getDamager().getEntityId()) {
                    p = pl;
                    break;
                }
            }
            
            if(p == null)
                return;
            
            if((p.isOp() || p.hasPermission("amcserver.pvprew.entity.bypass") && p.isSneaking() || p.hasPermission("amcserver.pvprew.*")) && p.isSneaking())
                return;
            
            p.sendMessage(_plugin.getMyConfig()._protect_entity_message.replaceAll("%entity%", event.getEntityType().name()));
            
            if(_plugin.getMyConfig()._protect_entity_lightning) {
                Location loc = event.getDamager().getLocation();
                if(Rnd.get(1, 99) < _plugin.getMyConfig()._protect_entity_lightning_dmg && event.getDamager().getType().equals(EntityType.PLAYER))
                    _plugin.getServer().getWorld(loc.getWorld().getName()).strikeLightning(loc);
                else {
                    if(event.getDamager().getType().equals(EntityType.PLAYER))
                        _plugin.getServer().getWorld(loc.getWorld().getName()).strikeLightningEffect(loc);
                }
            }
        }
    }
}
