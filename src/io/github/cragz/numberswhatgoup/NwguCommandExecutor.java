package io.github.cragz.numberswhatgoup;

import io.github.cragz.numberswhatgoup.dal.MicroDAL;
import io.github.cragz.numberswhatgoup.dal.MicroDALFactory;
import io.github.cragz.numberswhatgoup.enums.SkillType;
import io.github.cragz.numberswhatgoup.logging.LoggerFactory;
import io.github.cragz.numberswhatgoup.skills.CompetencyTitles;
import io.github.cragz.runeplugin.LocationSerializer;
import io.github.cragz.runeplugin.RuneUtils;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class NwguCommandExecutor implements CommandExecutor
{
	private JavaPlugin _plugin;
	
	public NwguCommandExecutor(JavaPlugin plugin)
	{
		_plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		switch (command.getName().toLowerCase())
		{
			case "givescoreboard":	return giveScoreboard(sender, args);
			case "mark":			return markRune(sender, args);
			case "unmark":			return unmarkRune(sender);
			case "forcesave":		return forceSave();
			case "skills":			return skills(sender);
			case "setskill":		return setSkill(sender, args);
			case "playallsounds":	return playAllSounds(sender);
			default:				return false;
		}
	}
	
	private boolean playAllSounds(CommandSender sender)
	{
		// KILL ME
		
		ThrowAway.playAllSounds((Player)sender);
		
		return true;
	}
	
	private boolean unmarkRune(CommandSender sender)
	{
		// Todo: This isn't exactly defense in depth ...
		// Todo: Ultra ugly. Next on the fix list. (Same with its brother below)
		
		if (sender instanceof Player)
		{
			Utils.switchItemInHand((Player)sender, new ItemStack(Material.BOOK, 1));
		}
		
		return true;
	}
	
	private boolean markRune(CommandSender sender, String[] args)
	{
		// Todo: Needs tidying up asap
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage("Only players can mark items!");
			return false;
		}
		
		Player player = (Player)sender;
		ItemStack item = ((Player)sender).getItemInHand();
		
		if (item == null || (item.getType() != Material.BOOK && item.getType() != Material.ENCHANTED_BOOK))
		{
			sender.sendMessage("This item refuses to take your mark.");
			return false;
		}
		
		if (args.length == 0)
		{
			Utils.switchItemInHand(player, RuneUtils.getMarkedRuneBook(player.getLocation(), "Marked"));
		}
		else
		{
			Utils.switchItemInHand(player, RuneUtils.getMarkedRuneBook(player.getLocation(), args[0]));
		}
		
		sender.sendMessage("Your book has been enchanted with the power of RECALL!");
		
		return true;
	}
	
	private boolean giveScoreboard(CommandSender sender, String[] args)
	{
		if (args.length != 1)
		{
			sender.sendMessage("Usage: /givescoreboard <player>");
			return false;
		}

		Player p = _plugin.getServer().getPlayer(args[0]);
		
		if (p == null)
		{
			sender.sendMessage(String.format("Unable to find player %s.", args[0]));
			return false;
		}
		
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getNewScoreboard();
		 
		Objective objective = board.registerNewObjective("lives", "dummy");
		objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
		objective.setDisplayName("lives");
		 
		Score score = objective.getScore(p);
		score.setScore(5);
		
		return true;
	}
	
	private boolean forceSave()
	{
		MicroDAL dal = MicroDALFactory.createNew();

		try
		{
			for (Player player : _plugin.getServer().getOnlinePlayers())
			{
				PlayerInfo info = PlayerInfoManager.getPlayerInfo(player);
				dal.updateAllPlayerValues(info);
			}
		}
		catch (SQLException e)
		{
			LoggerFactory.getPluginLogger().warning("Failure when trying to run the forcesave command");
			e.printStackTrace();
		}

		return true;
	}
	
	// Todo - this is hokey.
	
	private boolean skills(CommandSender sender)
	{
		if (sender instanceof Player)
		{
			PlayerInfo info = PlayerInfoManager.getPlayerInfo((Player)sender);
			
			for (SkillType skillType : info.getLevels().keySet())
			{
				Double level = info.getLevels().get(skillType);
				String title = CompetencyTitles.getTitle(level);
				
				String message = String.format("%s: %3.1f%%", skillType.toString(), level);
				
				if (title != null)
				{
					message = String.format("[%s] %s", title, message);
				}
				
				sender.sendMessage(String.format("%s%s", ChatColor.GREEN, message));
			}
			
			return true;
		}
		else
		{
			sender.sendMessage("I'm sorry, I can't do that [guy who hates player only commands]");
			return false;
		}
	}
	
	private boolean setSkill(CommandSender sender, String[] args)
	{
		if (args.length != 3)
		{
			sender.sendMessage("Usage: /setskill <player> <type> <points>");
			return false;
		}

		Player p = _plugin.getServer().getPlayer(args[0]);
		
		if (p == null)
		{
			sender.sendMessage(String.format("Unable to find player %s.", args[0]));
			return false;
		}
		
		SkillType s;
		
		try
		{
			s = SkillType.valueOf(args[1].toUpperCase());
		}
		catch (Exception ex)
		{
			sender.sendMessage(String.format("%s is an unknown skill type.", args[1]));
			return false;
		}
		
		Double v = 0.0;
		
		try
		{
			v = Double.parseDouble(args[2]);
		}
		catch (Exception ex)
		{
			sender.sendMessage(String.format("Unable to assign %s skill points - is this a valid floating point number?", args[2]));
			return false;
		}
		
		if (v < 0.0 || v > 100.0)
		{
			sender.sendMessage(String.format("Skill points must be between 0.0 and 100.0 - %s is an invalid number.", args[2]));
			return false;
		}
		
		sender.sendMessage(String.format("Setting %s's %s points to %1.2f", p.getName(), s.toString(), v));
		
		PlayerInfoManager.setPlayerSkillPoints(p, s, v);
		
		return true;
	}
}
