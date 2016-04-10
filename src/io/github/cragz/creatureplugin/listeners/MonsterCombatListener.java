package io.github.cragz.creatureplugin.listeners;

import io.github.cragz.creatureplugin.handlers.CreatureHandler;
import io.github.cragz.creatureplugin.handlers.CreatureHealthHandler;
import io.github.cragz.creatureplugin.tasks.MonsterHealthBarUpdateTask;
import io.github.cragz.creatureplugin.tasks.MonsterHealthBarVanishTask;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class MonsterCombatListener implements Listener
{
	private JavaPlugin _plugin;

	public MonsterCombatListener(JavaPlugin plugin)
	{
		_plugin = plugin;
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onHeal(EntityRegainHealthEvent event)
	{
		Entity entity = event.getEntity();
		
		if (entity instanceof Monster)
		{
			updateHealthBar((Monster)entity);
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onDeath(EntityDeathEvent event)
	{
		Entity entity = event.getEntity();
		
		if (entity instanceof Monster)
		{
			((Monster)entity).setCustomNameVisible(false);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onHit(EntityDamageEvent event)
	{
		Entity entity = event.getEntity();
		
		if (entity instanceof Monster)
		{
			updateHealthBar((Monster)entity);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onHitByEntity(EntityDamageByEntityEvent event)
	{
		Entity attacker = event.getDamager();
		Entity defender = event.getEntity();

		if (attacker instanceof Monster || defender instanceof Monster)
		{
			// Todo: Refactor this into a HitProcessor.

			if (attacker instanceof Monster)
			{
				Monster monster = (Monster) attacker;
				Integer level = CreatureHandler.getMonsterLevel(monster);

				event.setDamage(event.getDamage() * (1 + ((double)level / 8)));
			}
		}
		
		if (defender instanceof Monster)
		{
			updateHealthBar((Monster)defender);
		}
	}
	
	private void updateHealthBar(Monster monster)
	{
		new MonsterHealthBarUpdateTask(monster).runTaskLater(_plugin, 0);

		Integer entityId = monster.getEntityId();
		Integer taskId = new MonsterHealthBarVanishTask(monster).runTaskLater(_plugin, 100).getTaskId();

		cancelPreviousVanishEvents(entityId);
		CreatureHealthHandler.getMobVanishTaskIDs().put(entityId, taskId);
	}

	private void cancelPreviousVanishEvents(Integer entityId)
	{
		if (CreatureHealthHandler.getMobVanishTaskIDs().containsKey(entityId))
		{
			Integer taskId = CreatureHealthHandler.getMobVanishTaskIDs().get(entityId);
			Bukkit.getScheduler().cancelTask(taskId);
		}
	}
}
