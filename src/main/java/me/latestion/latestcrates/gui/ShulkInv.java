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

public class ShulkInv {

	public List<Inventory> invs = new ArrayList<>();;
	public String name;
	
	public ShulkInv(String name) {
		this.name = name;
		crateShulk();
	}

	private void crateShulk() {
		Inventory inv = Bukkit.createInventory(null, 54, ChatColor.GOLD + "Shulks");
		setClose(inv);
		setArrow(inv);
		int i = 0;
		for (String s : CrateAPI.getCrateShulkers(name)) {
			inv.setItem(i, createItem(s));
			i++;
			if (i == 45) {
				Inventory set;
				set = inv;
				invs.add(set);
				inv = Bukkit.createInventory(null, 54, ChatColor.GOLD + "Crates");
				setClose(inv);
				setArrow(inv);
			}
		}
		if (!invs.contains(inv)) invs.add(inv);
	}

	private ItemStack createItem(String s) {
		ItemStack item = new ItemStack(Material.SHULKER_BOX);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + name + " Crate!");
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.GOLD + "Click to teleport at crate!");
		lore.add(s);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
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

	public Inventory getInventory() {
		return invs.get(0);
	}
	
}
