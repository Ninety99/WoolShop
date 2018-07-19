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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import jdz.bukkitUtils.misc.StringUtils;
import jdz.bukkitUtils.misc.WorldUtils;
import lombok.Getter;

public enum GameTeam implements Listener{
	RED, YELLOW, GREEN, BLUE, NONE, BOSS1, BOSS2;

	public static GameTeam[] teams() {
		return new GameTeam[] { RED, YELLOW, GREEN, BLUE };
	}

	public static GameTeam getTeam(String teamName) {
		teamName = ChatColor.stripColor(teamName.toUpperCase());
		for (GameTeam team : GameTeam.teams())
			if (teamName.equals(team.name()))
				return team;
		return null;
	}

	@Getter private Nexus nexus = null;

	private final Set<OfflinePlayer> allPlayers = new HashSet<OfflinePlayer>();
	private final Set<OfflinePlayer> recentPlayers = new HashSet<OfflinePlayer>();

	public ChatColor getChatColor() {
		if (isPlayerTeam())
			return ChatColor.valueOf(name());
		else
			return ChatColor.GRAY;
	}

	public String getColoredName() {
		return getChatColor() + toString();
	}

	@Override
	public String toString() {
		return StringUtils.capitalizeWord(name());
	}

	public boolean isPlayerTeam() {
		for (GameTeam team : GameTeam.teams())
			if (team == this)
				return true;
		return false;
	}

	public boolean isTooBig() {
		if (!isPlayerTeam())
			return true;

		int players = recentPlayers.size();

		for (GameTeam gt : GameTeam.teams())
			if (players >= gt.getRecentPlayers().size() + 3 && (gt.getNexus() == null || gt.getNexus().isAlive()))
				return true;
		return false;
	}

	public void loadNexus(Location loc) {
		if (isPlayerTeam()) {
			nexus = new Nexus(this, loc);
			System.out.println("New nexus for team " + toString() + " " + WorldUtils.locationToLegibleString(loc));
		}
	}


	public void join(Player player) {
		join(player, false);
	}

	public void join(Player player, boolean isForce) {
		AnniPlayer meta = AnniPlayer.get(player);

		if (isPlayerTeam())
			if (isForce) {
				player.sendMessage(ChatColor.DARK_AQUA + "You force-joined " + getColoredName());
				for (Player staff : Bukkit.getOnlinePlayers())
					if (staff.hasPermission("anni.staff")) {
						staff.sendMessage(
								ChatColor.GOLD + "[ANNI] " + player.getName() + " force-joined " + getColoredName());
						staff.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 0.75f, 0.0f);
					}
			}
			else {
				player.sendMessage(ChatColor.DARK_AQUA + "You joined " + getColoredName());
			}

		if (meta.getTeam().isPlayerTeam()) {
			meta.getTeam().allPlayers.remove(player);
			meta.getTeam().recentPlayers.remove(player);
		}
		recentPlayers.add(player);
		allPlayers.add(player);
	}

	private Map<UUID, Integer> recentPlayersRemoveTask = new HashMap<UUID, Integer>();

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		AnniPlayer anniPlayer = AnniPlayer.get(player);
		if (anniPlayer.getTeam() != this)
			return;

		if (recentPlayersRemoveTask.containsKey(player.getUniqueId()))
			Bukkit.getScheduler().cancelTask(recentPlayersRemoveTask.remove(player.getUniqueId()));
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (AnniPlayer.get(event.getPlayer()).getTeam() != this)
			return;

		UUID uuid = event.getPlayer().getUniqueId();
		if (recentPlayersRemoveTask.containsKey(uuid)) {
			Bukkit.getScheduler().cancelTask(recentPlayersRemoveTask.get(uuid));
			recentPlayersRemoveTask.remove(uuid);
		}

		if (!recentPlayers.contains(event.getPlayer()))
			recentPlayers.add(event.getPlayer());
	}

	public Set<OfflinePlayer> getRecentPlayers() {
		return Collections.unmodifiableSet(recentPlayers);
	}

	public Set<OfflinePlayer> getAllPlayers() {
		return Collections.unmodifiableSet(allPlayers);
	}

	public List<Player> getOnlinePlayers() {
		List<Player> players = new ArrayList<Player>();

		if (isPlayerTeam())
			for (Player p : Bukkit.getOnlinePlayers())
				if (AnniPlayer.get(p).getTeam() == this)
					players.add(p);

		return players;
	}

	public Color getColor() {
		switch (this) {
		case RED:
			return Color.RED;
		case BLUE:
			return Color.BLUE;
		case GREEN:
			return Color.LIME;
		case YELLOW:
			return Color.YELLOW;
		default:
			return Color.PURPLE;
		}
	}

	public void reset() {
		recentPlayers.clear();
		allPlayers.clear();
	}

	public static Set<GameTeam> getDeadTeams() {
		Set<GameTeam> deadTeams = new HashSet<GameTeam>();
		for (GameTeam team : GameTeam.teams())
			if (!team.getNexus().isAlive())
				deadTeams.add(team);
		return deadTeams;
	}

	public static GameTeam getSmallestLivingTeam() {
		GameTeam smallestTeam = GameTeam.NONE;
		int smallestSize = Integer.MAX_VALUE;
		for (GameTeam team : GameTeam.teams()) {
			if (team.getRecentPlayers().size() < smallestSize) {
				smallestSize = team.getOnlinePlayers().size();
				smallestTeam = team;
			}
		}
		return smallestTeam;
	}
}
