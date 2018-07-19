package me.NinetyNine.woolshop.anni;

import java.util.Arrays;

import org.bukkit.inventory.ItemStack;

public class IconPackage {
	private final ItemStack stack;
	private String name = null;
	private String[] lore = null;

	public IconPackage(ItemStack stack) {
		this(stack, null, null);
	}

	public IconPackage(ItemStack stack, String name) {
		this(stack, name, null);
	}

	public IconPackage(ItemStack stack, String[] lore) {
		this(stack, null, lore);
	}

	public IconPackage(ItemStack stack, String name, String[] lore) {
		this.stack = stack;
		this.name = name;
		this.lore = lore;
	}

	public ItemStack getFinalIcon() {
		ItemStack s = stack.clone();
		if (name != null)
			KitUtils.setName(s, name);
		if (lore != null)
			KitUtils.setLore(s, Arrays.asList(lore));
		return s;
	}

	public ItemStack getIcon() {
		return this.stack;
	}

	public String[] getLore() {
		return this.lore;
	}

	public String getName() {
		return this.name;
	}
}
