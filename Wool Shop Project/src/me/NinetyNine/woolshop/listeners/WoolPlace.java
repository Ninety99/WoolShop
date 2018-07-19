package me.NinetyNine.woolshop.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Wool;

import lombok.Getter;
import me.NinetyNine.woolshop.anni.AnniPlayer;

public class WoolPlace implements Listener {

	@Getter
	private static List<Material> wools = new ArrayList<Material>();

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if (e.getBlockPlaced().getType() == Material.WOOL)
			return;

		if (!(e.getBlockPlaced().getState() instanceof Wool))
			return;
		else {
			Block block = e.getBlockPlaced();
			ItemStack item = new ItemStack(block.getType(), 1);
			item.setData(
					new MaterialData(Integer.parseInt(AnniPlayer.get(e.getPlayer()).getTeam().getColor().toString())));
			if (!getWools().contains(item.getType()))
				getWools().add(item.getType());
			return;
		}
	}
}
