package io.github.cragz.numberswhatgoup.dal;

import io.github.cragz.numberswhatgoup.PlayerInfo;

public interface IDAL
{
	public void addPlayer(PlayerInfo info);
	public PlayerInfo getPlayerInfo(String name);
}
