package io.github.cragz.numberswhatgoup.hitprocessors;

import io.github.cragz.numberswhatgoup.skills.UnarmedMetaData;

import org.bukkit.entity.Entity;

public class UnarmedHitProcessor extends HitProcessor
{
	public UnarmedHitProcessor()
	{
		super();
		
		_skillMeta = new UnarmedMetaData();
	}
	
	@Override
	void applyOnHitEffect(Entity defender)
	{
		// Todo: On Hit Effects! :)
	}
}
