package io.github.cragz.numberswhatgoup.tasks;

import io.github.cragz.numberswhatgoup.dal.MicroDAL;
import io.github.cragz.numberswhatgoup.logging.LoggerFactory;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

@Deprecated
public class SaveUserInfoTask extends BukkitRunnable
{
	private MicroDAL _dal;
	
	public SaveUserInfoTask(MicroDAL dal)
	{
		_dal = dal;
	}
	
	@Override
	public void run()
	{
		// Todo. Think a bit more about exactly what we're doing asynchronously here.
		// Ideally we should either:
		// a) Cache the shit out of the data we're gonna save and not access it from the game during the asynchronous process
		// b) Not do this asynchronously. What do the big boys do?
		
		LoggerFactory.getPluginLogger().info("Saving all online player statistics ...");
		_dal.massSavePlayerInfo();
	}
}
