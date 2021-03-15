package me.latestion.latestcrates.gui.events;

import me.latestion.latestcrates.LatestCrates;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryClose implements Listener {

	private final LatestCrates plugin;
	
	public InventoryClose(LatestCrates plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		if (plugin.cGUI.containsKey(event.getPlayer())) {
			if (plugin.cGUI.get(event.getPlayer()).inv.equals(event.getInventory())) {
				if (!plugin.cache2.contains(event.getPlayer())) {
			        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			            public void run() {
			            	event.getPlayer().openInventory(event.getInventory());
			            }            
			        }, 5);
			        return;
				}
				plugin.cache2.remove(event.getPlayer());
				plugin.cGUI.remove(event.getPlayer());
			}
		}
		if (plugin.sInv.containsKey(event.getPlayer())) {
			if (plugin.sInv.get(event.getPlayer()).invs.contains(event.getInventory())) {
				if (!plugin.cache2.contains(event.getPlayer())) {
			        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			            public void run() {
			            	event.getPlayer().openInventory(event.getInventory());
			            }            
			        }, 5);
			        return;
				}
				plugin.cache2.remove(event.getPlayer());
				plugin.sInv.remove(event.getPlayer());
			}
		}
	}
}
