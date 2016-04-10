package io.github.cragz.numberswhatgoup.skills;

import io.github.cragz.numberswhatgoup.EntitySkillKey;
import io.github.cragz.numberswhatgoup.enums.EntityClass;
import io.github.cragz.numberswhatgoup.enums.SkillType;

public class AxeFightingMetaData extends SkillMetaData
{
	public AxeFightingMetaData()
	{
		super();
		
		_skillType = SkillType.AXEFIGHTING;
		_skillGainFactor = 0.75;
		_professionName = "Axe Fighter";
		
		_skillGainMap.put(new EntitySkillKey(SkillType.AXEFIGHTING, EntityClass.MOB_PASSIVE), 30.0);
		_skillGainMap.put(new EntitySkillKey(SkillType.AXEFIGHTING, EntityClass.MOB_NEUTRAL), 50.0);
		_skillGainMap.put(new EntitySkillKey(SkillType.AXEFIGHTING, EntityClass.MOB_UTILITY), 70.0);
		_skillGainMap.put(new EntitySkillKey(SkillType.AXEFIGHTING, EntityClass.MOB_HOSTILE), 90.0);
		_skillGainMap.put(new EntitySkillKey(SkillType.AXEFIGHTING, EntityClass.MOB_BOSS), 100.0);
	}
}
