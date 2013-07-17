package net.fuzzyblocks.animalguard;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class VersionCheck implements Listener{
	public static AnimalGuard plugin;
	
	public VersionCheck(AnimalGuard instance) {
		VersionCheck.plugin = instance;
	}

	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event){
		Player player = event.getPlayer();
		if(plugin.getConfig().getBoolean("notify-outdated") == true){
		if(plugin.outdated == true){
			if(player.isOp() || player.hasPermission("animalguard.admin")){
				player.sendMessage(plugin.fail + "Is out of date, Latest Version: " + plugin.mlversion);
			}
		
		}
	}
	}

}
