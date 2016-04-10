package io.github.cragz.numberswhatgoup.dal;

import io.github.cragz.numberswhatgoup.logging.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;

public class SqliteConnector implements IDatabaseConnector
{
	// This needs thoroughly fleshing out & making robust. Pretty awful.
	
	private Connection _connection;
	private String _connectionString;
	
	public SqliteConnector(String databaseName)
	{
		try
		{
			Class.forName("org.sqlite.JDBC");
		}
		catch (Exception e)
		{
			LoggerFactory.getPluginLogger().warning("Critical Database Error: Unable to load org.sqlite.JDBC.");
		}
		
		_connectionString = String.format("jdbc:sqlite:%s/%s", Bukkit.getPluginManager().getPlugin("NumbersWhatGoUp").getDataFolder().getAbsolutePath(), databaseName);
	}
	
	@Override
	public void openConnection() throws SQLException
	{
		if (_connection == null || _connection.isClosed())
		{
			_connection = DriverManager.getConnection(_connectionString);
		}
	}
	
	@Override
	public void closeConnection() throws SQLException
	{
		if (_connection != null)
			_connection.close();
	}

	@Override
	public ResultSet executeQuery(String query) throws SQLException
	{
		openConnection();
		
		if (_connection.isReadOnly() || _connection.isClosed())
		{
			throw new SQLException("Unable to open the SQLite connection.");
		}
		
		return _connection.createStatement().executeQuery(query);
	}
	
	@Override
	public void executeNonQuery(String query) throws SQLException
	{
		openConnection();
		
		if (_connection.isReadOnly() || _connection.isClosed())
		{
			throw new SQLException("Unable to open the SQLite connection.");
		}
		
		_connection.createStatement().execute(query);
	}
}
