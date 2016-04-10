package io.github.cragz.numberswhatgoup;

import io.github.cragz.numberswhatgoup.enums.SkillType;

import java.util.HashMap;

public class PlayerInfo
{
	private String _playerName;
	private Integer _karma;
	private Integer _fame;
	private HashMap<SkillType, Double> _levels;
	private HashMap<SkillType, Long> _lastGains;

	public String getPlayerName()
	{
		return _playerName;
	}

	public Integer getPlayerFame()
	{
		return _fame;
	}

	public Integer getPlayerKarma()
	{
		return _karma;
	}

	public HashMap<SkillType, Double> getLevels()
	{
		return _levels;
	}

	public PlayerInfo(String name)
	{
		_playerName = name;

		_levels = new HashMap<SkillType, Double>();
		_lastGains = new HashMap<SkillType, Long>();
	}
	
	public PlayerInfo(String name, Integer fame, Integer karma)
	{
		_playerName = name;
		_fame = fame;
		_karma = karma;

		_levels = new HashMap<SkillType, Double>();
		_lastGains = new HashMap<SkillType, Long>();
	}

	public Double getSkillLevel(SkillType type)
	{
		return _levels.containsKey(type) ? _levels.get(type) : 0.0;
	}

	public void setSkillLevel(SkillType type, Double value)
	{
		_levels.put(type, value);
	}

	public Double getTotalSkillPoints()
	{
		Double skillPoints = 0.0;

		for (Double skillLevel : _levels.values())
		{
			skillPoints += skillLevel;
		}

		return skillPoints;
	}

	public void addSkillPoints(SkillType type, Double value)
	{
		value = Utils.round(value, 1);

		if (_levels.containsKey(type))
		{
			_levels.put(type, _levels.get(type) + value);
		}
		else
		{
			setSkillLevel(type, value);
		}

		_lastGains.put(type, System.currentTimeMillis());
	}

	public Long getTimeSinceLastGain(SkillType type)
	{
		// Default to an hour as the maximum.

		return _lastGains.containsKey(type) ? (System.currentTimeMillis() - _lastGains.get(type)) : 3600000;
	}
}
