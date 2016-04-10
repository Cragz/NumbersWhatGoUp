package io.github.cragz.runeplugin;

import io.github.cragz.numberswhatgoup.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationSerializer
{
	public static String getLocationString(Location location)
	{
		try
		{
			String x = Utils.round(location.getX(), 2).toString();
			String y = Utils.round(location.getY(), 2).toString();
			String z = Utils.round(location.getZ(), 2).toString();
			
			return String.format("%s,%s,%s,%s", location.getWorld().getName(), x, y, z);
		}
		catch (Exception e)
		{
			System.out.println(e);
			return null;
		}
	}
	
	// Todo. Debug. Delete. Hacky as all hell. Make better.
	
	public static Boolean isStringSerializedLocation(String toTest)
	{
		try
		{
			String[] parts = toTest.split(",");
			
			if (parts.length != 4)
			{
				return false;
			}
		}
		catch (Exception e)
		{
			return false;
		}
		
		return true;
	}

	public static Location getLocationFromString(String serializedLocation)
	{
		String[] parts = serializedLocation.split(",");
		
		if (parts.length != 4)
		{
			throw new IllegalArgumentException("Location must contain four parts (x, y, z, world)."); 
		}
		
		try
		{
			String worldName = parts[0];
			Double x = Double.parseDouble(parts[1]);
			Double y = Double.parseDouble(parts[2]);
			Double z = Double.parseDouble(parts[3]);
			
			World world = Bukkit.getWorld(worldName);
			
			return new Location(world, x, y, z);
		}
		catch (Exception e)
		{
			System.out.println(e);
			return null;
		}
	}
}
