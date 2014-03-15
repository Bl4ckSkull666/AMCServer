/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.amcserver.scoreboards;

/**
 *
 * @author Pappi
 */
public class SBStatistik {
    public void setScoreboard(Player player, boolean join)
    {
        delScoreBoardTask(player.getName());
        Scoreboard board = null;
        if(join)
            board = Bukkit.getScoreboardManager().getNewScoreboard();
        else
            board = player.getScoreboard();
        if(board == null)
            board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.getObjective((new StringBuilder()).append(player.getName()).append("_").append(getName()).toString().substring(0, 15).toLowerCase());
        if(obj == null)
            obj = board.registerNewObjective((new StringBuilder()).append(player.getName()).append("_").append(getName()).toString().substring(0, 15).toLowerCase(), "dummy");
        if(!isPlayerHide(player.getName()))
        {
            boolean isChange = false;
            if(obj.getDisplaySlot() != DisplaySlot.SIDEBAR)
            {
                obj.setDisplaySlot(DisplaySlot.SIDEBAR);
                obj.setDisplayName((new StringBuilder()).append(setColors(_config.sb_title_color)).append(_config.sb_title).toString());
            }
            int money = (int)Math.floor((float)getEconomy().getBalance(player.getName()));
            if(obj.getScore(Bukkit.getOfflinePlayer((new StringBuilder()).append(setColors(_config.economy_color)).append(_config.economy_title).toString())).getScore() != money)
            {
                obj.getScore(Bukkit.getOfflinePlayer((new StringBuilder()).append(setColors(_config.economy_color)).append(_config.economy_title).toString())).setScore(money);
                isChange = true;
            }
            if(isMcJobs() && PlayerCache.getJobCount(player.getName()) > 0)
            {
                ArrayList jobs = PlayerCache.getPlayerJobs(player.getName());
                Iterator i$ = jobs.iterator();
                do
                {
                    if(!i$.hasNext())
                        break;
                    String job = (String)i$.next();
                    int lvl = PlayerCache.getJobLevel(player.getName(), job).intValue();
                    job = (new StringBuilder()).append(Character.toUpperCase(job.charAt(0))).append(job.substring(1)).toString();
                    Score sco = obj.getScore(Bukkit.getOfflinePlayer((new StringBuilder()).append(setColors(_config.jobs_color)).append(job).toString()));
                    if(sco.getScore() != lvl)
                    {
                        sco.setScore(lvl);
                        isChange = true;
                    }
                } while(true);
            }
            if(isPwnFilter())
            {
                int points = PointManager.getInstance().getPlayerPoints(player).intValue();
                String fieldName = (new StringBuilder()).append(setColors(_config.pwnfilter_color)).append(_config.pwnfilter_title).toString();
                Score sco = obj.getScore(Bukkit.getOfflinePlayer(fieldName));
                if(sco.getScore() != points)
                {
                    sco.setScore(points);
                    isChange = true;
                }
            }
            if(_playerJoin.containsKey(player.getName()))
            {
                long minJoin = ((Long)_playerJoin.get(player.getName())).longValue();
                long timeNow = System.currentTimeMillis() / 1000L;
                int minOnline = (int)((timeNow - minJoin) / 60L);
                String fieldName = (new StringBuilder()).append(setColors(_config.online_color)).append(_config.online_title).toString();
                obj.getScore(Bukkit.getOfflinePlayer(fieldName)).setScore(minOnline);
            }
            if(!_config.field1_con_type.equalsIgnoreCase("none") && canFieldUse("field1"))
            {
                String fieldName = (new StringBuilder()).append(setColors(_config.field1_color)).append(_config.field1_name).toString();
                int amount = _database.getAmount("field1", player.getName());
                Score sco = obj.getScore(Bukkit.getOfflinePlayer(fieldName));
                if(sco.getScore() != amount)
                {
                    sco.setScore(amount);
                    isChange = true;
                }
            }
            if(!_config.field2_con_type.equalsIgnoreCase("none") && canFieldUse("field2"))
            {
                String fieldName = (new StringBuilder()).append(setColors(_config.field2_color)).append(_config.field2_name).toString();
                int amount = _database.getAmount("field2", player.getName());
                Score sco = obj.getScore(Bukkit.getOfflinePlayer(fieldName));
                if(sco.getScore() != amount)
                {
                    sco.setScore(amount);
                    isChange = true;
                }
            }
            if(!_config.field3_con_type.equalsIgnoreCase("none") && canFieldUse("field3"))
            {
                String fieldName = (new StringBuilder()).append(setColors(_config.field3_color)).append(_config.field3_name).toString();
                int amount = _database.getAmount("field3", player.getName());
                Score sco = obj.getScore(Bukkit.getOfflinePlayer(fieldName));
                if(sco.getScore() != amount)
                {
                    sco.setScore(amount);
                    isChange = true;
                }
            }
            if(!_config.field4_con_type.equalsIgnoreCase("none") && canFieldUse("field4"))
            {
                String fieldName = (new StringBuilder()).append(setColors(_config.field4_color)).append(_config.field4_name).toString();
                int amount = _database.getAmount("field4", player.getName());
                Score sco = obj.getScore(Bukkit.getOfflinePlayer(fieldName));
                if(sco.getScore() != amount)
                {
                    sco.setScore(amount);
                    isChange = true;
                }
            }
            if(player.getScoreboard() != board)
                player.setScoreboard(board);
            updateScoreBoard rd = new updateScoreBoard(player);
            BukkitTask task = Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(this, rd, _config.interval * 20);
            setScoreBoardTask(player.getName(), task);
        } else
        {
            obj.unregister();
            board.clearSlot(DisplaySlot.SIDEBAR);
            player.setScoreboard(board);
        }
    }

}
