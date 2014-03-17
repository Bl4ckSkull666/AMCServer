/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.amcserver.util;

import java.util.HashMap;

/**
 *
 * @author Pappi
 */
public class JumpArena {
    
    private int _arenaid;
    private int _played;
    private int _wins;
    
    public JumpArena(int arena, int played, int wins) {
        _arenaid = arena;
        _played = played;
        _wins = wins;
    }
    
    public int getArenaId() {
        return _arenaid;
    }
    
    public int getPlayed() {
        return _played;
    }
    
    public int getWins() {
        return _wins;
    }
    
    public void setArenaId(int arenaid) {
        _arenaid = arenaid;
    }
    
    public void setPlayed(int played) {
        _played = played;
    }
    
    public void setWins(int wins) {
        _wins = wins;
    }
    
    public void addPlayed(int played) {
        _played+=played;
    }
    
    public void addWins(int wins) {
        _wins+=wins;
    }
}
