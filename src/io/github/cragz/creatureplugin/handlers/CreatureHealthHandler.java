package io.github.cragz.creatureplugin.handlers;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.cragz.numberswhatgoup.Utils;
import io.github.cragz.numberswhatgoup.logging.LoggerFactory;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.LivingEntity;

public class CreatureHealthHandler
{
	private static Integer _maxBars = 16;
	private static String[] _cachedBarArray = getCachedBarArray(_maxBars);
	private static Map<Integer, Integer> _mobVanishTaskIDs = new HashMap<Integer, Integer>();

	public static Map<Integer, Integer> getMobVanishTaskIDs()
	{
		return _mobVanishTaskIDs;
	}
	
	private static Integer calculateBars(LivingEntity entity)
	{
		Double health = entity.getHealth();
		Double maxHealth = entity.getMaxHealth();
		Double healthPercentage = (health * 100) / maxHealth;
		Double barPercentage = _maxBars * (healthPercentage / 100);
		
		//LoggerFactory.getBukkitLogger().info(String.format("%s %s %s %s", health.toString(), maxHealth.toString(), healthPercentage.toString(), barPercentage.toString()));
		//LoggerFactory.getBukkitLogger().info(String.format("%s %s", debugNonInt.toString(), debugInt.toString()));
		
		return Utils.round(barPercentage, 0, RoundingMode.UP).intValue();
	}
	
	// Not terribly efficient (understatement)!
	
	private static String generateBarString(Integer numberOfBars)
	{
		char[] bars = new char[_maxBars - 2];
		Arrays.fill(bars, '\u25AE');
		
		StringBuffer buffer = new StringBuffer();
		
		buffer.append('\u25D6');
		buffer.append(bars);
		buffer.append('\u25D7');
		buffer.insert(numberOfBars, "$8");
		buffer.insert(0, "$a");
		// buffer.insert(0,  ' ');
		// buffer.insert(0, '\u24F5');

		return ChatColor.translateAlternateColorCodes('$', buffer.toString());
	}
	
	public static String getBarString(Integer numberOfBars)
	{
		return _cachedBarArray[numberOfBars];
	}
	
	public static String getHealthBarString(LivingEntity entity)
	{
		return getBarString(calculateBars(entity));
	}
	
	private static String[] getCachedBarArray(Integer numberOfBars)
	{
		String[] cache = new String[numberOfBars + 1];
		
		for (int i = 0; i <= numberOfBars ; ++i)
		{
			cache[i] = generateBarString(i);
		}
		
		return cache;
	}
}
