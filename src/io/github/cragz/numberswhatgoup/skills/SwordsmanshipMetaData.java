package io.github.cragz.numberswhatgoup.skills;

import io.github.cragz.numberswhatgoup.EntitySkillKey;
import io.github.cragz.numberswhatgoup.enums.EntityClass;
import io.github.cragz.numberswhatgoup.enums.SkillType;

public class SwordsmanshipMetaData extends SkillMetaData
{
	public SwordsmanshipMetaData()
	{
		super();
		
		_skillType = SkillType.SWORDSMANSHIP;
		_skillGainFactor = 0.5;
		_professionName = "Swordsman";
		
		_skillGainMap.put(new EntitySkillKey(SkillType.SWORDSMANSHIP, EntityClass.MOB_PASSIVE), 30.0);
		_skillGainMap.put(new EntitySkillKey(SkillType.SWORDSMANSHIP, EntityClass.MOB_NEUTRAL), 50.0);
		_skillGainMap.put(new EntitySkillKey(SkillType.SWORDSMANSHIP, EntityClass.MOB_UTILITY), 70.0);
		_skillGainMap.put(new EntitySkillKey(SkillType.SWORDSMANSHIP, EntityClass.MOB_HOSTILE), 90.0);
		_skillGainMap.put(new EntitySkillKey(SkillType.SWORDSMANSHIP, EntityClass.MOB_BOSS), 100.0);
	}
}
