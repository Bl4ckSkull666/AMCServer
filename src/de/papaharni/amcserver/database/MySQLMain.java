/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.amcserver.database;

import com.dmgkz.mcjobs.playerdata.PlayerCache;
import com.dmgkz.mcjobs.playerjobs.levels.Leveler;
import de.papaharni.amcserver.AMCServer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.bukkit.entity.Player;

/**
 *
 * @author Pappi
 */
public class MySQLMain {
    
    public AMCServer _plugin;
    private final MySQLJump _jump;
    private final MySQLPvP _pvp;
    private final MySQLStatistik _stats;
    
    public MySQLMain(AMCServer plugin)
    {
        _plugin = plugin;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch(ClassNotFoundException t) {
            _plugin.getLog().error("Konnte den MySQL Treiber nicht finden! Beende Plugin." + t);
            _plugin.getServer().getPluginManager().disablePlugin(_plugin);
        }
        if(!getTestConnect()) {
            _plugin.getLog().error("Fehler bei den Test Verbindungen zum MySQL Server! Beende Plugin.");
            _plugin.getServer().getPluginManager().disablePlugin(_plugin);
        }
        
        setupMcJobsStructure();
        setupJumpnRunStructure();
        setupPvPStructure();
        
        _jump = new MySQLJump(_plugin);
        _pvp = new MySQLPvP(_plugin);
        _stats = new MySQLStatistik(_plugin);
    }

    private boolean getTestConnect()
    {
        Connection con = null;
        try
        {
            if(_plugin.getMyConfig()._smysql.containsKey("use")?Boolean.getBoolean(_plugin.getMyConfig()._smysql.get("use")):false) {
                con = getConnect("server");
                if(con == null)
                    _plugin.getLog().error("Konnte keine Verbindung zur Server Datenbank aufbauen.");
                close(con);
            }
            
            if(_plugin.getMyConfig()._fmysql.containsKey("use")?Boolean.getBoolean(_plugin.getMyConfig()._fmysql.get("use")):false) {
                con = getConnect("forum");
                if(con == null)
                    _plugin.getLog().error("Konnte keine Verbindung zur Forum Datenbank aufbauen.");
                close(con);
            }
            
            if(_plugin.getMyConfig()._hmysql.containsKey("use")?Boolean.getBoolean(_plugin.getMyConfig()._hmysql.get("use")):false) {
                con = getConnect("homepage");
                if(con == null)
                    _plugin.getLog().error("Konnte keine Verbindung zur Homepage Datenbank aufbauen.");
                close(con);
            }
            
        } catch(Exception e) {
            _plugin.getLog().error("Fehler in der Konfiguration der Datenbank!", e);
            return false;
        }
        return true;
    }

    public Connection getConnect(String t)
    {
        Connection con = null;
        try {
            switch(t) {
                case "server":
                    if(_plugin.getMyConfig()._smysql.containsKey("use")?Boolean.getBoolean(_plugin.getMyConfig()._smysql.get("use")):false) {
                        con = DriverManager.getConnection("jdbc:mysql://" + _plugin.getMyConfig()._smysql.get("host")
                            + ":" + _plugin.getMyConfig()._smysql.get("port")
                            + "/" + _plugin.getMyConfig()._smysql.get("data"),
                            _plugin.getMyConfig()._smysql.get("user"),
                            _plugin.getMyConfig()._smysql.get("pass")
                        );
                    }
                    break;
                case "forum":
                    if(_plugin.getMyConfig()._fmysql.containsKey("use")?Boolean.getBoolean(_plugin.getMyConfig()._fmysql.get("use")):false) {
                        con = DriverManager.getConnection("jdbc:mysql://" + _plugin.getMyConfig()._fmysql.get("host")
                            + ":" + _plugin.getMyConfig()._fmysql.get("port")
                            + "/" + _plugin.getMyConfig()._fmysql.get("data"),
                            _plugin.getMyConfig()._fmysql.get("user"),
                            _plugin.getMyConfig()._fmysql.get("pass")
                        );
                    }
                    break;
                case "homepage":
                    if(_plugin.getMyConfig()._hmysql.containsKey("use")?Boolean.getBoolean(_plugin.getMyConfig()._hmysql.get("use")):false) {
                        con = DriverManager.getConnection("jdbc:mysql://" + _plugin.getMyConfig()._hmysql.get("host")
                            + ":" + _plugin.getMyConfig()._hmysql.get("port")
                            + "/" + _plugin.getMyConfig()._hmysql.get("data"),
                            _plugin.getMyConfig()._hmysql.get("user"),
                            _plugin.getMyConfig()._hmysql.get("pass")
                        );
                    }
                    break;
                default:
                    con = null;
                    break;
            }
        } catch(SQLException e) {
            _plugin.getLog().error("Fehler beim Herstellen der Verbindung zum MySQL Type : " + t + "!", e);
        }
        return con;
    }
    
    public void setMcJobsByPlayer(Player p) {
        if(!_plugin.getMyConfig()._tmysql.containsKey("mcjobs"))
            return;
        
        if(!_plugin.isMcJobs() && PlayerCache.getJobCount(p.getName()) < 1)
            return;
        
        Connection con = null;
        try {
            con = getConnect(_plugin.getMyConfig()._tmysql.get("mcjobs"));
            if(con == null)
                return;
            PreparedStatement statement = con.prepareStatement("DELETE FROM `mcjobs` WHERE `username` = ?");
            statement.setString(1, p.getName());
            statement.execute();
            statement.close();
            
            statement = con.prepareStatement("INSERT INTO `mcjobs` (`username`.`jobname`,`level`,`exp`,`exp_up`,`rank`) VALUES (?,?.?,?,?,?)");
            for(String jobname: PlayerCache.getPlayerJobs(p.getName())) {
                statement.setString(1, p.getName());
                statement.setString(2, jobname);
                statement.setInt(3, PlayerCache.getJobLevel(p.getName(), jobname));
                statement.setInt(4, (int)PlayerCache.getJobExp(p.getName(), jobname));
                statement.setLong(5, (long)Leveler.getXPtoLevel(PlayerCache.getJobLevel(p.getName(), jobname)));
                statement.setString(6, PlayerCache.getJobRank(p.getName(), jobname));
                statement.execute();
            }
            statement.close();
        } catch(SQLException e) {
            _plugin.getLog().error("Konnte die Jobs von " + p.getName() + " nicht updaten.", e);
        } finally {
            close(con);
        }
    }
    
    public final void setupMcJobsStructure() {
        if(!_plugin.getMyConfig()._tmysql.containsKey("mcjobs"))
            return;
        
        Connection con = null;
        try {
            con = getConnect(_plugin.getMyConfig()._tmysql.get("mcjobs"));
            if(con == null)
                return;
            PreparedStatement statement = con.prepareStatement("CREATE TABLE IF NOT EXISTS `mcjobs` (`username` varchar(32) NOT NULL, "
                    + "`jobname` varchar(32) NOT NULL, "
                    + "`level` int(11) NOT NULL, "
                    + "`exp` bigint(13) NOT NULL, "
                    + "`exp_up` bigint(13) NOT NULL, "
                    + "`rank` varchar(32) DEFAULT NULL, "
                    + "PRIMARY KEY (`username`,`jobname`,`level`)) "
                    + "ENGINE=MyISAM DEFAULT CHARSET=latin1"
            );
            statement.execute();
            statement.close();
        } catch(SQLException e) {
            
        } finally {
            close(con);
        }
    }
    
    public final void setupJumpnRunStructure() {
        if(!_plugin.getMyConfig()._tmysql.containsKey("jumpnrun"))
            return;
        
        Connection con = null;
        try {
            con = getConnect(_plugin.getMyConfig()._tmysql.get("jumpnrun"));
            if(con == null)
                return;
            PreparedStatement statement = con.prepareStatement("CREATE TABLE IF NOT EXISTS `jumpnruns` (`username` varchar(32) NOT NULL, "
                    + "`arenaid` int(11) NOT NULL, "
                    + "`attempts` int(11) NOT NULL DEFAULT '0', "
                    + "`wins` int(11) NOT NULL DEFAULT '0', "
                    + "PRIMARY KEY (`username`,`arenaid`)) "
                    + "ENGINE=MyISAM DEFAULT CHARSET=latin1"
            );
            statement.execute();
            statement.close();
        } catch(SQLException e) {
            
        } finally {
            close(con);
        }
    }
    
    public final void setupPvPStructure() {
        if(!_plugin.getMyConfig()._tmysql.containsKey("pvp"))
            return;
        
        Connection con = null;
        try {
            con = getConnect(_plugin.getMyConfig()._tmysql.get("pvp"));
            if(con == null)
                return;
            PreparedStatement statement = con.prepareStatement("CREATE TABLE IF NOT EXISTS `mcjobs` (`username` varchar(32) NOT NULL, "
                    + "`wins` int(11) NOT NULL DEFAULT '0', "
                    + "`lose` int(11) NOT NULL DEFAULT '0', "
                    + "`exp_up` bigint(13) NOT NULL, "
                    + "`rank` varchar(32) DEFAULT NULL, "
                    + "PRIMARY KEY (`username`)) "
                    + "ENGINE=MyISAM DEFAULT CHARSET=latin1"
            );
            statement.execute();
            statement.close();
        } catch(SQLException e) {
            
        } finally {
            close(con);
        }
    }
    
    public String isPlayerNotActivatedYet(Player p) {
        if(!_plugin.getMyConfig()._tmysql.containsKey("authme_activation"))
            return "";
        
        String msg = "";
        Connection con = null;
        try {
            con = getConnect(_plugin.getMyConfig()._tmysql.get("authme_activation"));
            if(con == null)
                return "";
            
            PreparedStatement statement = con.prepareStatement("SELECT `banMessage` FROM `authMe_activation` WHERE `name` = ? LIMIT 0,1");
            statement.setString(1, p.getName());
            ResultSet rset = statement.executeQuery();
            if(rset.next()) {
                msg = rset.getString("banMessage");
            }
            statement.close();
        } catch(SQLException e) {
            _plugin.getLog().error("Konnte nicht feststellen ob Account noch gesperrt ist.");
            close(con);
            return "";
        } finally {
            close(con);
            return msg;
        }
    }
    
    public void setupActivationStructure() {
        if(!_plugin.getMyConfig()._tmysql.containsKey("authme_activation"))
            return;
        
        Connection con = null;
        try {
            con = getConnect(_plugin.getMyConfig()._tmysql.get("authme_activation"));
            if(con == null)
                return;
            PreparedStatement statement = con.prepareStatement("CREATE TABLE IF NOT EXISTS `authMe_activation` (" 
                + "  `id` int(11) NOT NULL,"
                + "  `name` varchar(32) NOT NULL,"
                + "  `activation` varchar(64) NOT NULL,"
                + "  `status` enum('1','2') NOT NULL DEFAULT '1',"
                + "  `banMessage` text NOT NULL,"
                + "  PRIMARY KEY (`id`)"
                + ") ENGINE=MyISAM DEFAULT CHARSET=latin1"
            );
            statement.execute();
            statement.close();
        } catch(SQLException e) {
            
        } finally {
            close(con);
        }
    }
    
    public void close(Connection con) {
        if(con == null)
            return;
        
        try {
            con.close();
        } catch(SQLException e) {
            _plugin.getLog().error("Fehler beim beenden einer MySQL Verbindung", e);
        }
    }
    
    public MySQLStatistik getStats() {
        return _stats;
    }
    
    public MySQLJump getJump() {
        return _jump;
    }
    
    public MySQLPvP getPvP() {
        return _pvp;
    }
    
}
