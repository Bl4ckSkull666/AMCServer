/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.amcserver.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;

/**
 *
 * @author Pappi
 */
public class Region {
    private final String _name;
    private int _minX;
    private int _minY;
    private int _minZ;
    private int _maxX;
    private int _maxY;
    private int _maxZ;
    private String _world;
    
    //Zusatz Optionen
    private boolean _isJumpArena = false;
    private String _tpworld = "";
    private int _tpX = 0;
    private int _tpY = 0;
    private int _tpZ = 0;
    private boolean _tpin = false;
    private String _tpinworld = "";
    private int _tpinX = 0;
    private int _tpinY = 0;
    private int _tpinZ = 0;
    private String _enterMessage = "";
    private String _leaveMessage = "";
    
    public Region(String w, String n, int iX, int iY, int iZ, int aX, int aY, int aZ) {
        _world = w;
        _name = n;
        _minX = iX;
        _minY = iY;
        _minZ = iZ;
        _maxX = aX;
        _maxY = aY;
        _maxZ = aZ;
    }
    
    public World getWorld() {
        return Bukkit.getWorld(_world);
    }
    
    public String getStringWorld() {
        return _world;
    }
    
    public String getName() {
        return _name;
    }
    
    public int getMinX() {
        return _minX;
    }
    
    public int getMinY() {
        return _minY;
    }
    
    public int getMinZ() {
        return _minZ;
    }
    
    public int getMaxX() {
        return _maxX;
    }
    
    public int getMaxY() {
        return _maxY;
    }
    
    public int getMaxZ() {
        return _maxZ;
    }
    
    public Location getPos1() {
        return new Location(Bukkit.getWorld(_world), (double)_minX, (double)_minY, (double)_minZ);
    }
    
    public Location getPos2() {
        return new Location(Bukkit.getWorld(_world), (double)_maxX, (double)_maxY, (double)_maxZ);
    }
    
    public boolean isInside(Location loc) {
        if(loc.getBlockX() >= _minX && loc.getBlockX() <= _maxX && loc.getBlockY() >= _minY && loc.getBlockY() <= _maxY && loc.getBlockZ() >= _minZ && loc.getBlockZ() <= _maxZ)
            return true;
        return false;
    }
            
    public boolean isJumpRegion() {
        return _isJumpArena;
    }
    
    public void setJumpArena(boolean b) {
        _isJumpArena = b;
    }
    
    public boolean isSetTeleport() {
        return !_tpworld.isEmpty();
    }
    
    public Location getTeleport() {
        if(!_tpworld.isEmpty())
            return new Location(Bukkit.getWorld(_tpworld), _tpX, _tpY, _tpZ);
        return null;
    }
    
    public void setTeleport(Location loc) {
        if(loc != null) {
            _tpworld = loc.getWorld().getName();
            _tpX = loc.getBlockX();
            _tpY = loc.getBlockY();
            _tpZ = loc.getBlockZ();
        } else {
            _tpworld = "";
            _tpX = 0;
            _tpY = 0;
            _tpZ = 0;
        }
    }
    
    public boolean isTeleportOnEnter() {
        return _tpin;
    }
    
    public boolean isSetTeleportOnEnter() {
        return !_tpinworld.isEmpty();
    }
    
    public Location getTeleportOnEnter() {
        if(!_tpinworld.isEmpty())
            return new Location(Bukkit.getWorld(_tpinworld), _tpinX, _tpinY, _tpinZ);
        return null;
    }
    
    public void setTeleportOnEnter(Location loc) {
        if(loc != null) {
            _tpinworld = loc.getWorld().getName();
            _tpinX = loc.getBlockX();
            _tpinY = loc.getBlockY();
            _tpinZ = loc.getBlockZ();
        } else {
            _tpinworld = "";
            _tpinX = 0;
            _tpinY = 0;
            _tpinZ = 0;
        }
    }
    
    public void setEnterMessage(String msg) {
        _enterMessage = msg;
    }
    
    public boolean isEnterMessage() {
        return !_enterMessage.isEmpty();
    }
    
    public String getEnterMessage() {
        return ChatColor.translateAlternateColorCodes('&',_enterMessage);
    }
    
    public void setLeaveMessage(String msg) {
        _leaveMessage = msg;
    }
    
    public boolean isLeaveMessage() {
        return !_leaveMessage.isEmpty();
    }
    
    public String getLeaveMessage() {
        return ChatColor.translateAlternateColorCodes('&',_leaveMessage);
    }
    
    public void setLocation(Location l1, Location l2) {
        _minX = Math.min(l1.getBlockX(), l2.getBlockX());
        _maxX = Math.min(l1.getBlockX(), l2.getBlockX());
        _minY = Math.min(l1.getBlockY(), l2.getBlockY());
        _maxY = Math.min(l1.getBlockY(), l2.getBlockY());
        _minZ = Math.min(l1.getBlockZ(), l2.getBlockZ());
        _maxZ = Math.min(l1.getBlockZ(), l2.getBlockZ());
    }
}
