package io.github.cragz.creatureplugin.listeners;

import io.github.cragz.creatureplugin.handlers.CreatureHandler;
import io.github.cragz.numberswhatgoup.Utils;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.projectiles.ProjectileSource;

public class PlayerDeathListener implements Listener
{
	@EventHandler(priority=EventPriority.HIGH)
	public void onPlayerDeathEvent(PlayerDeathEvent event)
	{
		EntityDamageEvent damageEvent = event.getEntity().getLastDamageCause();

		if (damageEvent instanceof EntityDamageByEntityEvent)
		{
			String message = event.getDeathMessage();
			Entity damager = ((EntityDamageByEntityEvent)damageEvent).getDamager();
			
			if (damager instanceof Monster)
			{
				String customName = ((Monster)damager).getCustomName();
				String killMessageName = getKillMessageName((Monster)damager);
				
				if (message != null && customName != null && killMessageName != null)
				{
					event.setDeathMessage(message.replace(customName, killMessageName));
				}
			}
			else if (damager instanceof Projectile)
			{
				ProjectileSource shooter = ((Projectile)damager).getShooter();
				
				if (shooter instanceof Monster)
				{
					String customName = ((Monster)shooter).getCustomName();
					String killMessageName = getKillMessageName((Monster)shooter);
					
					if (message != null && customName != null && killMessageName != null)
					{
						event.setDeathMessage(message.replace(customName, killMessageName));
					}
				}
			}
		}
	}
	
	private String getKillMessageName(Monster monster)
	{
		String friendlyName = Utils.getCreatureFriendlyName(monster.getType());
		Integer level = CreatureHandler.getMonsterLevel(monster);
		
		return String.format("Level %s %s", level.toString(), friendlyName);
	}
}
