package me.latestion.latestcrates.events;

import me.latestion.latestcrates.LatestCrates;
import me.latestion.latestcrates.utils.CrateLoot;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

	private LatestCrates plugin;
	public PlayerJoin(LatestCrates plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void pJoin(PlayerJoinEvent event) {
		plugin.idInstance.put(event.getPlayer().getName(), event.getPlayer().getUniqueId());
		if (!plugin.lootInstance.containsKey(event.getPlayer().getUniqueId())) 
			plugin.lootInstance.put(event.getPlayer().getUniqueId(), new CrateLoot(plugin, event.getPlayer().getUniqueId()));
	}
	
}
