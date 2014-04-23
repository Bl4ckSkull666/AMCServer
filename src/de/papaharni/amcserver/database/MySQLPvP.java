/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.amcserver.database;

import de.papaharni.amcserver.AMCServer;
import de.papaharni.amcserver.util.PvPCounter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Pappi
 */
public class MySQLPvP {
    private AMCServer _plugin;
    
    public MySQLPvP(AMCServer plugin) {
        _plugin = plugin;
    }
    
    public int[] getPvPStats(String p) {
        int[] pvp = {0, 0};
        if(!_plugin.getMyConfig()._tmysql.containsKey("pvp"))
            return pvp;
        
        Connection con = null;
        try {
            con = _plugin.getMySQL().getConnect(_plugin.getMyConfig()._tmysql.get("pvp"));
            if(con == null)
                return pvp;
            
            PreparedStatement statement = con.prepareStatement("SELECT `wins`,`lose` FROM `pvp` WHERE `username` = ? LIMIT 0,1");
            statement.setString(1, p);
            ResultSet rset = statement.executeQuery();
            if(rset.next()) {
                pvp[0] = rset.getInt("wins");
                pvp[1] = rset.getInt("lose");
            }
            rset.close();
            statement.close();
        } catch(SQLException e) {
            _plugin.getLog().error("Fehler beim Auslesen der PvPRewards Tabelle für Wins und Losee", e);
            _plugin.getMySQL().close(con);
            return pvp;
        } finally {
            _plugin.getMySQL().close(con);
            return pvp;
        }
    }
    
    public int[] setPvPStats(String p, PvPCounter pc) {
        int[] pvp = {0, 0};
        if(!_plugin.getMyConfig()._tmysql.containsKey("pvp"))
            return pvp;
        
        Connection con = null;
        try {
            con = _plugin.getMySQL().getConnect(_plugin.getMyConfig()._tmysql.get("pvp"));
            if(con == null)
                return pvp;
            
            PreparedStatement statement = con.prepareStatement("INSERT INTO `pvp` SET (`username``wins`,`lose`) VALUES (?,?,?) ON DUPLICATE KEY UPDATE `wins`=?, `lose`=?");
            statement.setString(1, p);
            statement.setInt(2, pc.getWins());
            statement.setInt(3, pc.getLose());
            statement.setInt(4, pc.getWins());
            statement.setInt(5, pc.getLose());
            statement.execute();
            statement.close();
        } catch(SQLException e) {
            _plugin.getLog().error("Fehler beim Auslesen der PvPRewards Tabelle für Wins und Losee", e);
            _plugin.getMySQL().close(con);
            return pvp;
        } finally {
            _plugin.getMySQL().close(con);
            return pvp;
        }
    }
}
