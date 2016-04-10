package io.github.cragz.numberswhatgoup;

import io.github.cragz.numberswhatgoup.logging.LoggerFactory;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ThrowAway
{
	// for demonstration only. Extremely hacky and horrible.
	
	public static void playAllSounds(Player player)
	{
		final Player lol = player;
		
		int tickDelay = 0;
		
		for (Sound sound : Sound.values())
		{
			final Sound lol2 = sound;
			
			new BukkitRunnable() {
				@Override
				public void run() {
					lol.playSound(lol.getLocation(), lol2, 1, 1);
					lol.sendMessage("Playing sound: " + lol2.toString());
				}
			}.runTaskLater(Bukkit.getPluginManager().getPlugin("NumbersWhatGoUp"), tickDelay);
			
			tickDelay += 40;
		}
	}
}
