package io.github.cragz.numberswhatgoup.hitprocessors;

import io.github.cragz.numberswhatgoup.PlayerInfo;
import io.github.cragz.numberswhatgoup.PlayerInfoManager;
import io.github.cragz.numberswhatgoup.skills.SwordsmanshipMetaData;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class SwordHitProcessor extends HitProcessor
{
	public SwordHitProcessor()
	{
		super();
		
		_skillMeta = new SwordsmanshipMetaData();
	}
	
	@Override
	void applyOnHitEffect(Entity defender)
	{
		// Todo: On Hit Effects! :)
	}
	
	@Override
	public Double getDamageMultiplier(Player player)
	{
		PlayerInfo info = PlayerInfoManager.getPlayerInfo(player);
		Double skillLevel = info.getSkillLevel(_skillMeta.getSkillType());
		
		return ((skillLevel * 1.5) + 50) / 100;
	}
}
