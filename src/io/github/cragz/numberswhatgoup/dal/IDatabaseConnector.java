package io.github.cragz.numberswhatgoup.dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface IDatabaseConnector
{
	public void openConnection() throws SQLException;
	public void closeConnection() throws SQLException;
	public ResultSet executeQuery(String query) throws SQLException;
	public void executeNonQuery(String query) throws SQLException;
}
