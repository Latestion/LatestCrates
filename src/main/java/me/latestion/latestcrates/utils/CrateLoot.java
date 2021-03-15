package me.latestion.latestcrates.utils;

import me.latestion.latestcrates.LatestCrates;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrateLoot {

	private LatestCrates plugin;
	public List<Inventory> invs = new ArrayList<Inventory>();
	
	private UUID id;
	
	public List<ItemStack> allItems = new ArrayList<>();
	
	public CrateLoot(LatestCrates plugin, UUID id) {
		this.plugin = plugin;
		this.id = id;
		allItems = getAllItems();
		createInv();
	}
	
	public Inventory getInventory() {
		return invs.get(0);
	}
	
	public void createInv() {
		invs.clear();
		Inventory inv = Bukkit.createInventory(null, 54, ChatColor.GOLD + "Crate Loot");
		setClose(inv);
		setArrow(inv);
		setClaim(inv);
		int i = 0;
		for (ItemStack item : allItems) {
			inv.addItem(item);
			i++;
			if (i == 45) {
				Inventory set;
				set = inv;
				invs.add(set);
				inv = Bukkit.createInventory(null, 54, ChatColor.GOLD + "Crate Loot");
				setClose(inv);
				setArrow(inv);
				setClaim(inv);
			}
		}
		if (!invs.contains(inv)) invs.add(inv);
	}
	
	private void setClose(Inventory inv) {
		ItemStack item = new ItemStack(Material.BARRIER);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Close");
		item.setItemMeta(meta);
		inv.setItem(49, item);
	}
	
	private void setArrow(Inventory inv) {
		ItemStack item = new ItemStack(Material.SPECTRAL_ARROW);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Previous Page");
		item.setItemMeta(meta);
		inv.setItem(48, item);
		
		ItemStack item2 = new ItemStack(Material.SPECTRAL_ARROW);
		ItemMeta meta2 = item2.getItemMeta();
		meta2.setDisplayName(ChatColor.GREEN + "Next Page");
		item2.setItemMeta(meta2);
		inv.setItem(50, item2);
	}
	
	private void setClaim(Inventory inv) {
		ItemStack item = new ItemStack(Material.CHEST);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "Claim All");
		item.setItemMeta(meta);
		inv.setItem(47, item);
	}
	
	private List<ItemStack> getAllItems() {
		List<ItemStack> items = new ArrayList<>();
		try {
			plugin.loot.getConfig().getConfigurationSection("loot." + id.toString() + ".items").getKeys(false).forEach(num -> {
				// num is number
				ItemStack add = plugin.loot.getConfig().getItemStack("loot." + id.toString() + ".items." + num);
				items.add(add);
			});
		} catch (Exception e) {
		}
		return items;
	}
	
	public void setCrateItems() {
		int i = 0;
		plugin.loot.getConfig().set("loot." + id.toString() + ".items", null);
		plugin.loot.saveConfig();
		for (ItemStack item : allItems) {
			plugin.loot.getConfig().set("loot." + id.toString() + ".items." + i, item);
			plugin.loot.saveConfig();
			i++;
		}
	}
	
	
	public Player getPlayer() {
		return Bukkit.getPlayer(id);
	}
}
