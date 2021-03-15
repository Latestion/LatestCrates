package me.latestion.latestcrates.utils;

import me.latestion.latestcrates.LatestCrates;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Util {

	private LatestCrates plugin;
	
	public Util(LatestCrates plugin) {
		this.plugin = plugin;
	}
	
	public String locToString(Location loc) {
		return (loc.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ());
	}
	
	public Location stringToLoc(String loc) {
		String[] split = loc.split(",");
		Location send = new Location(Bukkit.getWorld(split[0]), parseInt(split[1]), parseInt(split[2]), parseInt(split[3]));
		return send;
	}
	
	public int parseInt(String i) {
		return Integer.parseInt(i);
	}
	
	public boolean isCrate(String name) {
		return (plugin.crateInstance.containsKey(name));
	}
	
	public boolean isNum(String i) {
		try {
			Integer.parseInt(i);
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}
	
	public int getCratePrice(String name) {
		return plugin.data.getConfig().getInt("crates." + name + ".price");
	}
	
	public boolean isInventoryFull(Player player) {
		return player.getInventory().firstEmpty() == -1;
	}
	
	public boolean isShulkerBox(Material m) {
		switch (m) {
			case SHULKER_BOX:
			case LIGHT_GRAY_SHULKER_BOX:
			case BLACK_SHULKER_BOX:
			case BLUE_SHULKER_BOX:
			case BROWN_SHULKER_BOX:
			case CYAN_SHULKER_BOX:
			case GRAY_SHULKER_BOX:
			case GREEN_SHULKER_BOX:
			case LIGHT_BLUE_SHULKER_BOX:
			case LIME_SHULKER_BOX:
			case MAGENTA_SHULKER_BOX:
			case ORANGE_SHULKER_BOX:
			case PINK_SHULKER_BOX:
			case PURPLE_SHULKER_BOX:
			case RED_SHULKER_BOX:
			case WHITE_SHULKER_BOX:
			case YELLOW_SHULKER_BOX:
				return true;
			default:
				return false;
		}
	}
    
    public boolean isLocationTaken(Location loc) {
    	List<Location> test = new ArrayList<>();
    	try {
			plugin.data.getConfig().getConfigurationSection("shulker").getKeys(false).forEach(key -> {
				test.add(this.stringToLoc(key));
			});
			if (test.contains(loc)) {
				return true;
			}
			else {
				return false;
			}
		} catch (Exception e) {

		}
    	return false;
    }
    
}
