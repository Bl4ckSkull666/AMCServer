package de.papaharni.amcserver;

import com.dmgkz.mcjobs.McJobs;
import de.papaharni.amcserver.events.*;
import de.papaharni.amcserver.commands.*;
import de.papaharni.amcserver.database.MySQLMain;
import de.papaharni.amcserver.scoreboards.SBMain;
import de.papaharni.amcserver.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
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
    
    public static Economy economy = null;
    public static boolean _isMcJobs = false;
    public static boolean _isPwnFilter = false;
    
    private List<Location> saveBlocks = new ArrayList<>();
    private MySQLMain _mysql;
    private SBMain _sb;
    private HashMap<String, Long> _playerOnlineSince = new HashMap<>();
    
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
        
        checkExternPlugins();
                
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
        
        _mysql = new MySQLMain(this);
        _sb = new SBMain(this);
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
    
    public Economy getEconomy()
    {
        return economy;
    }
    
    private boolean setupEconomy()
    {
        RegisteredServiceProvider economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if(economyProvider != null)
            economy = (Economy)economyProvider.getProvider();
        return economy != null;
    }
    
    private void checkExternPlugins() {
        setupEconomy();
        
        if(getServer().getPluginManager().isPluginEnabled("mcjobs")) {
            _isMcJobs = true;
            if(getMyConfig()._mcjobs_save)
                getMySQL().setupMcJobsStructure();
        }
        if(!_isMcJobs)
            getLog().error("McJobs wurde in der Statistik und Datenbank deaktiviert.");

        if(getServer().getPluginManager().isPluginEnabled("PwnFilter"))
            _isPwnFilter = true;
        else
            getLog().error("PwnFilter wurde fuer die Statistik deaktiviert.");
    }
    
    public boolean isMcJobs() {
        return _isMcJobs;
    }
    
    public boolean isPwnFilter() {
        return _isPwnFilter;
    }
    
    public MySQLMain getMySQL() {
        return _mysql;
    }
    
    public SBMain getSBMain() {
        return _sb;
    }
    
    public HashMap<String ,Long> getPlayerOnlineSince() {
        return _playerOnlineSince;
    }
}
