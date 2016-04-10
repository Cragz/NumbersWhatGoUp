package io.github.cragz.numberswhatgoup.logging;

import java.util.logging.Logger;

import org.bukkit.Bukkit;

public class LoggerFactory
{
	public static Logger getLogger(LogType logType)
	{
		switch (logType)
		{
			case BUKKIT: 	return getBukkitLogger();
			case PLUGIN: 	return getPluginLogger();
			case FILE: 		return getFileLogger();
			default:		return getBukkitLogger();
		}
	}
	
	public static Logger getBukkitLogger()
	{
		return Bukkit.getLogger();
	}
	
	public static Logger getPluginLogger()
	{
		return Bukkit.getPluginManager().getPlugin("NumbersWhatGoUp").getLogger();
	}
	
	public static Logger getFileLogger()
	{
		return Logger.getLogger("NumbersWhatGoUp_File");
	}
}
