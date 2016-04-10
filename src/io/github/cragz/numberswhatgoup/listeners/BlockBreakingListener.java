package io.github.cragz.numberswhatgoup.listeners;

import io.github.cragz.numberswhatgoup.PlayerInfoManager;
import io.github.cragz.numberswhatgoup.Utils;
import io.github.cragz.numberswhatgoup.skills.MiningMetaData;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakingListener implements Listener
{
	private MiningMetaData _skillMeta;
	
	public BlockBreakingListener()
	{
		_skillMeta = new MiningMetaData();
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event)
	{
		Player player = event.getPlayer();
		Block block = event.getBlock();
		
		switch (Utils.getMaterialClass(block.getType()))
		{
			case GARBAGE_BLOCK:
				
				break;
				
			case STANDARD_BLOCK:
				break;
				
			case SPECIAL_BLOCK:
				break;
				
			default:
				break;
		}
	}
}
