package io.github.cragz.numberswhatgoup.listeners;

import io.github.cragz.numberswhatgoup.PlayerInfo;
import io.github.cragz.numberswhatgoup.PlayerInfoManager;
import io.github.cragz.numberswhatgoup.logging.LoggerFactory;
import io.github.cragz.numberswhatgoup.skills.FishingMetaData;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;

public class FishingListener implements Listener
{
	private FishingMetaData _skillMeta;

	public FishingListener()
	{
		_skillMeta = new FishingMetaData();
	}

	@EventHandler(ignoreCancelled = true)
	public void onFish(PlayerFishEvent event)
	{
		Player player = event.getPlayer();
		Entity caught = event.getCaught();
		
		PlayerInfo info = PlayerInfoManager.getPlayerInfo(player);

		if (caught != null)
		{
			switch (event.getState())
			{
				case CAUGHT_FISH:

					if (_skillMeta.rollSkillCheck(player, caught))
					{
						Double gain = _skillMeta.calculateSkillGain(player);
						PlayerInfoManager.addPlayerSkillPoints(player, _skillMeta.getSkillType(), gain);
					}
					
					break;
					
				case CAUGHT_ENTITY:
					caughtLivingEntity(caught, info);
					break;
					
				default:
					break;
			}
		}
	}
	
	private void caughtLivingEntity(Entity caught, PlayerInfo info)
	{
		if (!caught.isDead())
		{
			LoggerFactory.getPluginLogger().info(String.format("%s hit a  %s and it was alive!", info.getPlayerName(), caught.getType().toString()));
		}
	}
}
