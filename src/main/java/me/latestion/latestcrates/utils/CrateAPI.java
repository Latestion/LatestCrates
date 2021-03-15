package me.latestion.latestcrates.utils;

import me.latestion.latestcrates.LatestCrates;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CrateAPI {

	public CrateAPI() {
		
	}

	public static Set<String> getAllCrates() {
		return LatestCrates.instance.crateInstance.keySet();
	}
	
	public static List<String> getCrateShulkers(String name) {
		List<String> send = new ArrayList<String>();
		try {
			LatestCrates.instance.data.getConfig().getConfigurationSection("shulker").getKeys(false).forEach(loc -> {
				if (LatestCrates.instance.data.getConfig().getString("shulker." + loc + ".crate-name").equalsIgnoreCase(name)) {
					send.add(loc);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return send;
	}
	
	public static void renameCrate(String name, String newName) {
		LatestCrates.instance.crateInstance.get(name).rename(newName);
	}
	
	public static boolean isCrateEmpty(String name) {
		return !LatestCrates.instance.data.getConfig().contains("crates." + name + ".items.0");
	}
	
}
