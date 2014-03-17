package de.papaharni.amcserver.util;

import de.papaharni.amcserver.AMCServer;

/**
 *
 * @author Pappi
 */
public class PvPCounter
{
    private String userName;
    private int userWins;
    private int userLose;

    public PvPCounter(String uname)
    {
        int playerData[] = AMCServer.getInstance().getMySQL().getPvP().getPvPStats(uname);
        userName = uname;
        userWins = playerData[0];
        userLose = playerData[1];
    }

    public int getWins()
    {
        return userWins;
    }

    public int getLose()
    {
        return userLose;
    }

    public void setWins(int allWins)
    {
        userWins = allWins;
    }

    public void setLose(int allLose)
    {
        userLose = allLose;
    }

    public void addWins(int addWins)
    {
        userWins += addWins;
    }

    public void addLoses(int addLose)
    {
        userLose += addLose;
    }

    public void addWin()
    {
        userWins++;
    }

    public void addLose()
    {
        userLose++;
    }
    
    public int getScore() {
        return (userWins-userLose);
    }
}
