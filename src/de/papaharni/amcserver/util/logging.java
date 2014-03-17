/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.amcserver.util;

import de.papaharni.amcserver.AMCServer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pappi
 */
public class logging {
    
    private AMCServer _plugin;
    private static Logger _log;
    private static boolean _debug;
    
    public logging(AMCServer plugin, boolean debug) {
        _plugin = plugin;
        _log = plugin.getLogger();
        _debug = debug;
    }
    
    public static void log(String msg)
    {
        _log.log(Level.INFO, msg);
    }

    public static void error(String msg)
    {
        _log.log(Level.SEVERE, msg);
    }

    public static void error(String msg, Throwable t)
    {
        _log.log(Level.SEVERE, msg, t);
    }

    public static void debug(String msg)
    {
        if(_debug)
            _log.log(Level.WARNING,"[debug] " + msg);
    }
    
}
