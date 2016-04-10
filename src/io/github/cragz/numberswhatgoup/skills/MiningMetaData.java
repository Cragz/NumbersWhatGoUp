package io.github.cragz.numberswhatgoup.skills;

import io.github.cragz.numberswhatgoup.EntitySkillKey;
import io.github.cragz.numberswhatgoup.enums.EntityClass;
import io.github.cragz.numberswhatgoup.enums.SkillType;

public class MiningMetaData extends SkillMetaData
{
	public MiningMetaData()
	{
		super();
		
		_skillType = SkillType.MINING;
		_skillGainFactor = 1.0;
		_professionName = "Miner";
		
		_skillGainMap.put(new EntitySkillKey(SkillType.MINING, EntityClass.GARBAGE_BLOCK), 40.0);
		_skillGainMap.put(new EntitySkillKey(SkillType.MINING, EntityClass.STANDARD_BLOCK), 80.0);
		_skillGainMap.put(new EntitySkillKey(SkillType.MINING, EntityClass.SPECIAL_BLOCK), 100.0);
	}
}
