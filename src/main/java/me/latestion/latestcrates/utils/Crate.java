package me.latestion.latestcrates.utils;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import me.latestion.latestcrates.LatestCrates;
import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.TileEntityShulkerBox;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Crate {

	String name; // Crate name
	private LatestCrates plugin;
	
	public List<ItemStack> getCrateItems = new ArrayList<>();
	
	public Crate(LatestCrates plugin, String name) {
		this.plugin = plugin;
		this.name = name;
		loadCrateItems();
	}
	
	public void loadCrateItems() {
		getCrateItems.clear();
		try {
			plugin.data.getConfig().getConfigurationSection("crates." + name + ".items").getKeys(false).forEach(num -> {
				// num is number
				ItemStack add = plugin.data.getConfig().getItemStack("crates." + name + ".items." + num);
				getCrateItems.add(add);
			});
		} catch (Exception e) {
		}
	}
	
	public void setCrateItem(ItemStack item, int set) {
		plugin.data.getConfig().set("crates." + name + ".items." + set, item);
		plugin.data.saveConfig();
	}
	
	public void setCrateItems(List<ItemStack> items) {
		int i = 0;
		try {
			plugin.data.getConfig().set("crates." + name + ".items", null);
			plugin.data.saveConfig();
		} catch (Exception e) {
		}
		for (ItemStack item : items) {
			plugin.data.getConfig().set("crates." + name + ".items." + i, item);
			plugin.data.saveConfig();
			i++;
		}
	}
	
	public List<Location> getLocations() {
		List<Location> send = new ArrayList<Location>();
		try {
			plugin.data.getConfig().getConfigurationSection("shulker").getKeys(false).forEach(loc -> {
				if (plugin.data.getConfig().getString("shulker." + loc + ".crate-name").equalsIgnoreCase(name)) {
					send.add(plugin.util.stringToLoc(loc));
				}
			});
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return send;
	}
	
	public boolean isEmpty() {
		return !plugin.data.getConfig().contains("crates." + name + ".items.0");
	}
	
	public int getPrice() {
		return plugin.data.getConfig().getInt("crates." + name + ".price");
	}
	
	public void setPrice(int price) {
		plugin.data.getConfig().set("crates." + name + ".price", price);
		plugin.data.saveConfig();
		
		// Change the HOLO Price of all shulkers here
		
	}
	
	public int getPlayerCrate(Player player) {
		return plugin.data.getConfig().getInt("data." + player.getUniqueId().toString() + "." + name);
	}
	
	public void setPlayerCrate(Player player, int i) {
		plugin.data.getConfig().set("data." + player.getUniqueId().toString() + "." + name, i);
		plugin.data.saveConfig();
	}
	
	public void purchase(Block block, Player player) {
		Location loc = block.getLocation();
		plugin.inProcess.add(block);
        new SpawnParticles(plugin, loc, 5, Particle.TOTEM); // Spawns the particles
        int i = getRandomItem();
        ItemStack item = getCrateItems.get(i);
        plugin.data.getConfig().set("crates." + name + ".items." + i, null);
        plugin.data.saveConfig();
    	plugin.lootInstance.get(player.getUniqueId()).allItems.add(item);
    	plugin.lootInstance.get(player.getUniqueId()).createInv();
    	player.sendMessage(ChatColor.GOLD + "Do /crateloot, to claim your rewards!");
        String itemname = item.getItemMeta().getDisplayName();
        if (itemname.equals("")) {
        	itemname = item.getType().toString().replace('_', ' ');
        }
		renameHologram(block, player.getName(), item.getAmount(), itemname, 10);
        openShulker(loc, 10, block);
        Location rotateLoc = loc.clone().subtract(0, 1, 0);
        if (!item.getType().isBlock()) {
        	rotateLoc.subtract(0, 0.25, 0);
        }
        armorStandRotateAnimate(block.getLocation(), 10, item, rotateLoc.clone());
        getCrateItems.remove(item);
        loc.getWorld().playSound(loc, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
	}
	
	public Hologram createArmorStand(Location loc) {
		Hologram hologram = HologramsAPI.createHologram(plugin, loc.clone().add(0.5, 2.0, 0.5)); 
		hologram.appendTextLine(ChatColor.BOLD + "" + ChatColor.GOLD + name.substring(0, name.length() - 1));
		hologram.appendTextLine(ChatColor.WHITE + "" + getPrice() + " Dollars");
		plugin.holoInstance.put(loc, hologram);
		return hologram;
	}
	
	private void openShulker(Location loc, int i, Block block) {
		net.minecraft.server.v1_16_R3.World world = ((CraftWorld) loc.getWorld()).getHandle();
        BlockPosition position = new BlockPosition(loc.getX(), loc.getY(), loc.getZ());
        TileEntityShulkerBox tileShulker = (TileEntityShulkerBox) world.getTileEntity(position);
        world.playBlockAction(position, tileShulker.getBlock().getBlock(), 1, 1);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            public void run() {
            	world.playBlockAction(position, tileShulker.getBlock().getBlock(), 1, 0);
            	plugin.inProcess.remove(block);
            }            
        }, i * 20);
	}
	
    private void armorStandRotateAnimate(Location a, int i, ItemStack item, Location loc){
    	ArmorStand as = plugin.asInstance.get(a);
    	as.teleport(loc);
    	as.setVisible(false);
    	prepareArmorStand(as, item);
    	if (!plugin.mani.contains(as)) {
    		plugin.mani.add(as);	
    	}
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @SuppressWarnings("deprecation")
			public void run() {
            	as.setHelmet(new ItemStack(Material.AIR));
            }            
        }, i * 20);
    	plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            loc.setYaw(loc.getYaw() +  4);
            as.teleport(loc);
        }, 0, 1);
    }
    
    private int getRandomItem() {
        Random rand = new Random();
        int i = rand.nextInt(getCrateItems.size());
        return i;
    }
    
    @SuppressWarnings("deprecation")
	private void prepareArmorStand(ArmorStand as, ItemStack item) {
    	as.setVisible(false);
    	as.setMarker(false);
    	as.setBasePlate(false);
    	as.setSmall(true);
    	as.setHelmet(item);
    	as.setInvulnerable(true);
    	as.setAI(false);
    	as.setGravity(false);
    	as.setCollidable(false);
    }
    
    private void renameHologram(Block loc, String pName, int amount, String item, int duration) {
    	Hologram gram = plugin.holoInstance.get(loc.getLocation());
    	gram.clearLines();
    	gram.appendTextLine(ChatColor.BOLD + "" + ChatColor.GOLD + pName);
    	gram.appendTextLine(ChatColor.WHITE + "Has won " + amount + "x " + item + "!");
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            public void run() {
            	/*
            	 * ChatColor.BOLD + "" + ChatColor.GOLD + name.substring(0, name.length() - 1)
            	 * ChatColor.WHITE + "" + getPrice() + " Dollars"
            	 */
            	gram.clearLines();
            	gram.appendTextLine(ChatColor.BOLD + "" + ChatColor.GOLD + name.substring(0, name.length() - 1));
            	gram.appendTextLine(ChatColor.WHITE + "" + getPrice() + " Dollars");
            }            
        }, duration * 20);
    }
    
    public void updateHoloPrice() {
    	plugin.data.getConfig().getConfigurationSection("shulker").getKeys(false).forEach(key -> {
    		Location loc = plugin.util.stringToLoc(key);
        	Hologram gram = plugin.holoInstance.get(loc);
        	gram.clearLines();
        	gram.appendTextLine(ChatColor.BOLD + "" + ChatColor.GOLD + name.substring(0, name.length() - 1));
        	gram.appendTextLine(ChatColor.WHITE + "" + getPrice() + " Dollars");
    	});	
    }

	public void rename(String newName) {

		newName = newName + " ";
		
		Crate crate = new Crate(plugin, newName);

		crate.setCrateItems(this.getCrateItems);
		crate.getCrateItems.clear();
		crate.getCrateItems.addAll(this.getCrateItems);
		
		for (Location loc : getLocations()) {
			plugin.data.getConfig().set("shulker." + plugin.util.locToString(loc) + ".crate-name", newName);
			plugin.data.saveConfig();
		}
		
		for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
			plugin.data.getConfig().set("data." + p.getUniqueId().toString() + "." + name, null);
			plugin.data.saveConfig();			
			plugin.data.getConfig().set("data." + p.getUniqueId().toString() + "." + newName, 0);
			plugin.data.saveConfig();	
		}
		

		
		plugin.data.getConfig().set("crates." + name, null);
		plugin.data.saveConfig();
		
		plugin.crateInstance.put(newName, crate);
		plugin.crateInstance.remove(name);
		
		plugin.removeHolos();
		plugin.removeAs();
		
		plugin.loadHolos();
		plugin.spawnAs();

	}
    
}

/* crates:
 *   cratename:
 *     items:
 *       1:
 *         item
 *       2:
 *         item
 *     price: int
 */

/* 
 * shulker:
 *   location: 
 *     crate-name: name
 */   