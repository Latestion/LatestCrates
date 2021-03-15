package me.latestion.latestcrates.commands;

import me.latestion.latestcrates.LatestCrates;
import me.latestion.latestcrates.gui.AllCrates;
import me.latestion.latestcrates.utils.Crate;
import me.latestion.latestcrates.utils.CreateCrate;
import me.latestion.latestcrates.utils.RefillInv;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class Executor implements CommandExecutor {

	private LatestCrates plugin;
	
	public Executor(LatestCrates plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (sender instanceof Player) {
			Player player = (Player) sender;
			
			if (label.equalsIgnoreCase("crate")) {

				if (args.length == 0) {
					player.sendMessage(ChatColor.RED + "/crate create {cratename}" + ChatColor.WHITE + ": Creates the desierd crate!");
					player.sendMessage(ChatColor.RED + "/crate set {cratename}" + ChatColor.WHITE + ": Set the shulker box to crate!");
					player.sendMessage(ChatColor.RED + "/crate setprice {cratename}" + ChatColor.WHITE + ": Sets the price of crate!");
					player.sendMessage(ChatColor.RED + "/crate refill {cratename}" + ChatColor.WHITE + ": Refills the crate!");
					return false;
				}
				
				if (args[0].equalsIgnoreCase("create")) {
					// Create a new crate
					if (sender.hasPermission("crate.create")) {
						if (args.length == 1) {
							return false;
						}
						String name = stringBuilder(args, 1);
						
						if (plugin.isBeingCreated.contains(name)) {
							return false;
						}
						
						if (plugin.util.isCrate(name)) {
							return false;
						}
						
						CreateCrate crate = new CreateCrate(plugin, name, player);
						player.openInventory(crate.getInventory());
						plugin.newCrates.put(player, crate);
						plugin.isCreating.add(player);
						plugin.isBeingCreated.add(name);
					
					}
				}
				
				if (args[0].equalsIgnoreCase("set")) {
					// Set it to shulker box here
					if (player.hasPermission("crate.set")) {
						Block block = player.getTargetBlockExact(5);	
						if (block == null) {
							return false;
						}	
						if (args.length == 1) {
							return false;
						}
						if (!plugin.util.isShulkerBox(block.getType())) return false;
						String name = stringBuilder(args, 1);
						
						if (!plugin.util.isCrate(name)) {
							return false;
						}
					
						plugin.data.getConfig().set("shulker." + plugin.util.locToString(block.getLocation().clone()) + ".crate-name", name);
						plugin.data.saveConfig();
						Crate crate = plugin.crateInstance.get(name);
						crate.createArmorStand(block.getLocation());
						
						Location loc = block.getLocation();
						ArmorStand as = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
						plugin.prepareArmorStand(as);
						plugin.asInstance.put(loc, as);
					}
				}
				
				if (args[0].equalsIgnoreCase("setprice")) {
					// Set the price of crate with the crate name and eco plugin will be used
					if (player.hasPermission("crate.setprice")) {
						if (args.length < 2) {
							return false;
						}	
						String name = stringBuilder(args, 1, args.length - 1);
						
						if (!plugin.util.isCrate(name)) {
							return false;
						}
						
						if (!plugin.util.isNum(args[args.length - 1])) {
							return false;
						}
						
						Crate crate = plugin.crateInstance.get(name);
						crate.setPrice(plugin.util.parseInt(args[args.length - 1]));
						sender.sendMessage(ChatColor.GOLD + name + " price set to " + plugin.util.parseInt(args[args.length - 1]));
					}
				}
				
				if (args[0].equalsIgnoreCase("give")) {
					if (sender.hasPermission("crate.give")) {
						if (args.length == 1 || args.length == 2) {
							return false;
						}
						try {
							Player target = Bukkit.getPlayer(plugin.idInstance.get(args[1]));
							String name = stringBuilder(args, 2, args.length - 1);
							int i = plugin.util.parseInt(args[args.length - 1]);
							if (target.isOnline()) target.sendMessage(ChatColor.GOLD + "You recieved " + i + " " + name + "keys.");
							int current = plugin.data.getConfig().getInt("data." + target.getUniqueId().toString() + "." + name);
							plugin.data.getConfig().set("data." + target.getUniqueId().toString() + "." + name, (current + i));
							plugin.data.saveConfig();
						} catch (Exception e) {
							return false;
						}		
					}
				}
				
				if (args[0].equalsIgnoreCase("refill")) {
					
					if (player.hasPermission("crate.refill"))  {
						String name = stringBuilder(args, 1);
						
						if (!plugin.util.isCrate(name)) {
							return false;
						}
						RefillInv inv = new RefillInv(plugin, name, player);
						player.openInventory(inv.getInventory());
						plugin.refillInv.put(player, inv);
					}
				}
				
				if (args[0].equalsIgnoreCase("remove")) {
					
					if (player.hasPermission("crate.remove")) {
						if (args.length == 1) {
							return false;
						}
						// REMOVE LOCATION WITH THE CRATES TOO
						// REMOVE THIS CRATE FROM ALL HASHMAPS
						String name = stringBuilder(args, 1);
						player.sendMessage(ChatColor.RED + name + " was removed!");
						
						for (Location loc : plugin.crateInstance.get(name).getLocations()) {
							plugin.data.getConfig().set("shulker." + plugin.util.locToString(loc), null);
							plugin.data.saveConfig();
						}
						for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
							plugin.data.getConfig().set("data." + p.getUniqueId().toString() + "." + name, null);
							plugin.data.saveConfig();			
						}
						
						plugin.removeHolos();
						plugin.removeAs();
						
						plugin.loadHolos();
						plugin.spawnAs();
						
						plugin.data.getConfig().set("crates." + name, null);
						plugin.data.saveConfig();
						plugin.crateInstance.remove(name);
					}
				}
				
				if (args[0].equalsIgnoreCase("rename")) {
					
				}
				
			}	
			if (label.equalsIgnoreCase("crateloot")) {
				player.openInventory(plugin.lootInstance.get(player.getUniqueId()).getInventory());
			}
			if (label.equalsIgnoreCase("cratesgui")) {
				if (sender.hasPermission("crates.all")) {
					plugin.gui = new AllCrates();
					player.openInventory(plugin.gui.invs.get(0));
				}
			}
		}
		else {
			if (args[0].equalsIgnoreCase("setprice")) {
				// Set the price of crate with the crate name and eco plugin will be used
				if (sender.hasPermission("crate.setprice")) {
					if (args.length < 2) {
						return false;
					}	
					String name = stringBuilder(args, 1, args.length - 1);
					
					if (!plugin.util.isCrate(name)) {
						return false;
					}
					
					if (!plugin.util.isNum(args[args.length - 1])) {
						return false;
					}
					
					Crate crate = plugin.crateInstance.get(name);
					crate.setPrice(plugin.util.parseInt(args[args.length - 1]));
				}
			}
			if (args[0].equalsIgnoreCase("give")) {
				if (sender.hasPermission("crate.give")) {
					if (args.length == 1 || args.length == 2) {
						return false;
					}
					try {
						Player target = Bukkit.getPlayer(plugin.idInstance.get(args[1]));
						String name = stringBuilder(args, 2, args.length - 1);
						int i = plugin.util.parseInt(args[args.length - 1]);
						int current = plugin.data.getConfig().getInt("data." + target.getUniqueId().toString() + "." + name);
						plugin.data.getConfig().set("data." + target.getUniqueId().toString() + "." + name, (current + i));
						plugin.data.saveConfig();
					} catch (Exception e) {
						return false;
					}
					
				}
			}
			if (args[0].equalsIgnoreCase("remove")) {
				
				if (sender.hasPermission("crate.remove")) {
					if (args.length == 1) {
						return false;
					}
					// REMOVE LOCATION WITH THE CRATES TOO
					// REMOVE THIS CRATE FROM ALL HASHMAPS
					String name = stringBuilder(args, 1);
					sender.sendMessage(ChatColor.RED + name + " was removed!");
					
					for (Location loc : plugin.crateInstance.get(name).getLocations()) {
						plugin.data.getConfig().set("shulker." + plugin.util.locToString(loc), null);
						plugin.data.saveConfig();
					}
					
					for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
						plugin.data.getConfig().set("data." + p.getUniqueId().toString() + "." + name, null);
						plugin.data.saveConfig();			
					}
					
					plugin.removeHolos();
					plugin.removeAs();
					
					plugin.loadHolos();
					plugin.spawnAs();
					
					plugin.data.getConfig().set("crates." + name, null);
					plugin.data.saveConfig();	
					plugin.crateInstance.remove(name);
				}
			}
		}
		return false;
	}
	
	public String stringBuilder(String[] args, int start) {
		StringBuffer sb = new StringBuffer();
      	for(int i = start; i < args.length; i++) {
      		sb.append(args[i] + " ");
      	}
      	return ChatColor.stripColor(sb.toString());
	}
	
	public String stringBuilder(String[] args, int start, int end) {
		StringBuffer sb = new StringBuffer();
      	for(int i = start; i < end; i++) { 	
      		sb.append(args[i] + " ");
      	}
      	return ChatColor.stripColor(sb.toString());
	}
}
