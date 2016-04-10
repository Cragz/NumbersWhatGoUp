package io.github.cragz.numberswhatgoup.skills;

public class Competency
{
	private String _name;
	private String _practitionerName;
	private Double _levelRequirement;
	
	public Competency(String name, String practitionerName, Double levelRequirement)
	{
		_name = name;
		_practitionerName = practitionerName;
		_levelRequirement = levelRequirement;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public String getPractitionerName()
	{
		return _practitionerName;
	}
	
	public Double getLevelRequirement()
	{
		return _levelRequirement;
	}
}
