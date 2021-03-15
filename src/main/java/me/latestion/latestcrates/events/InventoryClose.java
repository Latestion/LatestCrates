package me.latestion.latestcrates.events;

import me.latestion.latestcrates.LatestCrates;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryClose implements Listener {

	private LatestCrates plugin;
	
	public InventoryClose(LatestCrates plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void invClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		if (plugin.isCreating.contains(event.getPlayer())) {
			if (plugin.cache.contains(event.getPlayer())) {
				plugin.cache.remove(event.getPlayer());
				return;
			}
	        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
	            public void run() {
	            	event.getPlayer().openInventory(event.getInventory());
	            }            
	        }, 10);
		}
		if (plugin.shulkerInv.containsValue(event.getInventory())) {
			plugin.inCrate.remove(event.getPlayer());
		}
		if (plugin.refillInv.containsKey(player))
		if (plugin.refillInv.get(player).invs.contains(event.getInventory())) {
			if (plugin.cache.contains(event.getPlayer())) {
				plugin.cache.remove(event.getPlayer());
				return;
			}
	        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
	            public void run() {
	            	event.getPlayer().openInventory(event.getInventory());
	            }            
	        }, 10);
		}
	}
}
