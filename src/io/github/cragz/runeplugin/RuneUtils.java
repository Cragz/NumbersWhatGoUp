package io.github.cragz.runeplugin;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.cragz.numberswhatgoup.Utils;

public class RuneUtils
{
	public static ItemStack getMarkedRuneBook(Location location, String name)
	{
		ItemStack book = new ItemStack(Material.ENCHANTED_BOOK, 1);
		
		if (name == null)
		{
			name = "Marked";
		}
		
		String newName = String.format("%s [%s]", Utils.getNameFromMaterial(book.getType()), name);
		
		Utils.setItemLore(book, LocationSerializer.getLocationString(location), ChatColor.GRAY, true);
		Utils.setItemName(book, newName, ChatColor.YELLOW, true);
		
		return book;
	}
}
