/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.amcserver.events;

import de.papaharni.amcserver.AMCServer;
import de.papaharni.amcserver.util.Region;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 *
 * @author Pappi
 */
public class PlayerMove implements Listener {
    private final AMCServer _plugin;
    
    public PlayerMove(AMCServer plugin) {
        _plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if(!event.getFrom().getWorld().getName().equalsIgnoreCase(event.getTo().getWorld().getName()))
            _plugin.getSBMain().setScoreboard(event.getPlayer(), event.getTo());
        
        if(_plugin.getRegions().getRegionListByWorld(event.getFrom().getWorld().getName()).isEmpty())
            return;
        /*
        for(Region r: _plugin.getRegions().getRegionListByWorld(event.getFrom().getWorld().getName())) {
            if(r.isInside(event.getFrom()) && !r.isInside(event.getTo())) {
                //Verlasse Bereich
                if(r.isJumpRegion())
                    _plugin.getJumps().getArena(event.getPlayer(), r.getName()).addPlayed(1);
                if(r.isLeaveMessage())
                    event.getPlayer().sendMessage(r.getLeaveMessage());
                if(r.isSetTeleport() && !r.isTeleportOnEnter())
                    event.getPlayer().teleport(r.getTeleport());
            } else if(!r.isInside(event.getFrom()) && r.isInside(event.getTo())) {
                //Betrete Bereich
                if(r.isEnterMessage())
                    event.getPlayer().sendMessage(r.getEnterMessage());
                if(r.isSetTeleport() && r.isTeleportOnEnter())
                    event.getPlayer().teleport(r.getTeleport());
            }
        }
        */
    }
}
