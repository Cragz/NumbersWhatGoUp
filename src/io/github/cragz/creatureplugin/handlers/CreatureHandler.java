package io.github.cragz.creatureplugin.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.github.cragz.creatureplugin.tasks.MonsterBuffTask;
import io.github.cragz.numberswhatgoup.Utils;
import io.github.cragz.numberswhatgoup.logging.LoggerFactory;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Monster;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CreatureHandler
{
	// This is all very test and ad-hoc.
	// Needs a great deal of refactoring.
	
	@Deprecated 
	public static void generateMonsterLevel(JavaPlugin plugin, Monster monster)
	{
		Integer level = monsterHasCustomLevel(monster) ? getMonsterLevel(monster) : calculateLevel(monster);
		setMonsterLevelData(plugin, monster, level);
	}
		
	/*
		HP		x		1		2		10		20		30		50		100
		16		0.1		16		17.6	30.4	46.4	62.4	94.4	174.4
		20		0.1		20		22		38		58		78		118		218
		100		0.1		100		110		190		290		390		590		1090
	*/
	
	public static void setMonsterLevelAndBuffs(JavaPlugin plugin, Monster monster)
	{
		Integer level = monsterHasCustomLevel(monster) ? getMonsterLevel(monster) : calculateLevel(monster);
		setMonsterLevelData(plugin, monster, level);
		buffMonsterStatic(monster, (level - 1) * 0.1);
	}
	
	// Todo: This is all a bit of a mess without any real thought having gone into it.
	
	// Notes:
	
	// Y values are between 3 and 264
	// 0,0 is zero distance. A "run" for 4 minutes gets you ~1,000 distance.
	
	private static Integer calculateLevel(Monster monster)
	{
		World world = monster.getWorld();
		
		switch (world.getEnvironment())
		{
			case NORMAL:
				
				Location location = monster.getLocation();
				
				if (Utils.isUnderground(monster))
				{
					Location spawn = world.getSpawnLocation();
					Double distance = spawn.distance(location);
					
					Double levelCalc = distance;
					
					// Add random factor to distance figure.
					
					int rand = (new Random()).nextInt(50) - 24;
					levelCalc += rand;
					
					// Multiply level based on how deep underground.
					// At 59 (just underground) this would be 0.04
					// At 3 (bottom bedrock) this would be 2.6
					// Have 1 as the bottom multiplier - don't need to be halving far out mob levels
					
					Double multiplier = Math.max(1, (60 - location.getY()) / 22);
					levelCalc *= multiplier;
					
					// LoggerFactory.getFileLogger().info(String.format("%s - Dist: %3.1f Y: %3.1f Rand: %d Multi: %3.1f Calc: %3.1f Level: %d", monster.getName(), distance, location.getY(), rand, multiplier, levelCalc, Math.min((int)(levelCalc / 100) + 1, 100)));
					
					return Math.min((int)(levelCalc / 100) + 1, 100);
				}
				else
				{
					return 1;
				}

			case NETHER:
			case THE_END:
			default:
				return 1;
		}
	}
	
	private static Boolean monsterHasCustomLevel(Monster monster)
	{
		return !monster.getMetadata("monsterLevel").isEmpty();
	}
	
	public static Integer getMonsterLevel(Monster monster)
	{
		// LoggerFactory.getFileLogger().info(String.format("Getting monster level for %s", monster.getName()));
		
		List<MetadataValue> metadata = monster.getMetadata("monsterLevel");
		
		if (!metadata.isEmpty())
		{
			// LoggerFactory.getFileLogger().info(String.format("Metadata NOT empty! Returning %d", metadata.get(0).asInt()));
			
			return metadata.get(0).asInt();
		}
		
		LoggerFactory.getFileLogger().info("Metadata EMPTY! Returning 1!");
		
		return 1;
	}
	
	private static void setMonsterLevelData(JavaPlugin plugin, Monster monster, Integer level)
	{
		monster.removeMetadata("monsterLevel", plugin);
		monster.setMetadata("monsterLevel", new FixedMetadataValue(plugin, level));

		// Todo Debug Remove
		// monster.setCustomName(String.format("Debug-Level %s", level.toString()));
	}
	
	// This could possibly go over 1024 which is the absolute max MC creature hp
	
	public static void buffMonsterStatic(Monster monster, Double buffModifier)
	{
		// EntityEquipment equipment = monster.getEquipment();
		
		Double maxHealth = monster.getMaxHealth();
		// Double currHealth = monster.getHealth();
		
		// LoggerFactory.getFileLogger().info(String.format("%s values are %3.1f / %3.1f to start with. Modifier is %3.1f", monster.getName(), monster.getHealth(), monster.getMaxHealth(), buffModifier));
		
		Double newMaxHealth = Utils.round(maxHealth + (maxHealth * buffModifier), 0); // was rounded to 1x dp but see below for error (this was the real error)
		// Double newHealth = Utils.round(Math.min(newMaxHealth, currHealth + (currHealth * buffModifier)), 1);
		
		// LoggerFactory.getFileLogger().info(String.format("newMaxHealth: %3.1f newHealth: %3.1f", newMaxHealth, newHealth));
		// LoggerFactory.getFileLogger().info(String.format("newMaxHealth: %3.1f", newMaxHealth));
		
		monster.setMaxHealth(newMaxHealth);
		monster.setHealth(newMaxHealth); // This WAS set to newHealth but started getting strange "Health must be between 0 and <fractionally shorter than maxhealth>" errors
		
		// LoggerFactory.getFileLogger().info(String.format("Setting %s hp to %3.1f of %3.1f (game values)", monster.getName(), monster.getHealth(), monster.getMaxHealth()));
		
		// A whooooole bunch of debug
		
		// Integer lv = getMonsterLevel(monster);
		// monster.setCustomName(String.format("L%s MH%3.1f", lv.toString(), monster.getMaxHealth()));
		
	}
	
	public static void randomiseMonsterEquipment(Monster monster)
	{
		// Example below
		
		monster.getEquipment().setHelmet(new ItemStack(Material.PUMPKIN));
	}
	
	public static void randomiseMonsterEffects(Monster monster)
	{
		// Example below
		
		List<PotionEffect> effects = new ArrayList<PotionEffect>();
		
		effects.add(new PotionEffect(PotionEffectType.REGENERATION, 20000, 1));
		effects.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20000, 1));
		
		new MonsterBuffTask(monster, effects).runTaskTimer(null, 10, 20000);
	}
}
