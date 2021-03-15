package me.latestion.latestcrates.gui;

import java.util.ArrayList;
import java.util.List;

import me.latestion.latestcrates.utils.CrateAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AllCrates {

	public List<Inventory> invs = new ArrayList<>();
	
	public AllCrates() {
		crateGUI();
	}
	
	public Inventory getInventory() {
		return invs.get(0);
	}

	private void crateGUI() {
		Inventory inv = Bukkit.createInventory(null, 54, ChatColor.GOLD + "Crates");
		setClose(inv);
		setArrow(inv);
		int i = 0;
		for (String s : CrateAPI.getAllCrates()) {
			inv.setItem(i, createItem(s));
			i++;
			if (i == 45) {
				Inventory set;
				set = inv;
				invs.add(set);
				inv = Bukkit.createInventory(null, 54, ChatColor.GOLD + "Crates");
				setClose(inv);
				setArrow(inv);
				i = 0;
			}
		}
		if (!invs.contains(inv)) invs.add(inv);
	}

	
	private ItemStack createItem(String s) {
		ItemStack item = new ItemStack(Material.CHEST);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(format(s));
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.RED + "Is empty: " + CrateAPI.isCrateEmpty(s));
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	private String format(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
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

	public ItemStack createCrateItem() {
		ItemStack item = new ItemStack(Material.CHEST_MINECART);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "Create New Crate");
		item.setItemMeta(meta);
		return item;
	}
	
}
