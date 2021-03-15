package me.latestion.latestcrates.utils;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.UserDoesNotExistException;
import me.latestion.latestcrates.LatestCrates;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrateInv {

	private Inventory inv;
	private String name;
	private LatestCrates plugin;
	private UUID id;
	
	int x = 0;
	int price = 0;
	
	public CrateInv(LatestCrates plugin, UUID id, String name) {
		this.name = name;
		this.plugin = plugin;
		this.id = id;
		price = plugin.util.getCratePrice(name);
		setX();
		createInv();
	}
	
	public Inventory getInventory() {
		return inv;
	}
	
	private void createInv() {
		inv = Bukkit.createInventory(null, 27, name);
		Crate crate = plugin.crateInstance.get(name);
		int total = crate.getPlayerCrate(Bukkit.getPlayer(id));
		inv.setItem(11, iron(total));
		if (total > 64) {
			inv.setItem(11, iron(64));
		}
		inv.setItem(15, gold());
	}
	
	private ItemStack iron(int i) {
		if (i == 0) {
			i = 1;
		}
		ItemStack item = new ItemStack(Material.IRON_NUGGET, i);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.WHITE + "Pay with purchased crates");
		item.setItemMeta(meta);
		return item;
	}
	
	public void setX() {
		if (price == 0) return;
		try {
			BigDecimal i = Economy.getMoneyExact(id);
			x = i.intValue() / price;
		} catch (UserDoesNotExistException e) {

		}
		
	}
	
	private ItemStack gold() {
		ItemStack item = new ItemStack(Material.GOLD_NUGGET);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "Pay with dollars");
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.GOLD + "You can buy " + x + " crates with dollars");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
}
