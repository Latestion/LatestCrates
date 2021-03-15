package me.latestion.latestcrates;

import com.earth2me.essentials.api.Economy;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import me.latestion.latestcrates.commands.Executor;
import me.latestion.latestcrates.events.*;
import me.latestion.latestcrates.files.CrateLootManager;
import me.latestion.latestcrates.files.DataManager;
import me.latestion.latestcrates.gui.AllCrates;
import me.latestion.latestcrates.gui.CrateGUI;
import me.latestion.latestcrates.gui.CrateRename;
import me.latestion.latestcrates.gui.ShulkInv;
import me.latestion.latestcrates.gui.events.AsyncChat;
import me.latestion.latestcrates.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class LatestCrates extends JavaPlugin {

    public Economy eco;
    public DataManager data;
    public CrateLootManager loot;
    public Util util;

    public static LatestCrates instance;

    public Map<String, Crate> crateInstance = new HashMap<>();
    public Map<UUID, CrateLoot> lootInstance = new HashMap<>();

    public Map<String, UUID> idInstance = new HashMap<>();
    public Map<Player, CreateCrate> newCrates = new HashMap<>(); // NEW CRATES DONE EVERYTHING BY CLASS
    public Map<UUID, Inventory> shulkerInv = new HashMap<>();

    public Map<Player, Location> inCrate = new HashMap<>();
    public List<Player> isCreating = new ArrayList<>();
    public List<String> isBeingCreated = new ArrayList<>();
    public List<Block> inProcess = new ArrayList<>();
    public List<Player> cache = new ArrayList<>();

    public Map<Location, Hologram> holoInstance = new HashMap<>();
    public Map<Location, ArmorStand> asInstance = new HashMap<>();
    public List<ArmorStand> mani = new ArrayList<>();
    public Map<Player, RefillInv> refillInv = new HashMap<>();

    public AllCrates gui;

    public List<Player> cache2 = new ArrayList<>();

    public Map<Player, CrateGUI> cGUI = new HashMap<>();
    public Map<Player, ShulkInv> sInv = new HashMap<>();
    public Map<Player, CrateRename> cRename = new HashMap<>();


    @Override
    public void onEnable() {
        instance = this;
        if (!setupEconomy()) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        loadFiles();
        loadCrates();
        loadEvents();
        holo();
        loadHolos();
        spawnAs();
    }

    @Override
    public void onDisable() {
        removeHolos();
        removeAs();
        for (Crate c : crateInstance.values()) {
            c.setCrateItems(c.getCrateItems);
        }
        setLoot();
    }

    private void loadCrates() {
        try {
            data.getConfig().getConfigurationSection("crates").getKeys(false).forEach(key -> {
                crateInstance.put(key, new Crate(this, key));
            });
        } catch (Exception e) {

        }
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economy = getServer().getServicesManager().getRegistration
                (com.earth2me.essentials.api.Economy.class);
        if (economy != null) {
            eco = economy.getProvider();
        }
        return (eco == null);
    }

    public void holo() {
        if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
            getLogger().severe("*** HolographicDisplays is not installed or not enabled. ***");
            getLogger().severe("*** This plugin will be disabled. ***");
            this.setEnabled(false);
            return;
        }
    }

    private void loadFiles() {
        this.data = new DataManager(this);
        this.loot = new CrateLootManager(this);
        this.util = new Util(this);
        Executor exe = new Executor(this);
        this.getCommand("crate").setExecutor(exe);
        this.getCommand("crateloot").setExecutor(exe);
        this.getCommand("cratesgui").setExecutor(exe);
    }

    private void loadEvents() {
        PluginManager manager = this.getServer().getPluginManager();
        manager.registerEvents(new InventoryClick(this), this);
        manager.registerEvents(new InventoryClose(this), this);
        manager.registerEvents(new InventoryOpen(this), this);
        manager.registerEvents(new PlayerJoin(this), this);
        manager.registerEvents(new AsInteract(this), this);
        manager.registerEvents(new AsyncChat(this), this);
        manager.registerEvents(new me.latestion.latestcrates.gui.events.InventoryClick(this), this);
        manager.registerEvents(new me.latestion.latestcrates.gui.events.InventoryClose(this), this);
    }

    public void loadHolos() {
        try {
            data.getConfig().getConfigurationSection("shulker").getKeys(false).forEach(key -> {
                Location loc = util.stringToLoc(key);
                String name = data.getConfig().getString("shulker." + key + ".crate-name");
                Crate crate = crateInstance.get(name);
                crate.createArmorStand(loc);
            });
        } catch (Exception e) {

        }
    }

    public void removeHolos() {
        for (Hologram gram : HologramsAPI.getHolograms(this)) {
            gram.delete();
        }
    }

    public void spawnAs() {
        asInstance.clear();
        try {
            data.getConfig().getConfigurationSection("shulker").getKeys(false).forEach(key -> {
                Location loc = util.stringToLoc(key);
                ArmorStand as = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
                prepareArmorStand(as);
                asInstance.put(loc, as);
            });
        } catch (Exception e) {
        }
    }

    public void removeAs() {
        for (ArmorStand as : asInstance.values()) {
            as.remove();
        }
        asInstance.clear();
    }

    private void setLoot() {
        for (CrateLoot l : lootInstance.values()) {
            l.setCrateItems();
        }
    }

    public void prepareArmorStand(ArmorStand as) {
        as.setVisible(false);
        as.setMarker(false);
        as.setBasePlate(false);
        as.setSmall(true);
        as.setInvulnerable(true);
        as.setAI(false);
        as.setGravity(false);
        as.setCollidable(false);
    }

}
