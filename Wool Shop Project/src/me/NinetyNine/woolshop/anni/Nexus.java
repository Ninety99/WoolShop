/*******************************************************************************
 * Copyright 2014 stuntguy3000 (Luke Anderson) and coasterman10. This program is
 * free software; you can redistribute it and/or modify it under the terms of
 * the GNU General Public License as published by the Free Software Foundation;
 * either
 * version 2 of the License, or (at your option) any later version. This program
 * is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR
 * PURPOSE.See the GNU General Public License for more details. You should have
 * received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth
 * Floor,
 * Boston, MA 02110-1301, USA.
 ******************************************************************************/
package me.NinetyNine.woolshop.anni;

import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.RED;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

import jdz.bukkitUtils.events.Listener;
import lombok.Getter;

public class Nexus implements Listener {
	@Getter private final GameTeam team;
	@Getter private final Location location;
	@Getter private int health = 75;

	public Nexus(GameTeam team, Location location) {
		this.team = team;
		this.location = location;

		location.getBlock().setType(Material.ENDER_STONE);
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onBreak(BlockBreakEvent event) {
		if (event.getBlock().getType() != Material.ENDER_STONE)
			return;

		if (location.equals(event.getBlock().getLocation())) {
			event.setCancelled(true);
			return;
		}
	}

	public boolean isAlive() {
		return health > 0;
	}

	public boolean isNear(Player player) {
		return isNear(player.getLocation(), 20);
	}

	public boolean isNear(Player player, double range) {
		return isNear(player.getLocation(), 20);
	}

	public boolean isNear(Location loc, double range) {
		return loc.getWorld().equals(location.getWorld()) && location.distance(loc) < range;
	}

	public static boolean isNearNexus(Player player) {
		return isNearNexus(player.getLocation(), 20);
	}

	public static boolean isNearNexus(Location loc) {
		return isNearNexus(loc, 20);
	}

	public static boolean isNearNexus(Location loc, int range) {
		for (GameTeam team : GameTeam.teams())
			if (team.getNexus().isNear(loc, range))
				return true;
		return false;
	}

	public boolean isHorizontallyClose(Location loc, double range) {
		if (!loc.getWorld().equals(location.getWorld()))
			return false;
		Location diff = location.clone().subtract(loc);
		return Math.abs(diff.getX()) < range && Math.abs(diff.getZ()) < range;
	}

	public static boolean isTooClose(Location loc, int range) {
		for (GameTeam team : GameTeam.teams())
			if (team.getNexus().isHorizontallyClose(loc, range))
				return true;
		return false;
	}
	
	private void destroy(Player breaker, GameTeam breakerTeam) {
		location.getBlock().setType(Material.BEDROCK);

		int baseXP = 25 * GameTeam.getDeadTeams().size();
		int baseCoins = 10 * GameTeam.getDeadTeams().size();
		int basePoints = 5 * GameTeam.getDeadTeams().size();

		for (Player p : team.getOnlinePlayers())
			p.sendMessage(RED + "Your nexus has been destroyed! " + GRAY + "You are no longer respawning!");


		ParticleEffect.EXPLOSION_LARGE.display(1, 1, 1, 0, 20, location.clone().add(0.5, 0, 0.5), 32);

		Location nexusLocation = location.clone();

		for (int y = 0; y < 10; y++) {
			Block b = nexusLocation.add(0, 1, 0).getBlock();
			if (b != null && b.getType() == Material.BEACON) {
				b.setType(Material.AIR);
				break;
			}
		}
	}
}
