/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.amcserver;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.Configuration;

/**
 *
 * @author Pappi
 */
public class myConfig {
    
    private final AMCServer _plugin;
    public final boolean debug;
    
    //Protections
    public boolean _protection_use;
    public List<String> _protect_on_world;
    
    //Messages
    
    
    public myConfig(Configuration config, AMCServer plugin)
    {
        _plugin = plugin;
        this.debug = config.getBoolean("debug", false);
        
        //Protections
        _protection_use = config.getBoolean("protect.use", true);
        _protect_on_world = getWorldList(config.getStringList("protect.worlds"));
        //Messages
    }
    
    private List<String> getWorldList(List<String> str_worlds) {
        List<String> wl = new ArrayList<>();
        for(String str: str_worlds) {
            World w = Bukkit.getWorld(str);
            if(w != null) {
                wl.add(str);
            } else {
                AMCServer.debug("Die angegebene Welt " + str + " existiert nicht und wird Ignoriert.");
            }
        }
        return wl;
    }
}
