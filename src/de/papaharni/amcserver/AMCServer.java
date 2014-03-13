package de.papaharni.amcserver;

import de.papaharni.amcserver.events.*;
import de.papaharni.amcserver.commands.*;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Pappi
 */
public class AMCServer extends JavaPlugin {
    
    public AMCServer getInstance() {
        return this;
    }
     
    @Override
    public void onEnable() {

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new onJoinEvent(this), this);
        pm.registerEvents(new onLeaveEvent(this), this);
        
        this.getCommand("tpplayer").setExecutor(new tpplayer());
        this.getCommand("run").setExecutor(new run());
        this.getCommand("nachtsicht").setExecutor(new nachtsicht(this));
        this.getCommand("unsichtbar").setExecutor(new unsichtbar(this));
    }
    
    @Override
    public void onDisable() {
        
    }
}
