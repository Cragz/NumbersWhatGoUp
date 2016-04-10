package io.github.cragz.numberswhatgoup.enums;

// This feels super hacky - better to load these from a config file or DB?

public enum SkillTitle
{
	NEOPHYTE(30.0),
	NOVICE(40.0),
	APPRENTICE(50.0),
	JOURNEYMAN(60.0),
	EXPERT(70.0),
	ADEPT(80.0),
	MASTER(90.0),
	GRANDMASTER(100.0);
	
	private final Double _levelRequirement;
	
	public Double getLevelRequirement()
	{
		return _levelRequirement;
	}
	
	private SkillTitle(Double levelRequirement)
	{
		_levelRequirement = levelRequirement;
	}
}
