package io.github.cragz.creatureplugin.listeners;

import io.github.cragz.creatureplugin.handlers.CreatureHandler;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CreatureListener implements Listener
{
	private JavaPlugin _plugin;
	
	public CreatureListener(JavaPlugin plugin)
	{
		_plugin = plugin;
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onCreatureSpawn(CreatureSpawnEvent event)
	{
		Entity creature = event.getEntity();
		
		if (creature instanceof Monster)
		{
			Monster monster = (Monster)creature;
			CreatureHandler.setMonsterLevelAndBuffs(_plugin, monster);
		}
	}
}
