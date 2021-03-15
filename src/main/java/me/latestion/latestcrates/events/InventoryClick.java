package me.latestion.latestcrates.events;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;
import me.latestion.latestcrates.LatestCrates;
import me.latestion.latestcrates.utils.Crate;
import me.latestion.latestcrates.utils.CrateLoot;
import me.latestion.latestcrates.utils.CreateCrate;
import me.latestion.latestcrates.utils.RefillInv;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.math.BigDecimal;

public class InventoryClick implements Listener {

	private LatestCrates plugin;
	
	public InventoryClick(LatestCrates plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void invClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if (plugin.isCreating.contains(event.getWhoClicked())) {
			CreateCrate crate = plugin.newCrates.get(player);
			if (event.getClickedInventory() instanceof PlayerInventory) {
				return;
			}
			if (event.getSlot() < 9 || event.getSlot() > 44) {
				event.setCancelled(true);
			}
			if (event.getSlot() == 49) {
				plugin.isCreating.remove(player);
				plugin.isBeingCreated.remove(event.getView().getTitle());
				plugin.newCrates.remove(player);
				player.closeInventory();
			}
			if (event.getSlot() == 48) {
				if (event.getInventory().equals(crate.getInventory())) {
					event.setCancelled(true);
					return;
				}
				/// Open the inventory index - 1
				plugin.cache.add(player);
				player.openInventory(crate.invs.get(crate.invs.indexOf(event.getInventory()) - 1));
			}
			if (event.getSlot() == 50) {
				if (crate.isInventoryFull(event.getInventory())) {
					plugin.cache.add(player);
					player.openInventory(crate.createNewInv());
				}
			}
			if (event.getSlot() == 4) {
				player.closeInventory();
				crate.createCrate();
				plugin.isCreating.remove(player);
				plugin.isBeingCreated.remove(event.getView().getTitle());
				plugin.newCrates.remove(player);
			}
			return;
		}
		if (plugin.shulkerInv.containsValue(event.getInventory())) {
			String name = event.getView().getTitle();
			if (event.getClickedInventory() instanceof PlayerInventory) {
				event.setCancelled(true);
				return;
			}
			if (event.getCurrentItem() == null) {
				return;
			}
			event.setCancelled(true);
			Block block = player.getLocation().getWorld().getBlockAt(plugin.inCrate.get(player));
			if (event.getSlot() == 15) {
				try {
					BigDecimal i = Economy.getMoneyExact(event.getWhoClicked().getUniqueId());
					int price = plugin.util.getCratePrice(name);
					if (i.doubleValue() > price) {
						Economy.subtract(event.getWhoClicked().getUniqueId(), new BigDecimal(price));
						player.closeInventory();
						Crate crate = plugin.crateInstance.get(name);
						crate.purchase(block, player);
						// RUN THE MECHANICS FOR PARTICLES AND THE ROTATING ITEM AND THE CHANGE THE NAME OF THE ARMOR STAND ABOVE IT
					}
					else {
						event.getWhoClicked().closeInventory();
						event.getWhoClicked().sendMessage(ChatColor.RED + "Not enough funds!");
					}
				} catch (UserDoesNotExistException e) {
				} catch (NoLoanPermittedException e) {
				} catch (ArithmeticException e) {
				}
				event.getWhoClicked().closeInventory();
			}
			if (event.getSlot() == 11) {
				Crate crate = plugin.crateInstance.get(name);
				int currentCrates = crate.getPlayerCrate(player);
				if (currentCrates == 0) {
					event.getWhoClicked().sendMessage(ChatColor.RED + "Not enought crates!");
					event.getWhoClicked().closeInventory();
					return;
				}
				currentCrates--;
				crate.setPlayerCrate(player, currentCrates);
				event.getWhoClicked().closeInventory();
				crate.purchase(block, player);
			}
		}
		if (plugin.refillInv.containsKey(player))
		if (plugin.refillInv.get(player).invs.contains(event.getInventory())) {
			RefillInv crate = plugin.refillInv.get(player);
			if (event.getClickedInventory() instanceof PlayerInventory) {
				return;
			}
			if (event.getSlot() < 9 || event.getSlot() > 44) {
				event.setCancelled(true);
			}
			if (event.getSlot() == 49) {
				plugin.refillInv.remove(player);
				player.closeInventory();
			}
			if (event.getSlot() == 48) {
				if (event.getInventory().equals(crate.getInventory())) {
					event.setCancelled(true);
					return;
				}
				/// Open the inventory index - 1
				plugin.cache.add(player);
				player.openInventory(crate.invs.get(crate.invs.indexOf(event.getInventory()) - 1));
			}
			if (event.getSlot() == 50) {
				if (crate.isInventoryFull(event.getInventory())) {
					plugin.cache.add(player);
					int index = crate.invs.indexOf(event.getInventory());
					int size = crate.invs.size() - 1;
					if (size > index) {
						player.openInventory(crate.invs.get(index + 1));
					}
					else player.openInventory(crate.createNewInv());
				}
			}
			if (event.getSlot() == 4) {
				crate.createCrate();
				plugin.refillInv.remove(player);
				player.closeInventory();
			}
			return;
		}
		if (plugin.lootInstance.containsKey(event.getWhoClicked().getUniqueId()))
		if (plugin.lootInstance.get(event.getWhoClicked().getUniqueId()).invs.contains(event.getInventory())) {
			if (event.getClickedInventory().getType() == InventoryType.PLAYER) {
				event.setCancelled(true);
				return;
			}
			
			if (!event.getView().getTitle().contains(ChatColor.GOLD + "Crate Loot")) return;
			if (event.getCurrentItem() == null) return;
		
			event.setCancelled(true);
			
			CrateLoot loot = plugin.lootInstance.get(player.getUniqueId());
			
			if (event.getSlot() == 49) {
				player.closeInventory();
				return;
			}
			
			if (event.getSlot() == 48) {
				if (event.getClickedInventory().equals(loot.getInventory())) {
					event.setCancelled(true);
					return;
				}
				player.openInventory(loot.invs.get(loot.invs.indexOf(event.getInventory()) - 1));
				return;
			}
			
			if (event.getSlot() == 50) {
				int index = loot.invs.indexOf(event.getInventory());
				int size = loot.invs.size() - 1;
				if (size > index) {
					player.openInventory(loot.invs.get(index + 1));
				}
				return;
			}
			
			if (event.getSlot() == 47) {
				if (plugin.util.isInventoryFull(player)) {
					player.sendMessage(ChatColor.RED + "Your Inventory Is Full!");
					return;
				}
				player.closeInventory();
				for (int i = 0; i < loot.allItems.size() * 100; i++) {
					if (loot.allItems.isEmpty()) break;
					ItemStack item = loot.allItems.remove(0);
					player.getInventory().addItem(item);
					player.updateInventory();
					if (plugin.util.isInventoryFull(player)) {
						player.sendMessage(ChatColor.RED + "Your Inventory Is Full!");
						break;
					}
				}
				loot.createInv();
			}
			
			if (event.getSlot() < 45) {
				if (plugin.util.isInventoryFull(player)) {
					player.sendMessage(ChatColor.RED + "Your Inventory Is Full!");
					return;
				}
				ItemStack item = event.getCurrentItem();
				loot.allItems.remove(item);
				player.closeInventory();
				loot.createInv();
				player.sendMessage(ChatColor.GOLD + "Added " + format(item.getItemMeta().getDisplayName()) + " to your inventory!");
				player.getInventory().addItem(item);
				player.updateInventory();
				return;
			}
		}
	}
	
	@EventHandler
	public void drag(InventoryDragEvent event) {
		if (plugin.lootInstance.get(event.getWhoClicked().getUniqueId()).invs.contains(event.getInventory())) {
			event.setCancelled(true);
		}
	}
	
	private String format(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}	
}
