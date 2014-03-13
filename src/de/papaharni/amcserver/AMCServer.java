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
    private static Logger _log;
    
    private List<Location> saveBlocks = new ArrayList<>();
    private Map<String, PvPCounter> pvpcounts = new HashMap<>();
    
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
    
    public static void log(String msg)
    {
        _log.log(Level.INFO, msg);
    }

    public static void error(String msg)
    {
        _log.log(Level.SEVERE, msg);
    }

    public static void error(String msg, Throwable t)
    {
        _log.log(Level.SEVERE, msg, t);
    }

    public static void debug(String msg)
    {
        if(_debugMode)
            _log.log(Level.WARNING,"[debug] " + msg);
    }
    
    public void addPvPWin(String uname)
    {
        PvPCounter yours = getPvPCounter(uname);
        yours.addWin();
        setPvPCounter(uname, yours);
    }

    public void delPvPWin(String uname)
    {
        PvPCounter yours = getPvPCounter(uname);
        yours.setWins(yours.getWins() - 1);
        setPvPCounter(uname, yours);
    }

    public void setPvPWins(String uname, int winTotal)
    {
        PvPCounter yours = getPvPCounter(uname);
        yours.addWins(winTotal);
        setPvPCounter(uname, yours);
    }

    public void addPvPLose(String uname)
    {
        PvPCounter yours = getPvPCounter(uname);
        yours.addLose();
        setPvPCounter(uname, yours);
    }

    public void delPvPLose(String uname)
    {
        PvPCounter yours = getPvPCounter(uname);
        yours.setLose(yours.getLose() - 1);
        setPvPCounter(uname, yours);
    }

    public void setPvPLoses(String uname, int winTotal)
    {
        PvPCounter yours = getPvPCounter(uname);
        yours.addLoses(winTotal);
        setPvPCounter(uname, yours);
    }
    
    public boolean isPvPCounter(String uname)
    {
        return pvpcounts.containsKey(uname);
    }

    public PvPCounter getPvPCounter(String uname)
    {
        if(isPvPCounter(uname))
            return pvpcounts.get(uname);
        else
            return null;
    }

    public void setPvPCounter(String uname, PvPCounter myCount)
    {
        pvpcounts.put(uname, myCount);
    }

    public void savePvPCounter()
    {
        java.util.Map.Entry e;
        for(Iterator it = pvpcounts.entrySet().iterator(); it.hasNext(); database.savePlayerCount((String)e.getKey(), ((PvPCounter)e.getValue()).getWins(), ((PvPCounter)e.getValue()).getLose()))
            e = (java.util.Map.Entry)it.next();

    }

    public void addBlock(Location loc)
    {
        saveBlocks.add(loc);
    }

    public boolean isBlock(Location block)
    {
        return saveBlocks.contains(block);
    }

    public void delBlock(Location loc)
    {
        if(saveBlocks.contains(loc))
            saveBlocks.remove(loc);
    }
}
