package de.papaharni.amcserver.events;

import de.papaharni.amcserver.AMCServer;
import de.papaharni.pvprewards.Rnd;
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
        if(plugin.getMyConfigConf().protected_entity_use && plugin.getMyConfigConf().protected_entity_types.contains(event.getEntityType()))
        {
            boolean cantAttack = true;
            if(event.getDamager().getType().equals(EntityType.PLAYER))
            {
                Player arr$[] = plugin.getServer().getOnlinePlayers();
                int len$ = arr$.length;
                int i$ = 0;
                do
                {
                    if(i$ >= len$)
                        break;
                    Player player = arr$[i$];
                    if(player.getEntityId() == event.getDamager().getEntityId())
                    {
                        if((player.isOp() || player.hasPermission("pvprew.entity.bypass") || player.hasPermission("pvprew.*")) && player.isSneaking())
                            cantAttack = false;
                        else
                            player.sendMessage(plugin.getMyConfigConf().protected_entity_message.replaceAll("%entity%", event.getEntityType().name()));
                        break;
                    }
                    i$++;
                } while(true);
            }
            if(cantAttack)
            {
                if(plugin.getMyConfigConf().protected_entity_use_lightning)
                {
                    Location loc = event.getDamager().getLocation();
                    if(Rnd.get(1, 99) < plugin.getMyConfigConf().protected_entity_dmg_lightning && event.getDamager().getType().equals(EntityType.PLAYER))
                        plugin.getServer().getWorld(loc.getWorld().getName()).strikeLightning(loc);
                    else
                    if(event.getDamager().getType().equals(EntityType.PLAYER))
                        plugin.getServer().getWorld(loc.getWorld().getName()).strikeLightningEffect(loc);
                }
                event.setCancelled(true);
            }
        }
    }
}
