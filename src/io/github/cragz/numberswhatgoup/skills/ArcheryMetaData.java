package io.github.cragz.numberswhatgoup.skills;

import io.github.cragz.numberswhatgoup.EntitySkillKey;
import io.github.cragz.numberswhatgoup.enums.EntityClass;
import io.github.cragz.numberswhatgoup.enums.SkillType;

public class ArcheryMetaData extends SkillMetaData
{
	public ArcheryMetaData()
	{
		super();
		
		_skillType = SkillType.ARCHERY;
		_skillGainFactor = 1.0;
		_professionName = "Archer";
		
		_skillGainMap.put(new EntitySkillKey(_skillType, EntityClass.MOB_PASSIVE), 40.0);
		_skillGainMap.put(new EntitySkillKey(_skillType, EntityClass.MOB_NEUTRAL), 60.0);
		_skillGainMap.put(new EntitySkillKey(_skillType, EntityClass.MOB_UTILITY), 80.0);
		_skillGainMap.put(new EntitySkillKey(_skillType, EntityClass.MOB_HOSTILE), 99.0);
		_skillGainMap.put(new EntitySkillKey(_skillType, EntityClass.MOB_BOSS), 100.0);
	}
}
