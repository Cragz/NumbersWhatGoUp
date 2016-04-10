package io.github.cragz.numberswhatgoup.hitprocessors;

import io.github.cragz.numberswhatgoup.skills.AxeFightingMetaData;

import org.bukkit.entity.Entity;

public class AxeHitProcessor extends HitProcessor
{
	public AxeHitProcessor()
	{
		super();
		
		_skillMeta = new AxeFightingMetaData();
	}
	
	@Override
	void applyOnHitEffect(Entity defender)
	{
		// Todo: On Hit Effects! :)
	}
}
