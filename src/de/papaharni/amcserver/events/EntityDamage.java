package de.papaharni.amcserver.events;

import de.papaharni.amcserver.AMCServer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

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
        if(plugin.getMyConfigConf().protected_entity_use && plugin.getMyConfigConf().protected_entity_types.contains(event.getEntityType()) && (event.getCause().equals(org.bukkit.event.entity.EntityDamageEvent.DamageCause.FIRE) || event.getCause().equals(org.bukkit.event.entity.EntityDamageEvent.DamageCause.FIRE_TICK) || event.getCause().equals(org.bukkit.event.entity.EntityDamageEvent.DamageCause.LIGHTNING)))
            event.setCancelled(true);
    }
}
