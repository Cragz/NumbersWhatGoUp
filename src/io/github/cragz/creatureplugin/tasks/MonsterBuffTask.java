package io.github.cragz.creatureplugin.tasks;

import java.util.List;

import org.bukkit.entity.Monster;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class MonsterBuffTask extends BukkitRunnable
{
	private Monster _monster;
	private List<PotionEffect> _effects;
	
	public MonsterBuffTask(Monster monster, List<PotionEffect> effects)
	{
		_monster = monster;
		_effects = effects;
	}
	
	@Override
	public void run()
	{
		if (_monster == null || _monster.isDead())
		{
			this.cancel();
		}
		
		removeExistingEffects();
		addEffects();
	}
	
	private void addEffects()
	{
		for (PotionEffect effect : _effects)
		{
			_monster.addPotionEffect(effect, true);
		}
	}
	
	private void removeExistingEffects()
	{
		for (PotionEffect activeEffect : _monster.getActivePotionEffects())
		{
			Boolean override = false;
			
			for (PotionEffect pendingEffect : _effects)
			{
				if (activeEffect.getType() == pendingEffect.getType())
				{
					override = true;
					break;
				}
			}
			
			if (override)
			{
				_monster.removePotionEffect(activeEffect.getType());
			}
		}
	}
}
