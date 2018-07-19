package me.NinetyNine.woolshop.listeners;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.NinetyNine.woolshop.utils.WoolConfig;

public class ShopClick implements Listener {

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;

		if (!(e.getClickedBlock().getState() instanceof Sign))
			return;
		else {
			Sign sign = (Sign) e.getClickedBlock().getState();
			if (sign.getLine(1).equals(ChatColor.LIGHT_PURPLE + "[Shop]") && sign.getLine(2).equals("Wool")) {
				Player player = e.getPlayer();
				openInventory(player);
			} else
				return;
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (e.getCurrentItem() == null)
			return;

		if (!(e.getWhoClicked() instanceof Player))
			return;

		e.setCancelled(true);
		Player player = (Player) e.getWhoClicked();

		if (!hasItem(player))
			return;
		else {
			if (e.getCurrentItem() == WoolConfig.getItem()) {
				removeItem(player);
				if (e.getCurrentItem().getAmount() == 1) {
					addItem(player, 1);
					player.sendMessage(ChatColor.GREEN + "Successfully purchased 1x Wool");
					return;
				} else if (e.getCurrentItem().getAmount() == 8) {
					addItem(player, 8);
					player.sendMessage(ChatColor.GREEN + "Successfully purchased 8x Wool");
					return;
				} else if (e.getCurrentItem().getAmount() == 16) {
					addItem(player, 16);
					player.sendMessage(ChatColor.GREEN + "Successfully purchased 16x Wool");
					return;
				} else if (e.getCurrentItem().getAmount() == 32) {
					addItem(player, 32);
					player.sendMessage(ChatColor.GREEN + "Successfully purchased 32x Wool");
					return;
				} else if (e.getCurrentItem().getAmount() == 64) {
					addItem(player, 64);
					player.sendMessage(ChatColor.GREEN + "Successfully purchased 64x Wool");
					return;
				}
				return;
			} else
				return;
		}
	}

	public void addItem(Player player, int amount) {
		ItemStack wool = new ItemStack(Material.WOOL);
		ItemMeta meta = wool.getItemMeta();
		wool.setItemMeta(meta);
		wool.setAmount(amount);

		player.getInventory().addItem(wool);
	}

	private void removeItem(Player player) {
		player.getInventory().removeItem(WoolConfig.getItem());
		for (int i = 0; i < player.getInventory().getSize(); i++)
			if (player.getInventory().getItem(i) == WoolConfig.getItem()) {
				player.getInventory().removeItem(WoolConfig.getItem());
				player.getInventory().getItem(i).setAmount(0);
			}
	}

	private boolean hasItem(Player player) {
		if (player.getInventory().contains(WoolConfig.getItem())) {
			if (WoolConfig.getItem().getAmount() == WoolConfig.getInt("price"))
				return true;
			else
				return false;
		} else
			return false;
	}

	private void openInventory(Player player) {
		Inventory sign = Bukkit.createInventory(null, 27, ChatColor.GRAY + "Wool Shop");

		createWool(sign, 0, ChatColor.GOLD + "Wool", Arrays.asList(" ",
				ChatColor.GREEN + "Cost: " + WoolConfig.getIntAsString("price") + " " + WoolConfig.getString("type")),
				1);

		createWool(sign, 1, ChatColor.GOLD + "Wool", Arrays.asList(" ",
				ChatColor.GREEN + "Cost: " + WoolConfig.getIntAsString("price8") + " " + WoolConfig.getString("type")),
				8);

		createWool(sign, 2, ChatColor.GOLD + "Wool", Arrays.asList(" ",
				ChatColor.GREEN + "Cost: " + WoolConfig.getIntAsString("price16") + " " + WoolConfig.getString("type")),
				16);

		createWool(sign, 3, ChatColor.GOLD + "Wool", Arrays.asList(" ",
				ChatColor.GREEN + "Cost: " + WoolConfig.getIntAsString("price32") + " " + WoolConfig.getString("type")),
				32);

		createWool(sign, 4, ChatColor.GOLD + "Wool", Arrays.asList(" ",
				ChatColor.GREEN + "Cost: " + WoolConfig.getIntAsString("price32") + " " + WoolConfig.getString("type")),
				32);
	}

	private ItemStack createWool(Inventory inventory, int slot, String displayName, List<String> lore, int amount) {

		ItemStack item = new ItemStack(Material.WOOL);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(displayName);
		meta.setLore(lore);
		item.setItemMeta(meta);
		item.setAmount(amount);

		inventory.setItem(slot, item);

		return item;
	}
}
