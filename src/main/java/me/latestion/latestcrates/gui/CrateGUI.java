package me.latestion.latestcrates.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CrateGUI {
	
	public Inventory inv;
	
	public String name;
	
	public CrateGUI(String name) {
		this.name = name;
		createInv();
	}

	private void createInv() {
		inv = Bukkit.createInventory(null, 9, ChatColor.GOLD + name);
		inv.setItem(0, shulk());
		inv.setItem(2, rename());
		inv.setItem(4, close());
		inv.setItem(6, reffil());
		inv.setItem(8, remove());
	}
	
	private ItemStack shulk() {
		ItemStack item = new ItemStack(Material.SHULKER_SHELL);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "See Crates");
		item.setItemMeta(meta);
		return item;
	}

	private ItemStack rename() {
		ItemStack item = new ItemStack(Material.PAPER);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "Rename Crate");
		item.setItemMeta(meta);
		return item;
	}
	
	private ItemStack reffil() {
		ItemStack item = new ItemStack(Material.HOPPER);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "Refill Crate");
		item.setItemMeta(meta);
		return item;
	}
	
	private ItemStack close() {
		ItemStack item = new ItemStack(Material.BARRIER);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Close");
		item.setItemMeta(meta);
		return item;
	}
	
	private ItemStack remove() {
		ItemStack item = new ItemStack(Material.LAVA_BUCKET);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "Remove Crate");
		item.setItemMeta(meta);
		return item;
	}
}
