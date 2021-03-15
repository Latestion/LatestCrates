package me.latestion.latestcrates.gui.events;

import me.latestion.latestcrates.LatestCrates;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncChat implements Listener {

	private LatestCrates plugin;
	
	public AsyncChat(LatestCrates plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void chat(AsyncPlayerChatEvent event) {
		if (plugin.cRename.containsKey(event.getPlayer())) {
			event.setCancelled(true);
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
	            public void run() {
	    			plugin.cRename.get(event.getPlayer()).rename(event.getMessage());
	    			plugin.cRename.remove(event.getPlayer());
	            }
	        }, 1L);
		}
	}

}
