package me.latestion.latestcrates.gui.events;

import me.latestion.latestcrates.LatestCrates;
import me.latestion.latestcrates.gui.CrateGUI;
import me.latestion.latestcrates.gui.CrateRename;
import me.latestion.latestcrates.gui.ShulkInv;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class InventoryClick implements Listener {

	private LatestCrates plugin;
	
	public InventoryClick(LatestCrates plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if (plugin.gui.invs.contains(event.getClickedInventory())) {
			if (event.getClickedInventory().getType() == InventoryType.PLAYER) {
				event.setCancelled(true);
				return;
			}
			if (event.getCurrentItem() == null) return;
		
			if (event.getSlot() == 49) {
				player.closeInventory();
				return;
			}
			
			if (event.getSlot() == 48) {
				if (event.getClickedInventory().equals(plugin.gui.getInventory())) {
					event.setCancelled(true);
					return;
				}
				player.openInventory(plugin.gui.invs.get(plugin.gui.invs.indexOf(event.getInventory()) - 1));
				return;
			}
			
			if (event.getSlot() == 50) {
				int index = plugin.gui.invs.indexOf(event.getInventory());
				int size = plugin.gui.invs.size() - 1;
				if (size > index) {
					player.openInventory(plugin.gui.invs.get(index + 1));
				}
				return;
			}
			
			if (event.getSlot() < 45) {
				String name = event.getCurrentItem().getItemMeta().getDisplayName();
				CrateGUI gui = new CrateGUI(name);
				player.openInventory(gui.inv);
				plugin.cGUI.put(player, gui);
			}
		}
		if (plugin.cGUI.containsKey(player)) {
			if (plugin.cGUI.get(player).inv.equals(event.getClickedInventory())) {
				String name = plugin.cGUI.get(player).name;
				if (event.getClickedInventory().getType() == InventoryType.PLAYER) {
					event.setCancelled(true);
					return;
				}
				if (event.getCurrentItem() == null) return;
			
				if (event.getSlot() == 0) {
					plugin.cache2.add(player);
					player.closeInventory();
					ShulkInv inv = new ShulkInv(name);
					player.openInventory(inv.invs.get(0));
					plugin.sInv.put(player, inv);
				}
				
				if (event.getSlot() == 2) {
					/*
					 * Rename
					 * 
					 * A LOT OF NESTING AND TESTING
					 *  
					 */
					plugin.cache2.add(player);
					player.closeInventory();
					player.sendMessage("Enter the new name for the crate!");
					CrateRename rname = new CrateRename(player, name);
					plugin.cRename.put(player, rname);
				}
				
				if (event.getSlot() == 4) {
					plugin.cache2.add(player);
					player.closeInventory();
				}
				
				if (event.getSlot() == 6) {
					player.performCommand("crate refill " + name);
					plugin.cache2.add(player);
					player.closeInventory();
				}
				
				if (event.getSlot() == 8) {
					player.performCommand("crate remove " + name);
					plugin.cache2.add(player);
					player.closeInventory();
				}	
			}
		}
		if (plugin.sInv.containsKey(player))
		if (plugin.sInv.get(player).invs.contains(event.getClickedInventory())) {
			if (event.getClickedInventory().getType() == InventoryType.PLAYER) {
				event.setCancelled(true);
				return;
			}
			if (event.getCurrentItem() == null) return;
		
			if (event.getSlot() == 49) {
				plugin.cache2.add(player);
				player.closeInventory();
				return;
			}
			
			ShulkInv sInv = plugin.sInv.get(player);
			
			if (event.getSlot() == 48) {
				if (event.getClickedInventory().equals(sInv.getInventory())) {
					event.setCancelled(true);
					return;
				}
				plugin.cache2.add(player);
				player.openInventory(sInv.invs.get(sInv.invs.indexOf(event.getInventory()) - 1));
				return;
			}
			
			if (event.getSlot() == 50) {
				int index = sInv.invs.indexOf(event.getInventory());
				int size = sInv.invs.size() - 1;
				if (size > index) {
					plugin.cache2.add(player);
					player.openInventory(sInv.invs.get(index + 1));
				}
				return;
			}
			
			if (event.getSlot() < 45) {
				ItemStack item = event.getCurrentItem();
				Location loc = stringToLoc(item.getItemMeta().getLore().get(1));
				plugin.cache2.add(player);
				player.closeInventory();
				player.teleport(loc);
			}
		}
	}
	public Location stringToLoc(String loc) {
		String[] split = loc.split(",");
		Location send = new Location(Bukkit.getWorld(split[0]), parseInt(split[1]), parseInt(split[2]), parseInt(split[3]));
		return send;
	}
	public int parseInt(String i) {
		return Integer.parseInt(i);
	}
	
}
