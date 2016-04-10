package io.github.cragz.numberswhatgoup;

import java.util.HashMap;
import java.util.Map;

import io.github.cragz.numberswhatgoup.enums.TaskType;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class TaskManager
{
	private static JavaPlugin _plugin = (JavaPlugin)Bukkit.getPluginManager().getPlugin("NumbersWhatGoUp");
	private static final Map<String, HashMap<TaskType, Integer>> _taskMap = new HashMap<String, HashMap<TaskType, Integer>>();
	
	public static Boolean isTaskRunning(Player player, TaskType type)
	{
		HashMap<TaskType, Integer> innerMap = _taskMap.get(player.getName());
		
		if (innerMap != null)
		{
			return innerMap.containsKey(type);	
		}
		
		return false;
	}
	
	public static void cancelTask(Player player, TaskType type)
	{
		HashMap<TaskType, Integer> innerMap = _taskMap.get(player.getName());
		
		if (innerMap != null)
		{
			Integer taskId = innerMap.get(type);
			
			if (taskId != null)
			{
				innerMap.remove(type);
				Bukkit.getScheduler().cancelTask(taskId);
			}
		}
	}
	
	public static Integer startTaskLater(Player player, NwguTask task, int tickDelay)
	{
		HashMap<TaskType, Integer> innerMap = getInnerMap(player);
		
		task.runTaskLater(_plugin, tickDelay);
		
		innerMap.put(task.getTaskType(), task.getTaskId());
		_taskMap.put(player.getName(), innerMap);
		
		return task.getTaskId();
	}

	public static Integer startRepeatingTask(Player player, NwguTask task, int ticksBetween)
	{
		HashMap<TaskType, Integer> innerMap = getInnerMap(player);
		
		task.runTaskTimer(_plugin,  0,  ticksBetween);
		
		innerMap.put(task.getTaskType(), task.getTaskId());
		_taskMap.put(player.getName(), innerMap);
		
		return task.getTaskId();
	}

	public static void reportDone(Player player, TaskType taskType)
	{
		cancelTask(player, taskType);
	}
	
	public static void reportDone(Integer taskId)
	{
		Bukkit.getScheduler().cancelTask(taskId);
	}
	
	private static HashMap<TaskType, Integer> getInnerMap(Player player)
	{
		HashMap<TaskType, Integer> innerMap = _taskMap.get(player.getName());
		
		if (innerMap != null)
		{
			return innerMap;
		}
		else
		{
			return new HashMap<TaskType, Integer>();
		}
	}
}
