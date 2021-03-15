package me.latestion.latestcrates.events;

import me.latestion.latestcrates.LatestCrates;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;

public class AsInteract implements Listener {

	private LatestCrates plugin;
	
	public AsInteract(LatestCrates plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void asInteract(PlayerArmorStandManipulateEvent event) {
		if (plugin.mani.contains(event.getRightClicked())) {
			event.setCancelled(true);
		}
	}
	
}
