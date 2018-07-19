package me.NinetyNine.woolshop.anni;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class KitUtils {

	public static ItemStack[] getLeatherArmor() {
		return getLeatherArmor(null);
	}

	public static ItemStack[] getLeatherArmor(Color color) {
		ItemStack[] stacks = { new ItemStack(Material.LEATHER_BOOTS), new ItemStack(Material.LEATHER_LEGGINGS),
				new ItemStack(Material.LEATHER_CHESTPLATE), new ItemStack(Material.LEATHER_HELMET) };

		for (int i = 0; i < stacks.length; i++)
			stacks[i] = addSoulbound(stacks[i]);

		if (color != null)
			for (int i = 0; i < stacks.length; i++) {
				LeatherArmorMeta meta = (LeatherArmorMeta) stacks[i].getItemMeta();
				meta.setColor(color);
				stacks[i].setItemMeta(meta);
			}

		return stacks;
	}

	public static boolean isSoulbound(ItemStack stack) {
		if (stack == null)
			return false;

		ItemMeta meta = stack.getItemMeta();
		if (meta == null)
			return false;

		List<String> lore = meta.getLore();
		if (lore == null)
			return false;

		return lore.contains(ChatColor.GOLD + "Soulbound");
	}

	public static ItemStack addSoulbound(ItemStack stack) {
		if (stack == null)
			return stack;

		ItemMeta meta = stack.getItemMeta();
		if (meta == null)
			meta = Bukkit.getItemFactory().getItemMeta(stack.getType());

		List<String> lore = meta.getLore();
		if (lore == null)
			lore = new ArrayList<String>();

		lore.add(ChatColor.GOLD + "Soulbound");
		meta.setLore(lore);
		stack.setItemMeta(meta);
		return stack;
	}

	public static ItemStack addEnchant(ItemStack s, Enchantment m, int level) {
		s.addUnsafeEnchantment(m, level);
		return s;
	}

	public static boolean itemHasName(ItemStack stack, String name) {
		if (stack == null) {
			return false;
		}
		ItemMeta meta = stack.getItemMeta();
		if (meta == null) {
			return false;
		}
		if (!meta.hasDisplayName()) {
			return false;
		}
		return meta.getDisplayName().equalsIgnoreCase(name);
	}

	public static ItemStack setName(ItemStack itemStack, String name) {
		ItemMeta meta = itemStack.getItemMeta();
		if (meta == null) {
			meta = Bukkit.getItemFactory().getItemMeta(itemStack.getType());
		}
		meta.setDisplayName(name);
		itemStack.setItemMeta(meta);
		return itemStack;
	}

	public static ItemStack setLore(ItemStack itemStack, List<String> lore) {
		ItemMeta meta = itemStack.getItemMeta();
		if (meta == null) {
			meta = Bukkit.getItemFactory().getItemMeta(itemStack.getType());
		}
		meta.setLore(lore);
		itemStack.setItemMeta(meta);
		return itemStack;
	}

	public static ItemStack setNameLore(ItemStack itemStack, String name, List<String> lore) {
		return setLore(setName(itemStack, name), lore);
	}
}
