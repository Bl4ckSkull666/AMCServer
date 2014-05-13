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
    
    public JumpArena getArena(String p, String arena) {
        JumpArena dummy = new JumpArena(arena, 0,0);
        if(!_playersList.containsKey(p))
            return dummy;
        
        for(JumpArena ja: _playersList.get(p)) {
            if(ja.getArena() == arena)
                return ja;
        }
        return dummy;
    }
    
    public List<JumpArena> getArenas(String p) {
        List<JumpArena> dummy = new ArrayList<>();
        
        if(!_playersList.containsKey(p))
            return dummy;
        
        return _playersList.get(p);
    }
    
    public void loadArenas(String p) {
        _playersList.put(p, _plugin.getMySQL().getJump().loadArenas(p));
    }
    
    public void saveArenas(String p) {
        if(!_playersList.containsKey(p))
            return;
         _plugin.getMySQL().getJump().saveArenas(p, _playersList.get(p));
    }
    
    public void remArenas(String p) {
        if(!_playersList.containsKey(p))
            return;
        _playersList.remove(p);
    }
}