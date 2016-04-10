package io.github.cragz.numberswhatgoup.skills;

import io.github.cragz.numberswhatgoup.EntitySkillKey;
import io.github.cragz.numberswhatgoup.PlayerInfo;
import io.github.cragz.numberswhatgoup.PlayerInfoManager;
import io.github.cragz.numberswhatgoup.Utils;
import io.github.cragz.numberswhatgoup.enums.EntityClass;
import io.github.cragz.numberswhatgoup.enums.SkillType;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

abstract public class SkillMetaData
{
	protected String _professionName;
	protected SkillType _skillType;
	protected Double _skillGainFactor;
	protected Map<EntitySkillKey, Double> _skillGainMap;
	
	public SkillMetaData()
	{
		_skillGainFactor = 1.0;
		_skillGainMap = new HashMap<EntitySkillKey, Double>();
		_skillType = SkillType.NONE;
		_professionName = "Nobody";
	}
	
	public Double getSkillGainFactor()
	{
		return _skillGainFactor;
	}
	
	public SkillType getSkillType()
	{
		return _skillType;
	}
	
	public Double getEntityClassMinGainLevel(Entity target)
	{
		return 0.0;
	}
	
	public Double getEntityClassMaxGainLevel(Entity target)
	{
		EntityClass entityClass = Utils.getEntityClass(target);
		EntitySkillKey compositeKey = new EntitySkillKey(_skillType, entityClass);
		
		return _skillGainMap.containsKey(compositeKey) ? _skillGainMap.get(compositeKey) : 0.0;
	}
	
	public Boolean canGainFromEntity(Entity target, Double skillLevel)
	{
		Double min = getEntityClassMinGainLevel(target);
		Double max = getEntityClassMaxGainLevel(target);
		
		return skillLevel >= min && skillLevel < max;
	}
	
	public Boolean canGainFromEntity(Entity target, Player player)
	{
		Double skillLevel = PlayerInfoManager.getPlayerSkillLevel(player, _skillType);
		return canGainFromEntity(target, skillLevel);
	}
	
	public Boolean rollSkillCheck(Player player, Entity target)
	{
		PlayerInfo info = PlayerInfoManager.getPlayerInfo(player);
		
		if (canGainFromEntity(target, player))
		{
			Double skill = info.getSkillLevel(_skillType);
			Double totalSkill = info.getTotalSkillPoints();
			Double gainFactor = getSkillGainFactor();
			Long timeSinceGain = info.getTimeSinceLastGain(_skillType);
			
			return shouldGainSkill(skill, totalSkill, gainFactor, timeSinceGain);
		}
		
		return false;
	}

	private Boolean shouldGainSkill(Double playerSkillLevel, Double playerSkillTotal, Double skillGainFactor, Long msSinceLastGain)
	{
		if (playerSkillLevel == 100.00 || playerSkillTotal == 400.00)
			return false;
		
		Double secondsSinceLastGain = ((double)msSinceLastGain)/1000;
		Double timeWaitFactor = 0.6 - Math.min(secondsSinceLastGain, 6.0)/10;

		Double gainChance = 0.0;
		
		gainChance += (400 - playerSkillTotal) / 400;
		gainChance += (100 - playerSkillLevel) / 100;
		gainChance /= 2;
		gainChance *= skillGainFactor;
		gainChance -= timeWaitFactor;

		// Debug Todo
		{
			/*Double rnd = Utils.randDouble(0.0, 1.0);
			
			Boolean doWeGain = ((gainChance * skillGainFactor) >= rnd);
			
			LoggerFactory.getPluginLogger().info(String.format("gc: (%3.1f*%3.1f) = %3.1f >= (r)%3.1f -> %s", gainChance, skillGainFactor, gainChance * skillGainFactor, rnd, doWeGain.toString()));
			
			return doWeGain;*/
		}
		
		return (gainChance >= Utils.randDouble(0.0, 1.0));
	}
	
	public Double calculateSkillGain(Player player)
	{
		PlayerInfo info = PlayerInfoManager.getPlayerInfo(player);
		
		return calculateSkillGain(info.getSkillLevel(_skillType));
	}
	
	public Double calculateSkillGain(Double playerSkillLevel)
	{
		return (playerSkillLevel < 10) ? Utils.randDouble(0.1, 0.4) : 0.1;
	}
}
