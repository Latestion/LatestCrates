package me.latestion.latestcrates.events;

import me.latestion.latestcrates.LatestCrates;
import me.latestion.latestcrates.utils.Crate;
import me.latestion.latestcrates.utils.CrateInv;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class InventoryOpen implements Listener {
	
	private LatestCrates plugin;
	
	public InventoryOpen(LatestCrates plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void invOpen(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (event.getClickedBlock() == null) {
				return;
			}
			Block block = event.getClickedBlock();
			Location loc = event.getClickedBlock().getLocation();
			if (plugin.util.isShulkerBox(event.getClickedBlock().getType())) {
				if (plugin.util.isLocationTaken(loc)) {
					event.setCancelled(true);
					// REAL CRATE
					Player player = event.getPlayer();

					if (plugin.inProcess.contains(block)) {
						return;
					}
					
					String name = plugin.data.getConfig().getString("shulker." + plugin.util.locToString(loc) + ".crate-name");
					Crate crate = plugin.crateInstance.get(name);
					if (crate.getCrateItems.isEmpty()) {
						player.sendMessage(ChatColor.RED + "Crate is empty!");
						return;
					}
					
					UUID id = event.getPlayer().getUniqueId();
					CrateInv inv = new CrateInv(plugin, id, name);
					Inventory invv = inv.getInventory();
					event.getPlayer().openInventory(invv);
					plugin.shulkerInv.put(id, invv);
					plugin.inCrate.put(event.getPlayer(), event.getClickedBlock().getLocation());
				}
			}
		}
	}
}
