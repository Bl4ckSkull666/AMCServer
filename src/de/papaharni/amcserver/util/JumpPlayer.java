    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.amcserver.util;

import de.papaharni.amcserver.AMCServer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.entity.Player;

/**
 *
 * @author Pappi
 */
public class JumpPlayer {
    private AMCServer _plugin;
    
    private HashMap<String, List<JumpArena>> _playersList = new HashMap<>();
    
    public JumpPlayer(AMCServer plugin) {
        _plugin = plugin;
    }
    
    public JumpArena getArena(Player p, int arena) {
        JumpArena dummy = new JumpArena(arena, 0,0);
        if(!_playersList.containsKey(p.getName()))
            return dummy;
        
        for(JumpArena ja: _playersList.get(p.getName())) {
            if(ja.getArenaId() == arena)
                return ja;
        }
        return dummy;
    }
    
    public List<JumpArena> getArenas(Player p) {
        List<JumpArena> dummy = new ArrayList<>();
        
        if(!_playersList.containsKey(p.getName()))
            return dummy;
        
        return _playersList.get(p.getName());
    }
    
    public void loadArenas(Player p) {
        _playersList.put(p.getName(), _plugin.getMySQL().getJump().loadArenas(p.getName()));
    }
    
    public void saveArenas(Player p) {
        if(!_playersList.containsKey(p.getName()))
            return;
         _plugin.getMySQL().getJump().saveArenas(p.getName(), _playersList.get(p.getName()));
    }
}