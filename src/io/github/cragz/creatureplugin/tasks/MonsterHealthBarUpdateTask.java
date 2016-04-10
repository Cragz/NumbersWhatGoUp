package io.github.cragz.creatureplugin.tasks;

import io.github.cragz.creatureplugin.handlers.CreatureHealthHandler;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.scheduler.BukkitRunnable;

public class MonsterHealthBarUpdateTask extends BukkitRunnable
{
	private Monster _monster;
	
	public MonsterHealthBarUpdateTask(Monster monster)
	{
		_monster = monster;
	}
	
	@Override
	public void run()
	{
		if (_monster != null)
		{
			if (!_monster.isDead() && _monster.getHealth() > 0)
			{
				String customBar = CreatureHealthHandler.getHealthBarString((LivingEntity)_monster);
				
				_monster.setCustomName(customBar);
				_monster.setCustomNameVisible(true);
			}
			else
			{
				_monster.setCustomNameVisible(false);
			}
		}
	}
}
