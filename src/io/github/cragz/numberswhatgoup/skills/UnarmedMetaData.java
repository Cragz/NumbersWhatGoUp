package io.github.cragz.numberswhatgoup.skills;

import io.github.cragz.numberswhatgoup.EntitySkillKey;
import io.github.cragz.numberswhatgoup.enums.EntityClass;
import io.github.cragz.numberswhatgoup.enums.SkillType;

public class UnarmedMetaData extends SkillMetaData
{
	public UnarmedMetaData()
	{
		super();
		
		_skillType = SkillType.UNARMED;
		_skillGainFactor = 0.7;
		_professionName = "Brawler";
		
		_skillGainMap.put(new EntitySkillKey(SkillType.UNARMED, EntityClass.MOB_PASSIVE), 30.0);
		_skillGainMap.put(new EntitySkillKey(SkillType.UNARMED, EntityClass.MOB_NEUTRAL), 50.0);
		_skillGainMap.put(new EntitySkillKey(SkillType.UNARMED, EntityClass.MOB_UTILITY), 70.0);
		_skillGainMap.put(new EntitySkillKey(SkillType.UNARMED, EntityClass.MOB_HOSTILE), 90.0);
		_skillGainMap.put(new EntitySkillKey(SkillType.UNARMED, EntityClass.MOB_BOSS), 100.0);
	}
}
