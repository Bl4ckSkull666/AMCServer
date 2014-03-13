package de.papaharni.amcserver.util;

/**
 *
 * @author Pappi
 */
public class PvPCounter
{

    public PvPCounter(String uname)
    {
        int playerData[] = PvPRewards.getInstance().getMySQL().getPlayerData(uname);
        if(playerData[0] > -1 && playerData[1] > -1)
        {
            userName = uname;
            userWins = playerData[0];
            userLose = playerData[1];
        } else
        {
            userName = uname;
            userWins = 0;
            userLose = 0;
        }
    }

    public int getWins()
    {
        return userWins;
    }

    public int getLose()
    {
        return userLose;
    }

    public int getUserId()
    {
        return userId;
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

    private int userId;
    private String userName;
    private int userWins;
    private int userLose;
}
