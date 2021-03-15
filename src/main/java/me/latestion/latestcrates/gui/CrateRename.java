package me.latestion.latestcrates.gui;

import me.latestion.latestcrates.utils.CrateAPI;
import org.bukkit.entity.Player;

public class CrateRename {

	public Player player;
	public String name;
	
	public CrateRename(Player player, String name) {
		this.player = player;
		this.name = name;
	}
	
	public void rename(String newName) {
		CrateAPI.renameCrate(name, newName);
	}

}
