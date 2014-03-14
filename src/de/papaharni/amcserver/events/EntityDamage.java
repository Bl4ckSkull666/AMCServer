package de.papaharni.amcserver.events;

import de.papaharni.amcserver.AMCServer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/**
 *
 * @author Pappi
 */
public class EntityDamage implements Listener {
    
    private final AMCServer _plugin;
    
    public EntityDamage(AMCServer plugin) {
        _plugin = plugin;
    }
    
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event)
    {
        if(_plugin.getMyConfig()._protect_entity_use && _plugin.getMyConfig()._protect_entity_list.contains(event.getEntityType()) && (event.getCause().equals(DamageCause.FIRE) || event.getCause().equals(DamageCause.FIRE_TICK) || event.getCause().equals(DamageCause.LIGHTNING)))
            event.setCancelled(true);
    }
}
