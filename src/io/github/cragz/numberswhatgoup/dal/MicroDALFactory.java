package io.github.cragz.numberswhatgoup.dal;

public class MicroDALFactory
{
	public static MicroDAL createNew()
	{
		// Todo: "Configuration"
		
		String databaseName = "NumbersWhatGoUp.db";
		String databaseType = "Sqlite";
		
		switch (databaseType)
		{
			case "Sqlite":
				return new MicroDAL(new SqliteConnector(databaseName));
		}
		
		return null;
	}
}
