/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.amcserver.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 *
 * @author Pappi
 */
public class Region {
    private final String _world;
    private final String _name;
    private final int _minX;
    private final int _minY;
    private final int _minZ;
    private final int _maxX;
    private final int _maxY;
    private final int _maxZ;
    
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
}
