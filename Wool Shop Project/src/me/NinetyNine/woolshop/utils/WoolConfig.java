package me.NinetyNine.woolshop.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import me.NinetyNine.woolshop.WoolShop;

public class WoolConfig implements Listener {

	public static void saveConfig() {
		WoolShop.getInstance().saveConfig();
	}

	public static FileConfiguration getConfig() {
		return WoolShop.getInstance().getConfig();
	}

	public static void set(String path, Object object) {
		getConfig().set(path, object);
		saveConfig();
	}

	public static void loadConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

	public static String getString(String path) {
		return getConfig().getString(path);
	}

	public static String getIntAsString(String path) {
		if (isInt(path))
			return getString(path);
		else
			return null;
	}

	public static ItemStack getItem() {
		return new ItemStack(getType("type"));
	}

	public static Material getType(String path) {
		return Material.valueOf(getString(path).toUpperCase());
	}

	public static int getInt(String path) {
		return getConfig().getInt(path);
	}

	public static boolean isInt(String string) {
		try {
			Integer.parseInt(string);
			return true;
		} catch (NumberFormatException e) {
			Bukkit.getServer().getLogger().info(string + " is not a valid number!");
			return false;
		}
	}
}