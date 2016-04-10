package io.github.cragz.numberswhatgoup.hitprocessors;

import io.github.cragz.numberswhatgoup.Utils;
import io.github.cragz.numberswhatgoup.enums.WeaponClass;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;

public class HitProcessorFactory
{
	public static HitProcessor getHitProcessor(Entity attacker, Entity defender)
	{
		if (attacker instanceof Player)
		{
			ItemStack weapon = ((Player)attacker).getItemInHand();
			
			switch (Utils.getWeaponClass(weapon))
			{
				case SWORD:		return new SwordHitProcessor();
				case AXE:		return new AxeHitProcessor();
				case PICKAXE:	return new AxeHitProcessor();
				default:		return new UnarmedHitProcessor();
			}
		}
		else if (attacker instanceof Projectile)
		{
			return new ArcheryHitProcessor();
		}
		else if (defender instanceof Player)
		{
			ItemStack weapon = ((Player)defender).getItemInHand();
			
			if (Utils.getWeaponClass(weapon) == WeaponClass.SWORD)
			{
				return new SwordHitProcessor();
			}
		}
		
		return null;
	}
}
