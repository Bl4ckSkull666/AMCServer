/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.amcserver.events;

import de.papaharni.amcserver.AMCServer;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

/**
 *
 * @author Pappi
 */
public class EntityDeath implements Listener {
    
    private final AMCServer _plugin;
    
    public EntityDeath(AMCServer plugin) {
        _plugin = plugin;
    }
    
    @EventHandler
    public void EntityDeath(EntityDeathEvent e) {
        if(e.getEntityType().equals(EntityType.PLAYER) && e.getEntity().getLocation().getWorld().getName() == "PvPWelt") {
            if(e.getEntity().getKiller() != null) {
                if(!_plugin.getEconomy().hasAccount("pvp" + e.getEntity().getKiller().getName()))
                    _plugin.getEconomy().createPlayerAccount("pvp" + e.getEntity().getKiller().getName());
                
                _plugin.getEconomy().depositPlayer("pvp" + e.getEntity().getKiller().getName(), 1.0);
            }
        }
    }
    
}
