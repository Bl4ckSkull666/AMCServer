/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.amcserver.database;

import de.papaharni.amcserver.AMCServer;

/**
 *
 * @author Pappi
 */
public class MySQLMain {
    
    private AMCServer _plugin;
    
    public MySQLMain(AMCServer plugin)
    {
        _plugin = plugin;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch(Throwable t) {
            //AMCServer.getInstance().getLog().
            _plugin.getServer().getPluginManager().disablePlugin(_plugin);
            _plugin.getLog().
            throw new IllegalStateException("Couldn't find the MySQL driver!", t);
        }
        this.config = config;
        if(!getTestConnect() || !getTestBoardConnect() && this.config.votereward_use)
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        else
            setupStructure();
    }

    private boolean getTestConnect()
    {
        Connection con = null;
        try
        {
            con = getConnect();
            PreparedStatement statement = null;
            statement = con.prepareStatement((new StringBuilder()).append("SELECT * FROM `").append(config.database_prefix).append("pvprewards` LIMIT 0,1").toString());
            ResultSet rset = statement.executeQuery();
            rset.close();
            statement.close();
        }
        catch(Exception e)
        {
            close(con);
            throw new IllegalStateException("Fehler in der Konfiguration der Datenbank Config!", e);
        }
        close(con);
        return true;
        Exception exception;
        exception;
        close(con);
        return true;
    }

    public Connection getConnect()
    {
        Connection con = null;
        try
        {
            con = DriverManager.getConnection((new StringBuilder()).append("jdbc:mysql://").append(config.database_host).append(":").append(String.valueOf(config.database_port)).append("/").append(config.database_name).toString(), config.database_user, config.database_pass);
        }
        catch(SQLException e)
        {
            throw new IllegalStateException("Fehler beim Herstellen der Verbindung zum Server.!", e);
        }
        return con;
    }
    
}
