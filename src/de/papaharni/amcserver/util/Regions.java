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
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 *
 * @author Pappi
 */
public class Regions {
    private AMCServer _plugin;
    
    private List<Region> _regions = new ArrayList<>();
    private HashMap<String, Region> _playerIn = new HashMap<>();
    
    public Regions(AMCServer plugin) {
        _plugin = plugin;
    }
    
    public List<Region> getRegionListByWorld(String world) {
        List<Region> rl = new ArrayList<>();
        for(Region r: _regions) {
            if(r.getStringWorld().equalsIgnoreCase(world)) {
                rl.add(r);
            }
        }
        return rl;
    }
    
    public Region getRegionByName(String world, String name) {
        for(Region r: getRegionListByWorld(world)) {
            if(r.getName().equalsIgnoreCase(name))
                return r;
        }
        return null;
    }
    
    public Location getRegionLocation(String art, String world, String name) {
        for(Region r: getRegionListByWorld(world)) {
            if(r.getName().equalsIgnoreCase(name)) {
                switch(art) {
                    case "pos1":
                        return r.getPos1();
                    case "pos2":
                        return r.getPos2();
                    case "middle":
                        int mx = (int)Math.floor((double)((r.getMaxX()-r.getMinX())/2));
                        int mz = (int)Math.floor((double)((r.getMaxZ()-r.getMinZ())/2));
                        int my = getHighestY(world, mx, mz, r.getMinY(), r.getMaxY());
                        return new Location(Bukkit.getWorld(world), mx, my, mz);
                    case "top":
                        int tx = (int)Math.floor((double)((r.getMaxX()-r.getMinX())/2));
                        int tz = (int)Math.floor((double)((r.getMaxZ()-r.getMinZ())/2));
                        return new Location(Bukkit.getWorld(world), tx, r.getMaxY(), tz);
                    case "bottom":
                        int bx = (int)Math.floor((double)((r.getMaxX()-r.getMinX())/2));
                        int bz = (int)Math.floor((double)((r.getMaxZ()-r.getMinZ())/2));
                        return new Location(Bukkit.getWorld(world), bx, r.getMinY(), bz);
                    default:
                        int dx = (int)Math.floor((double)((r.getMaxX()-r.getMinX())/2));
                        int dz = (int)Math.floor((double)((r.getMaxZ()-r.getMinZ())/2));
                        int dy = getHighestY(world, dx, dz, r.getMinY(), r.getMaxY());
                        return new Location(Bukkit.getWorld(world), dx, dy, dz);
                }
            }
        }
        return null;
    }
    
    private int getHighestY(String wo, int x, int z, int miny, int maxy) {
        World w = Bukkit.getWorld(wo);
        for(int i = miny; i <= maxy; i++) {
            if(w.getBlockAt(x, i, z).isEmpty() && w.getBlockAt(x, i+1, z).isEmpty()) {
                return i;
            }
        }
        return (int)Math.floor(maxy-miny);
    }
    
    public boolean isPlayerInARegion(String p) {
        return _playerIn.containsKey(p);
    }
    
    public Region PlayerInRegion(String p) {
        if(isPlayerInARegion(p))
            return _playerIn.get(p);
        return null;
    }
    
    public void setPlayerInRegion(String p, Region r) {
        _playerIn.put(p, r);
    }
}
