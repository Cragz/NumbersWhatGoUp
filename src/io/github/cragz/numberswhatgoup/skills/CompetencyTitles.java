package io.github.cragz.numberswhatgoup.skills;

import java.util.LinkedHashMap;

import io.github.cragz.numberswhatgoup.enums.SkillTitle;
import io.github.cragz.numberswhatgoup.logging.LoggerFactory;
import io.github.cragz.numberswhatgoup.skills.Competency;

public final class CompetencyTitles {
	
	// Todo: This is only a linked hashmap so it can be iterated over in order later. Nasty.
	private static LinkedHashMap<SkillTitle, Competency> _titleValues;
	
	public static void loadCompetencyTitlesFromConfig()
	{
		// Temp - doing this locally rn.
		// Load from config ultimately
		
		_titleValues = new LinkedHashMap<SkillTitle, Competency>();
		
		_titleValues.put(SkillTitle.NEOPHYTE, new Competency("Neophyte", "a neophyte", 30.0));
		_titleValues.put(SkillTitle.NOVICE, new Competency("Novice", "a novice", 40.0));
		_titleValues.put(SkillTitle.APPRENTICE, new Competency("Apprentice", "an apprentice", 50.0));
		_titleValues.put(SkillTitle.JOURNEYMAN, new Competency("Journeyman", "a journeyman", 60.0));
		_titleValues.put(SkillTitle.EXPERT, new Competency("Expert", "an expert", 70.0));
		_titleValues.put(SkillTitle.ADEPT, new Competency("Adept", "an adept", 80.0));
		_titleValues.put(SkillTitle.MASTER, new Competency("Master", "a master", 90.0));
		_titleValues.put(SkillTitle.GRANDMASTER, new Competency("Grandmaster", "a grandmaster", 100.0));
	}
	
	public static Double getLevelRequirement(SkillTitle title)
	{
		return _titleValues.containsKey(title) ? _titleValues.get(title).getLevelRequirement() : 100.1;
	}
	
	public static String getPractitionerTitle(SkillTitle title)
	{
		return _titleValues.containsKey(title) ? _titleValues.get(title).getPractitionerName() : "a/an";
	}
	
	public static String getTitle(Double level)
	{
		String highestCompetency = null;
		
		for (Competency competency : _titleValues.values())
		{
			if (level >= competency.getLevelRequirement())
			{
				highestCompetency = competency.getName();
			}
			else
			{
				break;
			}
		}
		
		return highestCompetency;
	}
}
