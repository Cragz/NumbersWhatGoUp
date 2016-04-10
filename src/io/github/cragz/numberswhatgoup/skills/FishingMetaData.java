package io.github.cragz.numberswhatgoup.skills;

import io.github.cragz.numberswhatgoup.EntitySkillKey;
import io.github.cragz.numberswhatgoup.enums.EntityClass;
import io.github.cragz.numberswhatgoup.enums.SkillType;

public class FishingMetaData extends SkillMetaData
{
	public FishingMetaData()
	{
		super();
		
		_skillType = SkillType.FISHING;
		_skillGainFactor = 1.0;
		_professionName = "Fisherman";
		
		_skillGainMap.put(new EntitySkillKey(SkillType.FISHING, EntityClass.CAUGHT_FISH), 80.0);
		_skillGainMap.put(new EntitySkillKey(SkillType.FISHING, EntityClass.CAUGHT_ITEM), 100.0);
	}
}
