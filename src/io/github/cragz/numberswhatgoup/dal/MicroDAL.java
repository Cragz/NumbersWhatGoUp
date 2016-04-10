package io.github.cragz.numberswhatgoup.dal;

import io.github.cragz.numberswhatgoup.PlayerInfo;
import io.github.cragz.numberswhatgoup.PlayerInfoManager;
import io.github.cragz.numberswhatgoup.enums.SkillType;
import io.github.cragz.numberswhatgoup.logging.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MicroDAL
{
	private IDatabaseConnector _dbConnector;
	
	public MicroDAL(IDatabaseConnector dbConnector)
	{
		_dbConnector = dbConnector;
	}
	
	public PlayerInfo addOrRetrievePlayer(String name) throws SQLException
	{
		if (!playerExists(name))
		{
			return addPlayer(name);
		}
		else
		{
			return getPlayer(name);
		}
	}
	
	public void updateAllPlayerValues(PlayerInfo info) throws SQLException
	{
		_dbConnector.openConnection();
		
		for (SkillType skillType : info.getLevels().keySet())
		{
			if (info.getSkillLevel(skillType) > 0.0)
			{
				String query = String.format("INSERT OR REPLACE INTO PlayerSkillLevels ( PlayerID, SkillID, SkillLevel ) VALUES (( SELECT ID FROM Players WHERE Name = '%s' LIMIT 1 ), ( SELECT ID FROM Skills WHERE Name = '%s' LIMIT 1 ), %3.1f)", info.getPlayerName(), skillType.toString(), info.getSkillLevel(skillType));
				
				// Need something here to update other player things (e.g. Karma and Fame)
				
				_dbConnector.executeNonQuery(query);
			}
		}
		
		_dbConnector.closeConnection();
	}
	
	public void setPlayerFame(String playerName, Integer fame) throws SQLException
	{
		_dbConnector.openConnection();
		
		String query = String.format("INSERT OR REPLACE INTO Player ( Name, Fame ) VALUES ( %s, %d )", playerName, fame);
		_dbConnector.executeNonQuery(query);
		
		_dbConnector.closeConnection();
	}

	public void setPlayerKarma(String playerName, Integer karma) throws SQLException
	{
		_dbConnector.openConnection();
		
		String query = String.format("INSERT OR REPLACE INTO Player ( Name, Karma ) VALUES ( %s, %d )", playerName, karma);
		_dbConnector.executeNonQuery(query);
		
		_dbConnector.closeConnection();
	}
	
	public void setPlayerStatistics(String playerName, Double karma, Double fame) throws SQLException
	{
		_dbConnector.openConnection();
		
		String query = String.format("INSERT OR REPLACE INTO Player ( Name, Karma, Fame ) VALUES ( %s, %d, %d )", playerName, karma, fame);
		_dbConnector.executeNonQuery(query);
		
		_dbConnector.closeConnection();
	}
	
	public void setPlayerSkillLevel(String playerName, String skillName, Double skillValue) throws SQLException
	{
		_dbConnector.openConnection();
		
		String query = String.format("INSERT OR REPLACE INTO PlayerSkillLevels ( PlayerID, SkillID, SkillLevel ) VALUES (( SELECT ID FROM Players WHERE Name = '%s' LIMIT 1 ), ( SELECT ID FROM Skills WHERE Name = '%s' LIMIT 1 ), %3.1f)", playerName, skillName, skillValue);
		_dbConnector.executeNonQuery(query);
		
		_dbConnector.closeConnection();
	}
	
	public Boolean playerExists(String name) throws SQLException
	{
		_dbConnector.openConnection();
		
		Boolean exists = false;
		
		if (_dbConnector instanceof SqliteConnector)
		{
			String query = String.format("SELECT COUNT(*) FROM Players WHERE Name = '%s'", name);
			
			ResultSet results = _dbConnector.executeQuery(query);
			
			results.next();
			exists = (results.getInt(1) > 0);
		}
		
		_dbConnector.closeConnection();
		
		return exists;
	}
	
	public PlayerInfo getPlayer(String name) throws SQLException
	{
		PlayerInfo info = new PlayerInfo(name);
		
		_dbConnector.openConnection();
		
		if (_dbConnector instanceof SqliteConnector)
		{
			String query = String.format("SELECT s.Name, psl.SkillLevel FROM PlayerSkillLevels psl JOIN Players p on p.ID = psl.PlayerID JOIN Skills s on s.ID = psl.SkillID WHERE p.Name = '%s'", name);
			
			ResultSet results = _dbConnector.executeQuery(query);
			
			while (results.next())
			{
				String skillType = results.getString("Name");
				Double skillLevel = results.getDouble("SkillLevel");
				
				info.getLevels().put(SkillType.valueOf(skillType.toUpperCase()), skillLevel);
			}
		}
		
		_dbConnector.closeConnection();
		
		return info;
	}
	
	public PlayerInfo addPlayer(String name) throws SQLException
	{
		_dbConnector.openConnection();
		
		if (_dbConnector instanceof SqliteConnector)
		{
			_dbConnector.executeNonQuery(String.format("INSERT OR IGNORE INTO Players ( Name ) VALUES ( '%s' )", name));
		}
		
		_dbConnector.closeConnection();
		
		return new PlayerInfo(name);
	}
	
	public void massSavePlayerInfo()
	{
		try
		{
			for (Player player : Bukkit.getOnlinePlayers())
			{
				PlayerInfo info = PlayerInfoManager.getPlayerInfo(player);
				updateAllPlayerValues(info);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public void createDatabase()
	{
		if (_dbConnector instanceof SqliteConnector)
		{
			try
			{
				_dbConnector.openConnection();
				
				_dbConnector.executeNonQuery("CREATE TABLE IF NOT EXISTS Players (ID INTEGER PRIMARY KEY, Name TEXT UNIQUE COLLATE NOCASE NOT NULL, Fame INTEGER, Karma INTEGER)");
				_dbConnector.executeNonQuery("CREATE TABLE IF NOT EXISTS Skills (ID INTEGER PRIMARY KEY, Name VARCHAR(50) UNIQUE COLLATE NOCASE NOT NULL)");
				_dbConnector.executeNonQuery("CREATE TABLE IF NOT EXISTS PlayerSkillLevels (PlayerID INTEGER NOT NULL, SkillID INTEGER NOT NULL, SkillLevel REAL NOT NULL, PRIMARY KEY ( PlayerID, SkillID ))");
				
				_dbConnector.closeConnection();
			}
			catch (SQLException e)
			{
				LoggerFactory.getPluginLogger().warning("Critical Database Error: Unable to create database tables. Further info:");
				e.printStackTrace();
			}
		}
	}
	
	public void seedDatabase()
	{
		if (_dbConnector instanceof SqliteConnector)
		{
			try
			{
				_dbConnector.openConnection();
				
				_dbConnector.executeNonQuery("INSERT OR IGNORE INTO Skills ( Name ) VALUES ( 'Archery' )");
				_dbConnector.executeNonQuery("INSERT OR IGNORE INTO Skills ( Name ) VALUES ( 'Axefighting' )");
				_dbConnector.executeNonQuery("INSERT OR IGNORE INTO Skills ( Name ) VALUES ( 'Blacksmithy' )");
				_dbConnector.executeNonQuery("INSERT OR IGNORE INTO Skills ( Name ) VALUES ( 'Fishing' )");
				_dbConnector.executeNonQuery("INSERT OR IGNORE INTO Skills ( Name ) VALUES ( 'Lumberjacking' )");
				_dbConnector.executeNonQuery("INSERT OR IGNORE INTO Skills ( Name ) VALUES ( 'Mining' )");
				_dbConnector.executeNonQuery("INSERT OR IGNORE INTO Skills ( Name ) VALUES ( 'Swordsmanship' )");
				_dbConnector.executeNonQuery("INSERT OR IGNORE INTO Skills ( Name ) VALUES ( 'Taming' )");
				_dbConnector.executeNonQuery("INSERT OR IGNORE INTO Skills ( Name ) VALUES ( 'Unarmed' )");
				
				_dbConnector.closeConnection();
			}
			catch (SQLException e)
			{
				LoggerFactory.getPluginLogger().warning("Critical Database Error: Unable to create database tables. Further info:");
				e.printStackTrace();
			}
		}
	}
}
