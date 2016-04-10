package io.github.cragz.creatureplugin.tasks;

import org.bukkit.entity.Monster;
import org.bukkit.scheduler.BukkitRunnable;

public class MonsterHealthBarVanishTask extends BukkitRunnable
{
	private Monster _monster;
	
	public MonsterHealthBarVanishTask(Monster monster)
	{
		_monster = monster;
	}
	
	@Override
	public void run()
	{
		if (!_monster.isDead())
		{
			_monster.setCustomNameVisible(false);
		}
	}
}
