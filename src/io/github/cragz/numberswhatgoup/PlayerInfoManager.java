package io.github.cragz.numberswhatgoup;

import io.github.cragz.numberswhatgoup.dal.MicroDALFactory;
import io.github.cragz.numberswhatgoup.enums.SkillTitle;
import io.github.cragz.numberswhatgoup.enums.SkillType;
import io.github.cragz.numberswhatgoup.logging.LoggerFactory;
import io.github.cragz.numberswhatgoup.skills.CompetencyTitles;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public final class PlayerInfoManager
{
	private static Map<String, PlayerInfo> _map = new HashMap<String, PlayerInfo>();

	public static PlayerInfo add(Player player)
	{
		return add(new PlayerInfo(player.getName()));
	}

	public static PlayerInfo add(PlayerInfo info)
	{
		_map.put(info.getPlayerName(), info);
		
		return info;
	}

	public static void remove(String name)
	{
		if (_map.containsKey(name))
		{
			_map.remove(name);
		}
	}

	public static void remove(Player player)
	{
		remove(player.getName());
	}
	
	public static PlayerInfo getPlayerInfo(String name)
	{
		return _map.get(name);
	}
	
	public static PlayerInfo getPlayerInfo(Player player)
	{
		return getPlayerInfo(player.getName());
	}
	
	public static Double getPlayerSkillLevel(String name, SkillType skillType)
	{
		return getPlayerInfo(name).getSkillLevel(skillType);
	}
	
	public static Double getPlayerSkillLevel(Player player, SkillType skillType)
	{
		return getPlayerSkillLevel(player.getName(), skillType);
	}
	
	public static void addPlayerSkillPoints(Player player, SkillType skillType, Double value)
	{
		PlayerInfo info = getPlayerInfo(player.getName());
		info.addSkillPoints(skillType, value);
		
		Double newLevel = getPlayerSkillLevel(player, skillType);
		Double prevLevel = newLevel - value;
		
		if (player != null && player.isOnline())
		{
			String message = String.format("Your %s skill has increased by %3.1f%% to %3.1f%%", skillType.toString(), prevLevel, newLevel);
			player.sendMessage(ChatColor.AQUA + message);
			
			sendCompetencyMessage(player, skillType, prevLevel, newLevel);
		}
		
		updateDatabaseSkill(player.getName(), skillType.toString(), newLevel);
	}
	
	public static void setPlayerSkillPoints(Player player, SkillType skillType, Double value)
	{
		PlayerInfo info = getPlayerInfo(player.getName());		
		Double prevLevel = info.getSkillLevel(skillType);

		info.setSkillLevel(skillType, value);
		
		if (player != null && player.isOnline())
		{
			String message = String.format("Your skill in %s has been forcefully set to %3.1f%%", skillType.toString(), value);
			player.sendMessage(ChatColor.AQUA + message);
			
			sendCompetencyMessage(player, skillType, prevLevel, value);
		}
		
		updateDatabaseSkill(player.getName(), skillType.toString(), value);
	}
	
	// Messy
	
	private static void sendCompetencyMessage(Player player, SkillType skillType, Double prevLevel, Double newLevel)
	{
		if (player != null && player.isOnline())
		{
			SkillTitle titleEarned = skillTitleEarned(prevLevel, newLevel);
			
			if (titleEarned != null)
			{
				// Todo: Haha @ "in"
				// Can't spin up a new MetaData every time we want to find out what the profession is.
				// This wants to say e.g. You are now a novice swordsman!, not You are now a novice in SWORDSMANSHIP!
				
				String earnedMessage = String.format("Congratulations! You are now %s in %s!", CompetencyTitles.getPractitionerTitle(titleEarned), skillType.toString());
				player.sendMessage(ChatColor.AQUA + earnedMessage);
			}
		}
	}
	
	// Belongs in a "Skill" handler. Try CompetencyTitles (rename).
	
	private static SkillTitle skillTitleEarned(Double previousSkillLevel, Double newSkillLevel)
	{
		SkillTitle highestEarned = null;
		
		for (SkillTitle title : SkillTitle.values())
		{
			if (newSkillLevel < title.getLevelRequirement())
			{
				break;
			}
			
			if (previousSkillLevel < title.getLevelRequirement())
			{
				highestEarned = title;
			}
		}
		
		return highestEarned;
	}

	// Debug Todo Delete Horror
	// This is fairly hideous. On the right lines but not right. Fix.
	
	private static void updateDatabaseSkill(final String playerName, final String skill, final Double value)
	{
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					MicroDALFactory.createNew().setPlayerSkillLevel(playerName, skill, value);
				} catch (Exception e) {
					LoggerFactory.getPluginLogger().warning("Totes critical - Unable to update player skill level");
					e.printStackTrace();
				}
			};
		}.runTaskLaterAsynchronously(Bukkit.getPluginManager().getPlugin("NumbersWhatGoUp"), 10);
	}

	private static void updateDatabaseStats(final String playerName, final Double fame, final Double karma)
	{
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					MicroDALFactory.createNew().setPlayerStatistics(playerName, karma, fame);
				} catch (Exception e) {
					LoggerFactory.getPluginLogger().warning("Totes critical - Unable to update player statistics");
					e.printStackTrace();
				}
			};
		}.runTaskLaterAsynchronously(Bukkit.getPluginManager().getPlugin("NumbersWhatGoUp"), 10);
	}
	
	public static void dispose()
	{
		if (_map != null)
		{
			_map.clear();
			_map = null;
		}
	}
}
