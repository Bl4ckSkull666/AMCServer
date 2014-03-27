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
    
    private String _arena;
    private int _played;
    private int _wins;
    
    public JumpArena(String arena, int played, int wins) {
        _arena = arena;
        _played = played;
        _wins = wins;
    }
    
    public String getArena() {
        return _arena;
    }
    
    public int getPlayed() {
        return _played;
    }
    
    public int getWins() {
        return _wins;
    }
    
    public void setArena(String arena) {
        _arena = arena;
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
