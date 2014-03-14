package de.papaharni.amcserver;

import de.papaharni.amcserver.events.*;
import de.papaharni.amcserver.commands.*;
import de.papaharni.amcserver.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Pappi
 */
public class AMCServer extends JavaPlugin {
    
    private static AMCServer _instance;
    private static boolean _debugMode;
    private static myConfig _config;
    private protection _protect;
    private logging _logger;
    
    private List<Location> saveBlocks = new ArrayList<>();
    
    public static AMCServer getInstance() {
        return _instance;
    }
     
    @Override
    public void onEnable() {
        _instance = this;
        
        Configuration configuration = this.getConfig();
        if(!this.getDataFolder().exists())
        {
            this.getDataFolder().mkdir();
            configuration.options().copyDefaults(true);
        }
        
        _config = new myConfig(configuration, this);
        this.saveConfig();
        _debugMode = _config.debug;
        
        _protect = new protection(this);
        _logger = new logging(this, _config.debug);
        
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new onJoinEvent(this), this);
        pm.registerEvents(new onLeaveEvent(this), this);
        pm.registerEvents(new InventoryOpen(this), this);
        pm.registerEvents(new InventoryClose(this), this);
        pm.registerEvents(new EntityDamage(this), this);
        pm.registerEvents(new EntityDamageByEntity(this), this);
        
        this.getCommand("tpplayer").setExecutor(new tpplayer());
        this.getCommand("run").setExecutor(new run());
        this.getCommand("nachtsicht").setExecutor(new nachtsicht(this));
        this.getCommand("unsichtbar").setExecutor(new unsichtbar(this));
    }
    
    @Override
    public void onDisable() {
        
    }
    
    public myConfig getMyConfig() {
        return _config;
    }
    
    public protection getProtect() {
        return _protect;
    }
    
    public logging getLog() {
        return _logger;
    }
}
