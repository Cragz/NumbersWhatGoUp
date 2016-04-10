package io.github.cragz.numberswhatgoup;

import io.github.cragz.numberswhatgoup.enums.TaskType;

import org.bukkit.scheduler.BukkitRunnable;

public class NwguTask extends BukkitRunnable
{
	protected TaskType _type;
	
	public TaskType getTaskType()
	{
		return _type;
	}
	
	@Override
	public void run() { }
}
