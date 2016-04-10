package io.github.cragz.runeplugin.runnables;

import io.github.cragz.numberswhatgoup.NwguTask;
import io.github.cragz.numberswhatgoup.enums.TaskType;
import io.github.cragz.numberswhatgoup.logging.LoggerFactory;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class RecallCastEffectTask extends NwguTask
{
	private String _playerName;
	private long _seedTime;
	
	public RecallCastEffectTask(Player player, long seedTime)
	{
		_playerName = player.getName();
		_type = TaskType.RECALLCASTEFFECT;
		_seedTime = seedTime;
	}
	
	@Override
	public void run()
	{
		Player player = Bukkit.getPlayer(_playerName);
		
		float timeDiff = System.currentTimeMillis() - _seedTime;
		float volume = timeDiff / 2500;
		
		World world = player.getWorld();
		//Location location = Utils.getHandLocation(_player);
		Location location = player.getLocation();
		
		// Todo: The getHandLocation simply isn't working so the smoke looks ridiculous.
		// Make it better!
		
		//world.playEffect(location, Effect.SMOKE, 4);
		player.playSound(location, Sound.ENTITY_ZOMBIE_INFECT, volume, 1f);
	}
}
