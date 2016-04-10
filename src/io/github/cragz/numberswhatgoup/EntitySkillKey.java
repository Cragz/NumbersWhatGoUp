package io.github.cragz.numberswhatgoup;

import io.github.cragz.numberswhatgoup.enums.EntityClass;
import io.github.cragz.numberswhatgoup.enums.SkillType;

public class EntitySkillKey
{
	public SkillType SkillType;
	public EntityClass EntityClass;
	
	public EntitySkillKey(SkillType skillType, EntityClass entityClass)
	{
		SkillType = skillType;
		EntityClass = entityClass;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof EntitySkillKey)
		{
			EntitySkillKey key = (EntitySkillKey)obj;
			return SkillType.equals(key.SkillType) && EntityClass.equals(key.EntityClass);
		}
		
		return false;
	}

    @Override
	public int hashCode()
	{
    	return (SkillType.toString() + EntityClass.toString()).hashCode();
	}
}