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
import org.bukkit.entity.Player;

/**
 *
 * @author Pappi
 */
public class MySQLStatistik {
    private AMCServer _plugin;
    
    public MySQLStatistik(AMCServer plugin) {
        _plugin = plugin;
    }
    
    public int[] getFromBB(Player p) {
        int[] vspf = {0, 0, 0, 0};
        if(!_plugin.getMyConfig()._tmysql.containsKey("bb1_users"))
            return vspf;
        
        Connection con = null;
        try {
            con = _plugin.getMySQL().getConnect(_plugin.getMyConfig()._tmysql.get("mcjobs"));
            if(con == null)
                return vspf;
            
            PreparedStatement statement = con.prepareStatement("SELECT `guthaben`,`sparbuch`,`vote_points`,`payClicks` FROM `bb1_users` WHERE `username` = ? LIMIT 0,1");
            statement.setString(1, p.getName());
            ResultSet rset = statement.executeQuery();
            if(rset.next()) {
                vspf[0] = rset.getInt("guthaben");
                vspf[1] = rset.getInt("sparbuch");
                vspf[2] = rset.getInt("vote_points");
                vspf[3] = rset.getInt("payClicks");
            }
            rset.close();
            statement.close();
        } catch(SQLException e) {
            _plugin.getLog().error("Fehler beim Auslesen der bb1_users Tabelle für Foins,Vote-Punkte und SP-Punkte", e);
            _plugin.getMySQL().close(con);
            return vspf;
        } finally {
            _plugin.getMySQL().close(con);
            return vspf;
        }
    }
}
