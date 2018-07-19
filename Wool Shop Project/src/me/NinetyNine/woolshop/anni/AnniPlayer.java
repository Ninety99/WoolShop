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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import jdz.bukkitUtils.misc.WorldUtils;
import lombok.Getter;
import lombok.Setter;

public class AnniPlayer<kit> {
	private static Map<UUID, AnniPlayer> nameToPlayer = new ConcurrentHashMap<UUID, AnniPlayer>();

	public static AnniPlayer get(OfflinePlayer player) {
		if (!nameToPlayer.containsKey(player.getUniqueId()))
			nameToPlayer.put(player.getUniqueId(), new AnniPlayer(player));
		return nameToPlayer.get(player.getUniqueId());
	}

	public static void reset() {
		nameToPlayer.clear();
	}

	@Getter private GameTeam team = GameTeam.NONE;
	@Getter @Setter private Kit kit = Kit.getKit("Civilian");
	@Getter @Setter private boolean alive = false;
	@Getter private final UUID uuid;

	private final HashMap<String, Object> data = new HashMap<>();

	public AnniPlayer(OfflinePlayer player) {
		uuid = player.getUniqueId();
	}

	public Object getData(String s) {
		return data.get(s);
	}

	public void setData(String s, Object o) {
		data.put(s, o);
	}

	void setTeam(GameTeam t) {
		if (team != null)
			team = t;
		else
			team = GameTeam.NONE;
	}

	public Player getPlayer() {
		return Bukkit.getPlayer(uuid);
	}

	public Set<AnniPlayer> getNearbyAllies(double range) {
		Set<AnniPlayer> players = new HashSet<AnniPlayer>();
		Player player = getPlayer();

		if (player == null)
			return players;

		for (Player other : WorldUtils.getNearbyPlayers(player.getLocation(), range)) {
			if (player.equals(other))
				continue;
			if (!player.canSee(other))
				continue;
			AnniPlayer anniOther = AnniPlayer.get(other);
			if (anniOther.getTeam() == getTeam())
				players.add(anniOther);
		}

		return players;
	}

	public Set<AnniPlayer> getNearbyEnemies(double range) {
		return getNearbyEnemies(range, false);
	}

	public Set<AnniPlayer> getNearbyEnemies(double range, boolean trackInvis) {
		Set<AnniPlayer> players = new HashSet<AnniPlayer>();
		Player player = getPlayer();

		if (player == null)
			return players;

		for (Player other : WorldUtils.getNearbyPlayers(player.getLocation(), range)) {
			if (!trackInvis && (other.hasPotionEffect(PotionEffectType.INVISIBILITY) || !player.canSee(other)))
				continue;
			AnniPlayer anniOther = AnniPlayer.get(other);
			if (anniOther.getTeam() != getTeam())
				players.add(anniOther);
		}

		return players;
	}

	public AnniPlayer getNearestAlly(double range) {
		AnniPlayer nearest = null;
		double nearestDist = range;
		Player player = getPlayer();

		for (AnniPlayer ally : getNearbyAllies(range)) {
			if (nearest == null)
				nearest = ally;
			else {
				double dist = ally.getPlayer().getLocation().distance(player.getLocation());
				if (dist < nearestDist) {
					nearest = ally;
					nearestDist = dist;
				}
			}
		}
		return nearest;
	}

	public AnniPlayer getNearestEnemy(double range) {
		return getNearestEnemy(range, false);
	}

	public AnniPlayer getNearestEnemy(double range, boolean trackInvis) {
		AnniPlayer nearest = null;
		double nearestDist = range;
		Player player = getPlayer();
		for (AnniPlayer enemy : getNearbyEnemies(range, trackInvis)) {
			if (nearest == null)
				nearest = enemy;
			else {
				double dist = enemy.getPlayer().getLocation().distance(player.getLocation());
				if (dist < nearestDist) {
					nearest = enemy;
					nearestDist = dist;
				}
			}
		}
		return nearest;
	}
}
