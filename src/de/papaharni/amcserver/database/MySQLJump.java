/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.amcserver.database;

import de.papaharni.amcserver.AMCServer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import org.bukkit.entity.Player;

/**
 *
 * @author Pappi
 */
public class MySQLJump {
    private AMCServer _plugin;
    
    public MySQLJump(AMCServer plugin) {
        _plugin = plugin;
    }
    
    public HashMap<Integer, int[]> getDataFromDB(Player p) {
        HashMap<Integer, int[]> hmap = new HashMap<>();
        if(!_plugin.getMyConfig()._tmysql.containsKey("jumpnrun"))
            return hmap;
        
        Connection con = null;
        try {
            con = _plugin.getMySQL().getConnect(_plugin.getMyConfig()._tmysql.get("mcjobs"));
            if(con == null)
                return hmap;
            
            PreparedStatement statement = con.prepareStatement("SELECT `arenaid`,`wins`,`attempts` FROM `jumpnruns` WHERE `username` = ?");
            statement.setString(1, p.getName());
            ResultSet rset = statement.executeQuery();
            while(rset.next()) {
                int[] jstat = {rset.getInt("attepmts"), rset.getInt("wins")};
                hmap.put(rset.getInt("arenaid"), jstat);
            }
            rset.close();
            statement.close();
        } catch(SQLException e) {
            _plugin.getLog().error("Fehler beim Auslesen der jumpnrun Tabelle", e);
            _plugin.getMySQL().close(con);
        } finally {
            _plugin.getMySQL().close(con);
        }
        return hmap;
    }
}
