package io.github.cragz.runeplugin;

import java.io.Serializable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public final class SerializableLocation implements Serializable
{
	private final String _world;
	private final double _x, _y, _z;

	public SerializableLocation(String worldName, double x, double y, double z)
	{
		_world = worldName;
		_x = x;
		_y = y;
		_z = z;
	}
	
	public SerializableLocation(Location l)
	{
		_world = l.getWorld().getName();
		_x = l.getX();
		_y = l.getY();
		_z = l.getZ();
	}

	public Location returnLocation()
	{
		double x = _x;
		double y = _y;
		double z = _z;
		World world = Bukkit.getWorld(_world);
		
		return new Location(world, x, y, z);
	}
}
