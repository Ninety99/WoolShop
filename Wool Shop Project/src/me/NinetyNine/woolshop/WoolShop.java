package me.NinetyNine.woolshop;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import me.NinetyNine.woolshop.listeners.ShopClick;
import me.NinetyNine.woolshop.listeners.SignChange;
import me.NinetyNine.woolshop.listeners.WoolBreak;
import me.NinetyNine.woolshop.listeners.WoolPlace;
import me.NinetyNine.woolshop.utils.WoolConfig;

public class WoolShop extends JavaPlugin {

	@Getter
	private static WoolShop instance;
	
	@Override
	public void onEnable() {
		instance = this;
		
		registerListeners();
		WoolConfig.loadConfig();
		Bukkit.getServer().getLogger().info("Enabled");
	}
	
	@Override
	public void onDisable() {
		
	}
	
	private void registerListeners() {
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(new ShopClick(), this);
		pm.registerEvents(new WoolConfig(), this);
		pm.registerEvents(new SignChange(), this);
		pm.registerEvents(new WoolBreak(), this);
		pm.registerEvents(new WoolPlace(), this);
	}
}
