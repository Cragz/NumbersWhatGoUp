package io.github.cragz.runeplugin.listeners;

import io.github.cragz.numberswhatgoup.TaskManager;
import io.github.cragz.numberswhatgoup.enums.TaskType;
import io.github.cragz.runeplugin.BookshelfInfo;
import io.github.cragz.runeplugin.BookshelfManager;
import io.github.cragz.runeplugin.LocationSerializer;
import io.github.cragz.runeplugin.SerializableLocation;
import io.github.cragz.runeplugin.runnables.RecallCastEffectTask;
import io.github.cragz.runeplugin.runnables.RecallTask;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerInteractListener implements Listener
{
	private Material _runeMaterial = Material.ENCHANTED_BOOK;
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	{
		if (event.getFrom().distance(event.getTo()) >= 0.1)
		{
			if (TaskManager.isTaskRunning(event.getPlayer(), TaskType.RECALL))
			{
				TaskManager.cancelTask(event.getPlayer(), TaskType.RECALL);
				TaskManager.cancelTask(event.getPlayer(), TaskType.RECALLCASTEFFECT);
				
				event.getPlayer().playSound(event.getTo(), Sound.BLOCK_CLOTH_STEP, 2f, 0.001f);
			}
		}
	}
	
	@EventHandler
	public void onPlayerUse(PlayerInteractEvent event)
	{
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			Block block = event.getClickedBlock();
			
			if (block.getType() == Material.BOOKSHELF)
			{
				SerializableLocation location = new SerializableLocation(block.getWorld().getName(), block.getX(), block.getY(), block.getZ());
				
				BookshelfInfo info = BookshelfManager.getRuneBookshelf(location.returnLocation());
				
				if (info != null)
				{
					Player player = event.getPlayer();
					
					Inventory shelf = player.getServer().createInventory(null, 9, "Rune Bookshelf");
					
					for (ItemStack runeBook : info.getBooks())
					{
						shelf.addItem(runeBook);
					}
					
					player.openInventory(shelf);
				}
			}
		}
		else if (event.getAction() == Action.RIGHT_CLICK_AIR)
		{
			ItemStack item = event.getPlayer().getItemInHand();
			
			if (item.getType() == _runeMaterial)
			{
				ItemMeta metaClone = item.getItemMeta();
				
				if (metaClone.hasLore())
				{
					List<String> lore = metaClone.getLore();
					String serializedLocation = ChatColor.stripColor(lore.get(0));
					
					if (LocationSerializer.isStringSerializedLocation(serializedLocation))
					{
						Location tpLocation = LocationSerializer.getLocationFromString(serializedLocation);
						teleportPlayer(event.getPlayer(), tpLocation);
					}
				}
			}
		}
	}
	
	private void teleportPlayer(Player player, Location location)
	{
		if (TaskManager.isTaskRunning(player, TaskType.RECALL))
		{
			TaskManager.cancelTask(player, TaskType.RECALL);
			TaskManager.cancelTask(player, TaskType.RECALLCASTEFFECT);
		}
		
		TaskManager.startRepeatingTask(player, new RecallCastEffectTask(player, System.currentTimeMillis()), 5);
		TaskManager.startTaskLater(player, new RecallTask(player, location), 50);
	}
}
