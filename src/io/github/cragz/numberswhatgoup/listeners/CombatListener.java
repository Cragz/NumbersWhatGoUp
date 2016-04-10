package io.github.cragz.numberswhatgoup.listeners;

import io.github.cragz.numberswhatgoup.hitprocessors.HitProcessor;
import io.github.cragz.numberswhatgoup.hitprocessors.HitProcessorFactory;
import io.github.cragz.numberswhatgoup.logging.LoggerFactory;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class CombatListener implements Listener
{
	@EventHandler(ignoreCancelled = true)
	public void onHit(EntityDamageByEntityEvent event)
	{
		Entity attacker = event.getDamager();
		Entity defender = event.getEntity();

		if ((attacker instanceof Player || attacker instanceof Arrow || defender instanceof Player) && event.getDamage() > 0.0 && !isInvincible(defender, event.getDamage()))
		{
			HitProcessor hitProcessor = HitProcessorFactory.getHitProcessor(attacker, defender);
			
			if (hitProcessor != null)
			{
				Double damageMultiplier = hitProcessor.processHit(attacker,  defender);
				event.setDamage(event.getDamage() * damageMultiplier);
				
				// LoggerFactory.getPluginLogger().info(String.format("%3.1f damage! %s -> %s (%3.1f hp)", (event.getDamage() * damageMultiplier), attacker.getType(), defender.getType(), ((Damageable)defender).getHealth()));
			}
			else // Debug
			{
				// LoggerFactory.getPluginLogger().info(String.format("! Unprocessed Damage : %s -> %s -> %s", attacker.getType(), event.getCause().toString(), defender.getType()));
			}
		}
	}
	
	public Boolean isInvincible(Entity entity, double eventDamage)
	{
		if (entity instanceof LivingEntity)
		{
			LivingEntity le = (LivingEntity)entity;
			return (le.getNoDamageTicks() > le.getMaximumNoDamageTicks() / 2.0F) && (eventDamage <= le.getLastDamage());
		}
		
		return true;
	}
}
