package io.github.cragz.runeplugin.runnables;

import io.github.cragz.numberswhatgoup.NwguTask;
import io.github.cragz.numberswhatgoup.TaskManager;
import io.github.cragz.numberswhatgoup.enums.TaskType;
import io.github.cragz.runeplugin.LocationSerializer;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;

public class RecallTask extends NwguTask
{
	private String _playerName;
	private Location _location;
	
	public RecallTask(Player player, Location location)
	{
		super();
		
		_playerName = player.getName();
		_location = location;
		_type = TaskType.RECALL;
	}
	
	@Override
	public void run()
	{
		Player player = Bukkit.getPlayer(_playerName);
		
		World world = player.getWorld();
		
		if (world.getEnvironment() == Environment.NORMAL)
		{
			world.playEffect(player.getLocation(), Effect.SMOKE, 100);
			world.playSound(player.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1, 1);
			
			player.teleport(_location);
			
			world.playEffect(_location, Effect.ENDER_SIGNAL, 33);
			world.playSound(_location, Sound.ENTITY_ENDERMEN_TELEPORT, 1, 1);
		}
		else
		{
			player.playSound(_location, Sound.BLOCK_CLOTH_STEP , 2f, 0.001f);
			player.sendMessage("Your recall attempt fizzles in this strange environment.");
		}
		
		TaskManager.reportDone(player, TaskType.RECALLCASTEFFECT);
		TaskManager.reportDone(player, TaskType.RECALL);
	}
}
