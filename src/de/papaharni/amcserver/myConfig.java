/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.amcserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.EntityType;

/**
 *
 * @author Pappi
 */
public class myConfig {
    
    private final AMCServer _plugin;
    public final boolean debug;
    public String _joinMessage;
    public String _leaveMessage;
    
    //MySQL - Server
    public HashMap<String, String> _smysql = new HashMap<>();
    
    //MySQL - Forum
    public HashMap<String, String> _fmysql = new HashMap<>();
    
    //MySQL - Homepage
    public HashMap<String, String> _hmysql = new HashMap<>();
    
    //Use MySQL Tables
    public HashMap<String, String> _tmysql = new HashMap<>();
    
    //Scoreboard Options
    public HashMap<String, String> _sbOnWorld = new HashMap<>();
    public HashMap<String, Boolean> _sbAvailable = new HashMap<>();
    public HashMap<String, Boolean> _sbStatistik = new HashMap<>();
    
    //VoteRewards
    public HashMap<String, String> _vrUseable = new HashMap<>();
    
    //Protections
    public boolean _protect_onuse_use;
    public List<String> _protect_onuse_world;
    public boolean _protect_onuse_furnace;
    public String _protect_onuse_furnace_free;
    public String _protect_onuse_furnace_save;
    public String _protect_onuse_furnace_in_use;
    public boolean _protect_onuse_brewer;
    public String _protect_onuse_brewer_free;
    public String _protect_onuse_brewer_save;
    public String _protect_onuse_brewer_in_use;
    
    public boolean _protect_entity_use;
    public List<String> _protect_entity_world;
    public List<EntityType> _protect_entity_list;
    public String _protect_entity_message;
    public boolean _protect_entity_lightning;
    public int _protect_entity_lightning_dmg;
    
    //McJobs
    public boolean _mcjobs_save;
    public long _mcjobs_interval;
    
    //Messages
    
    
    public myConfig(Configuration config, AMCServer plugin) {
        _plugin = plugin;
        this.debug = config.getBoolean("debug", false);
        
        _smysql = getHashMapStr(config, "mysql.server");
        _fmysql = getHashMapStr(config, "mysql.forum");
        _hmysql = getHashMapStr(config, "mysql.homepage");
        _tmysql = getHashMapStr(config, "mysql.tables");
        
        
        _joinMessage = ChatColor.translateAlternateColorCodes('&',config.getString("join", "&2%player% ist zu uns gestossen."));
        _leaveMessage = ChatColor.translateAlternateColorCodes('&',config.getString("leave", "&c%player% hat uns verlassen."));
        
        //ScoreBoard Options
        _sbOnWorld = getHashMapStr(config, "scoreboard.useOnWorld");
        _sbAvailable = getHashMapBol(config, "scoreboard.use");
        _sbStatistik = getHashMapBol(config, "scoreboard.statistik");
        
        //VoteRewards
        _vrUseable = getHashMapStr(config, "votereward.onWorld");
        
        //Protection Furnace/Brewer on use
        _protect_onuse_use = config.getBoolean("protect.onuse.use", false);
        _protect_onuse_world = getWorldList(config.getStringList("protect.onuse.worlds"));
        _protect_onuse_furnace = config.getBoolean("protect.onuse.furnace.use", false);
        _protect_onuse_furnace_free = ChatColor.translateAlternateColorCodes('&',config.getString("protect.onuse.furnace.useable", "&2Der Ofen ist nun wieder Freigegeben."));
        _protect_onuse_furnace_save = ChatColor.translateAlternateColorCodes('&',config.getString("protect.onuse.furnace.protected", "&cDer Ofen ist nun gesichert vor Fremd Benutzung."));
        _protect_onuse_furnace_in_use = ChatColor.translateAlternateColorCodes('&',config.getString("protect.onuse.furnace.inuse", "&cDer Ofen ist bereits in verwendung."));
        _protect_onuse_brewer = config.getBoolean("protect.onuse.brewer.use", false);
        _protect_onuse_brewer_free = ChatColor.translateAlternateColorCodes('&',config.getString("protect.onuse.brewer.useable", "&2Der Braustand ist nun wieder Freigegeben."));
        _protect_onuse_brewer_save = ChatColor.translateAlternateColorCodes('&',config.getString("protect.onuse.brewer.protected", "&cDer Braustand ist nun gesichert vor Fremd Benutzung"));
        _protect_onuse_brewer_in_use = ChatColor.translateAlternateColorCodes('&',config.getString("protect.onuse.furnace.inuse", "&cDer Braustand ist bereits in verwendung."));
        
        //Protect Entitys like Villager and Irongolem
        _protect_entity_use = config.getBoolean("protect.entity.use", false);
        _protect_entity_world = getWorldList(config.getStringList("protect.entity.worlds"));
        _protect_entity_list = getEntityList(config.getStringList("protect.entity.list"));
        _protect_entity_message = ChatColor.translateAlternateColorCodes('&',config.getString("protect.entity.msg", "&cEs ist dir nicht erlaubt %entity% anzugreifen."));
        _protect_entity_lightning = config.getBoolean("protect.entity.lightning", true);
        _protect_entity_lightning_dmg = config.getInt("protect.entity.lightning_dmg_chance", 0);
        
        //Messages
    }
    
    private List<String> getWorldList(List<String> str_worlds) {
        List<String> wl = new ArrayList<>();
        for(String str: str_worlds) {
            World w = Bukkit.getWorld(str);
            if(w != null) {
                wl.add(str);
            } else {
                _plugin.getLog().debug("Die angegebene Welt " + str + " existiert nicht und wird Ignoriert.");
            }
        }
        return wl;
    }
    
    private List<EntityType> getEntityList(List<String> str_list) {
        List<EntityType> etl = new ArrayList<>();
        for(String str: str_list) {
            EntityType et = EntityType.valueOf(str);
            if(et != null) {
                etl.add(et);
            } else {
                _plugin.getLog().debug("Der angegebene EntityType " + str + " existiert nicht und wird Ignoriert.");
            }
        }
        return etl;
    }
    
    private HashMap<String, String> getHashMapStr(Configuration config, String path) {
        HashMap<String, String> hm = new HashMap<>();
        for(String key : config.getConfigurationSection(path).getKeys(false)) {
            hm.put(key.toLowerCase(), config.getString(path + key));
        }
        return hm;
    }
    
    private HashMap<String, Boolean> getHashMapBol(Configuration config, String path) {
        HashMap<String, Boolean> hm = new HashMap<>();
        for(String key : config.getConfigurationSection(path).getKeys(false)) {
            hm.put(key.toLowerCase(), config.getBoolean(path + key, false));
        }
        return hm;
    }
    
    private HashMap<String, Boolean> getHashMapItem(Configuration config, String path) {
        HashMap<String, Boolean> hm = new HashMap<>();
        for(String key : config.getConfigurationSection(path).getKeys(false)) {
            hm.put(key.toLowerCase(), config.getBoolean(path + key, false));
        }
        return hm;
    }
}
