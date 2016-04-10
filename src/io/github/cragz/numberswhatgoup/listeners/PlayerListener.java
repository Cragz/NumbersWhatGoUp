package io.github.cragz.numberswhatgoup.listeners;

import io.github.cragz.numberswhatgoup.PlayerInfo;
import io.github.cragz.numberswhatgoup.PlayerInfoManager;
import io.github.cragz.numberswhatgoup.dal.MicroDAL;
import io.github.cragz.numberswhatgoup.dal.MicroDALFactory;
import io.github.cragz.numberswhatgoup.logging.LoggerFactory;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener
{
	@EventHandler(ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		try
		{
			MicroDAL dal = MicroDALFactory.createNew();
			
			PlayerInfo info = dal.addOrRetrievePlayer(event.getPlayer().getName());
			PlayerInfoManager.add(info);
			
			LoggerFactory.getPluginLogger().info(String.format("Successfully loaded player %s from database.", info.getPlayerName()));
		}
		catch (Exception e)
		{
			LoggerFactory.getPluginLogger().warning("Critical Database Error : Unable to add player to or retrieve player from database!");
			e.printStackTrace();
		}
	}
	
	@EventHandler(ignoreCancelled = false)
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();
		PlayerInfoManager.remove(player);
	}
}
