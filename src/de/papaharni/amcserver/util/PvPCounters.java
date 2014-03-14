/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.amcserver.util;

import de.papaharni.amcserver.AMCServer;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Pappi
 */
public class PvPCounters {
    
    private AMCServer _plugin;
    private Map<String, PvPCounter> pvpcounts = new HashMap<>();
    
    public PvPCounters(AMCServer plugin) {
        _plugin = plugin;
    }
    
    public void addPvPWin(String uname) {
        PvPCounter yours = getPvPCounter(uname);
        yours.addWin();
        setPvPCounter(uname, yours);
    }

    public void delPvPWin(String uname) {
        PvPCounter yours = getPvPCounter(uname);
        yours.setWins(yours.getWins() - 1);
        setPvPCounter(uname, yours);
    }

    public void setPvPWins(String uname, int winTotal) {
        PvPCounter yours = getPvPCounter(uname);
        yours.addWins(winTotal);
        setPvPCounter(uname, yours);
    }

    public void addPvPLose(String uname) {
        PvPCounter yours = getPvPCounter(uname);
        yours.addLose();
        setPvPCounter(uname, yours);
    }

    public void delPvPLose(String uname) {
        PvPCounter yours = getPvPCounter(uname);
        yours.setLose(yours.getLose() - 1);
        setPvPCounter(uname, yours);
    }

    public void setPvPLoses(String uname, int winTotal) {
        PvPCounter yours = getPvPCounter(uname);
        yours.addLoses(winTotal);
        setPvPCounter(uname, yours);
    }
    
    public boolean isPvPCounter(String uname) {
        return pvpcounts.containsKey(uname);
    }

    public PvPCounter getPvPCounter(String uname) {
        if(isPvPCounter(uname))
            return pvpcounts.get(uname);
        else
            return null;
    }

    public void setPvPCounter(String uname, PvPCounter myCount) {
        pvpcounts.put(uname, myCount);
    }

    public void savePvPCounter()
    {
        for(Map.Entry e: pvpcounts.entrySet()) {
            _plugin.getMySQL().PvP().savePlayerCount((String)e.getKey(), ((PvPCounter)e.getValue()).getWins(), ((PvPCounter)e.getValue()).getLose());
        }
    }
}
