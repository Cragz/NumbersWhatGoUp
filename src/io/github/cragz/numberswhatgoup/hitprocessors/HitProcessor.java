package io.github.cragz.numberswhatgoup.hitprocessors;

import io.github.cragz.numberswhatgoup.PlayerInfoManager;
import io.github.cragz.numberswhatgoup.logging.LoggerFactory;
import io.github.cragz.numberswhatgoup.skills.SkillMetaData;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.ProjectileSource;

public abstract class HitProcessor
{
	protected SkillMetaData _skillMeta;
	
	abstract void applyOnHitEffect(Entity defender);
	
	protected void attemptSkillGain(Player player, Entity target)
	{
		if (_skillMeta.rollSkillCheck(player, target))
		{
			Double gain = _skillMeta.calculateSkillGain(player);
			PlayerInfoManager.addPlayerSkillPoints(player, _skillMeta.getSkillType(), gain);
		}
	}
	
	public Double processHit(Entity attacker, Entity defender)
	{
		if (attacker instanceof Player)
		{
			if (defender instanceof LivingEntity)
			{
				processPlayerMobHit((Player)attacker, defender);
			}
			else
			{
				processPlayerPlayerHit((Player)attacker, (Player)defender);
			}
			
			return getDamageMultiplier((Player)attacker);
		}
		else if (attacker instanceof LivingEntity)
		{
			if (defender instanceof Player)
			{
				processMobPlayerHit(attacker, (Player)defender);
			}
		}
		else if (attacker instanceof Arrow)
		{
			// Todo / Weird - had to change this as it wasn't recognising ProjectileSource.
			
			Arrow arrow = (Arrow)attacker;
			
			if (arrow.getShooter() instanceof Player)
			{
				processHit((Player)arrow.getShooter(), defender);
				return getDamageMultiplier((Player)arrow.getShooter());
			}
		}
		
		return 1.0;
	}
	
	public void processPlayerMobHit(Player player, Entity mob)
	{
		applyOnHitEffect(mob);
		attemptSkillGain(player, mob);
		
		// Fame & Karma
		// https://github.com/runuo/runuo/blob/master/Scripts/Misc/Titles.cs
	}
	
	public void processPlayerPlayerHit(Player attacker, Player defender)
	{
		applyOnHitEffect(defender);
		attemptSkillGain(attacker, defender);
		
		if (defender.isBlocking())
		{
			new SwordHitProcessor().attemptSkillGain(defender, attacker);
		}
	}
	
	public void processMobPlayerHit(Entity mob, Player player)
	{
		if (player.isBlocking())
		{
			new SwordHitProcessor().attemptSkillGain(player, mob);
		}
	}
	
	protected Double getDamageMultiplier(Player attacker)
	{
		return 1.0;
	}
}
