package me.NinetyNine.woolshop.listeners;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import lombok.Getter;

public class WoolBreak implements Listener {

	@Getter
	private static Map<Player, Integer> broke = new HashMap<Player, Integer>();

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if (e.getBlock().getType() != Material.WOOL)
			return;

		if (!WoolPlace.getWools().contains(e.getBlock().getType()))
			return;

		int mined = 1;
		Player player = e.getPlayer();

		if (!getBroke().containsKey(player))
			getBroke().put(e.getPlayer(), mined);
		else {
			if (getBroke().get(player) == 1) {
				add(player);
				player.sendMessage(ChatColor.GREEN + "Health of Wool: 4");
				return;
			} else if (getBroke().get(player) == 2) {
				add(player);
				player.sendMessage(ChatColor.GREEN + "Health of Wool: 3");
				return;
			} else if (getBroke().get(player) == 3) {
				add(player);
				player.sendMessage(ChatColor.GREEN + "Health of Wool: 2");
				return;
			} else if (getBroke().get(player) == 4) {
				add(player);
				player.sendMessage(ChatColor.GREEN + "Health of Wool: 1");
				return;
			} else if (getBroke().get(player) == 5) {
				getBroke().remove(player);
				WoolPlace.getWools().remove(e.getBlock().getType());
				e.getBlock().setType(Material.AIR);
				return;
			}
		}
	}

	public void add(Player player) {
		getBroke().put(player, getBroke().get(player) + 1);
	}
}
