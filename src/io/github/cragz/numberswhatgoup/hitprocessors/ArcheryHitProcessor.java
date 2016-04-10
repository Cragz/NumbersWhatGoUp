package io.github.cragz.numberswhatgoup.hitprocessors;

import io.github.cragz.numberswhatgoup.skills.ArcheryMetaData;

import org.bukkit.entity.Entity;

public class ArcheryHitProcessor extends HitProcessor
{
	public ArcheryHitProcessor()
	{
		super();
		
		_skillMeta = new ArcheryMetaData();
	}
	
	@Override
	void applyOnHitEffect(Entity defender)
	{
		// Todo: On Hit Effects! :)
	}
}
