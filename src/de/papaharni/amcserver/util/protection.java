/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.amcserver.util;

import de.papaharni.amcserver.AMCServer;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;

/**
 *
 * @author Pappi
 */
public class protection extends AMCServer {
    
    private AMCServer _plugin;
    private List<Location> saveBlocks = new ArrayList<>();
    
    public protection(AMCServer plugin) {
        _plugin = plugin;
    }
    
    public void addBlock(Location loc) {
        saveBlocks.add(loc);
    }

    public boolean isBlock(Location block) {
        return saveBlocks.contains(block);
    }

    public void delBlock(Location loc) {
        if(saveBlocks.contains(loc))
            saveBlocks.remove(loc);
    }
}
