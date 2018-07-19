package me.NinetyNine.woolshop.anni;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import jdz.bukkitUtils.misc.Random;

public abstract class Kit implements Listener, Comparable<Kit> {
	public static int DESC_LINE_WIDTH = 30;
	protected static int PARTICLE_DISPLAY_RANGE = 96;
	private static final Map<String, Kit> kits = new TreeMap<String, Kit>();

	static void registerKit(Kit kit) {
		kits.put(kit.getName().toLowerCase(), kit);
	}

	public static Collection<Kit> getKits() {
		return Collections.unmodifiableCollection(kits.values());
	}

	public static Kit getKit(String name) {
		return kits.get(ChatColor.stripColor(name).toLowerCase());
	}

	public static Kit getRandom() {
		int i = Random.nextInt(Kit.getKits().size());
		for (Kit kit : Kit.getKits())
			if (i-- == 0)
				return kit;
		return null;
	}

	protected static final ChatColor DESCRIPTION_COLOR = ChatColor.AQUA;

	public String getName() {
		return ChatColor.stripColor(getDisplayName());
	}

	@Override
	public int compareTo(Kit kit) {
		return getName().compareTo(kit.getName());
	}

	@Override
	public int hashCode() {
		return getName() == null ? 0 : getName().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Kit other = (Kit) obj;
		if (getName() == null) {
			if (other.getName() != null) {
				return false;
			}
		}
		else if (!getName().equals(other.getName())) {
			return false;
		}
		return true;
	}

	public List<Player> getPlayers() {
		List<Player> players = new ArrayList<Player>();
		for (Player player : Bukkit.getOnlinePlayers())
			if (this.equals(AnniPlayer.get(player).getKit()))
				players.add(player);
		return players;
	}

	public abstract int getNumUpgrades();

	public abstract int getXPCost();

	public abstract int[] getUpgradeXPCosts();

	public abstract String[] getUpgradeNames();

	public abstract String[] getUpgradeDescriptions();

	protected abstract boolean Initialize();

	public abstract String getDisplayName();

	public abstract String getDescription();

	public abstract String[] getLore(Player player);

	public abstract IconPackage getIconPackage(Player player);

	public abstract boolean hasBought(Player player);

	public abstract int getUpgradesBought(Player player);

	public abstract void onPlayerSpawn(Player player, boolean clear);

	public abstract void cleanUp(Player player);

	public abstract int getKillXP(Player killed, Player killer);
}
