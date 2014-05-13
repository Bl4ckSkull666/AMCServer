/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.amcserver.database;

import de.papaharni.amcserver.AMCServer;
import de.papaharni.amcserver.util.JumpArena;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author Pappi
 */
public class MySQLJump {
    private AMCServer _plugin;
    
    public MySQLJump(AMCServer plugin) {
        _plugin = plugin;
    }
    
    public List<JumpArena> loadArenas(String p) {
        List<JumpArena> jal = new ArrayList<>();
        
        Connection con = null;
        try {
            con = _plugin.getMySQL().getConnect(_plugin.getMyConfig()._tmysql.get("jumpnrun"));
            if(con == null)
                return jal;
            
            PreparedStatement statement = con.prepareStatement("SELECT `arena`,`played`,`wins` FROM `jumpnruns` WHERE `username` = ?");
            statement.setString(1, p);
            ResultSet rset = statement.executeQuery();
            while(rset.next()) {
                AMCServer.getInstance().getLogger().log(Level.INFO, p + " : Lade " + rset.getString("arena") + " mit " + rset.getInt("played") + " gespielten und " + rset.getInt("wins") + " gewonnen runden.");
                JumpArena ja = new JumpArena(rset.getString("arena"), rset.getInt("played"), rset.getInt("wins"));
                jal.add(ja);
            }
            rset.close();
            statement.close();
            
        } catch(SQLException e) {
            _plugin.getLog().error("Fehler beim Auslesen der jumpnrun Tabelle", e);
            _plugin.getMySQL().close(con);
        } finally {
            _plugin.getMySQL().close(con);
        }
        return jal;
    }
    
    public void saveArenas(String p, List<JumpArena> jal) {
        if(jal.isEmpty())
            return;
        
        if(!_plugin.getMyConfig()._tmysql.containsKey("jumpnrun"))
            return;
        
        Connection con = null;
        try {
            con = _plugin.getMySQL().getConnect(_plugin.getMyConfig()._tmysql.get("jumpnrun"));
            if(con == null)
                return;
            
            PreparedStatement statement = con.prepareStatement("INSERT INTO `jumpnruns` (`username`,`arena`,`played`,`wins`) VALUES (?,?,?,?) ON DUPLICATE KEY UPDATE `played`=?,`wins`=?");
            for(JumpArena ja: jal) {
                statement.setString(1, p);
                statement.setString(2, ja.getArena());
                statement.setInt(3, ja.getPlayed());
                statement.setInt(4, ja.getWins());
                statement.setInt(5, ja.getPlayed());
                statement.setInt(6, ja.getWins());
                statement.execute();
            }
            statement.close();
        } catch(SQLException e) {
            _plugin.getLog().error("Fehler beim Speichern der JumpnRun Spielerstände von " + p, e);
            _plugin.getMySQL().close(con);
        } finally {
            _plugin.getMySQL().close(con);
        }
    }
}