package io.github.cragz.numberswhatgoup;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.github.cragz.creatureplugin.handlers.CreatureHandler;
import io.github.cragz.creatureplugin.listeners.CreatureListener;
import io.github.cragz.creatureplugin.listeners.MonsterCombatListener;
import io.github.cragz.creatureplugin.listeners.PlayerDeathListener;
import io.github.cragz.numberswhatgoup.dal.MicroDAL;
import io.github.cragz.numberswhatgoup.dal.MicroDALFactory;
import io.github.cragz.numberswhatgoup.listeners.CombatListener;
import io.github.cragz.numberswhatgoup.listeners.FishingListener;
import io.github.cragz.numberswhatgoup.listeners.PlayerListener;
import io.github.cragz.numberswhatgoup.logging.LoggerFactory;
import io.github.cragz.numberswhatgoup.logging.NwguFormatter;
import io.github.cragz.numberswhatgoup.skills.CompetencyTitles;
import io.github.cragz.runeplugin.listeners.PlayerInteractListener;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

// Todo - Base

// Add runes (Some way there)
// Split plugin into multiple
// - UltimaCraft Numbers
// - UltimaCraft Skills
// - UltimaCraft Mobs
//Monsters level up the further you are from spawn / other point (En route)
// - Add monster level to death message
// - Add ability to tag locations as safe
//Add skill titles (neophyte through grandmaster)
	// 30 Neophyte
	// 40 Novice
	// 50 Apprentice
	// 60 Journeyman
	// 70 Expert
	// 80 Adept
	// 90 Master
	// 100 Grandmaster
//Clean up metadatas which have double skilltypes
// - Add actual Bukkit metadata info for player levels
//Implement lumberjacking, mining, farming & cooking
//Clean for Git
//Javadocs
//Implement fame! Possibly quite easy - link directly to experience gain?
//- http://uo2.stratics.com/reputation/reputation-faq-reputation
//Parameters in yml
//Make database writes all asynchronous (but thread safe first!) (En route!)
//Make it so Ant will shout when there's a broken build (1st attempt failed!)
//make sure the static items (eg maps) are nulled / cleared down on deregister plugin. (This is still failing)
//Hook fishing up to the same level of rigor as melee attacks


// Todo - Extended

// Add rune bookshelves with static rune books
//Investigate: Ghosts, healers & shrines, moongates, guards, vendors, plots, stamina, encumberance
//Add reputation / karma
// Could make a start on karma. Killing baby animals, horses or bats could lose karma. Players definitely so.
//Add hiding, stealth, pickpocketing

// Done!

//Main: Implement RunUO style algo for gains
//Stop using Bukkit logger and start using plugin logger - shows who sent the message in console
//Implement Axefighting
//Implement Archery
//Change queries to prepareed statements which I parameterise (Unnecessary)

public class NumbersWhatGoUp extends JavaPlugin
{	
	@Override
	public void onEnable()
	{
		try
		{
			createFiles();
			createAndSeedStorage();
			registerCommands();
			registerEvents();
			initialiseFileLogger();
			seedMonsterLevelData(); // Split
			loadConfig();

			MicroDAL dal = MicroDALFactory.createNew();
			
			for (Player player : Bukkit.getServer().getOnlinePlayers())
			{	
				PlayerInfo info = dal.addOrRetrievePlayer(player.getName());
				PlayerInfoManager.add(info);
			}
		}
		catch (Exception e)
		{
			LoggerFactory.getPluginLogger().warning(String.format("Critical Plugin Initialisation Error! : %s", e.toString()));
			e.printStackTrace();

			getServer().getPluginManager().disablePlugin(this);
		}
	}

	@Override
	public void onDisable()
	{
		for (Handler handler : LoggerFactory.getFileLogger().getHandlers())
		{
			handler.close();
		}
		
		Bukkit.getScheduler().cancelTasks(this);
		HandlerList.unregisterAll(this);
		PlayerInfoManager.dispose();
	}
	
	private void loadConfig()
	{
		CompetencyTitles.loadCompetencyTitlesFromConfig();	
	}

	private void createFiles()
	{
		if (!getDataFolder().exists())
		{
			getDataFolder().mkdir();
		}
	}

	private void createAndSeedStorage() throws SQLException
	{
		MicroDAL dal = MicroDALFactory.createNew();

		dal.createDatabase();
		dal.seedDatabase();
	}

	private void registerCommands()
	{
		NwguCommandExecutor executor = new NwguCommandExecutor(this);
		
		getCommand("forcesave").setExecutor(executor);
		getCommand("setskill").setExecutor(executor);
		getCommand("skills").setExecutor(executor);
		getCommand("givescoreboard").setExecutor(executor);
		getCommand("mark").setExecutor(executor);
		getCommand("unmark").setExecutor(executor);
		getCommand("playallsounds").setExecutor(executor);
	}

	private void registerEvents()
	{
		PluginManager manager = getServer().getPluginManager();

		manager.registerEvents(new FishingListener(), this);
		manager.registerEvents(new CombatListener(), this);
		manager.registerEvents(new PlayerListener(), this);
		manager.registerEvents(new CreatureListener(this), this);
		manager.registerEvents(new MonsterCombatListener(this), this);
		manager.registerEvents(new PlayerDeathListener(), this);
		manager.registerEvents(new PlayerInteractListener(), this);
	}
	
	private void initialiseFileLogger()
	{
		Logger fileLogger = Logger.getLogger("NumbersWhatGoUp_File");
		
		try
		{
			File pluginDataFolder = Bukkit.getPluginManager().getPlugin("NumbersWhatGoUp").getDataFolder();
			String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HHmm").format(new Date());
			
			Handler fileHandler = new FileHandler(pluginDataFolder + File.separator + timeStamp + "_NWGU.log", true);
			fileHandler.setFormatter(new NwguFormatter());
			
			for (Handler h : fileLogger.getHandlers())
			{
				fileLogger.removeHandler(h);
			}
			
			fileLogger.setUseParentHandlers(false);
			fileLogger.addHandler(fileHandler);
			fileLogger.setLevel(Level.ALL);
		}
		catch (Exception e)
		{
			LoggerFactory.getPluginLogger().warning("An error occurred whilst configuring NumbersWhatGoUp file logger");
			e.printStackTrace();
		}
	}
	
	private void seedMonsterLevelData()
	{
		LoggerFactory.getFileLogger().finer("Seeding monster data ...");
		
		for (World world : getServer().getWorlds())
		{
			for (LivingEntity entity : world.getLivingEntities())
			{
				if (entity instanceof Monster)
				{
					Monster monster = (Monster)entity;
					CreatureHandler.generateMonsterLevel(this, monster);
				}
			}
		}
	}
}
