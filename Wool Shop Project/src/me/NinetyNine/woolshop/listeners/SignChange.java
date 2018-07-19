package me.NinetyNine.woolshop.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignChange implements Listener {

	@EventHandler
	public void onSignChange(SignChangeEvent e) {
		if (e.getLine(1).equals("[Shop]") && e.getLine(2).equals("Wool")) {
			e.setLine(1, ChatColor.LIGHT_PURPLE + "[Shop]");
			e.getPlayer().sendMessage(ChatColor.GREEN + "Succesfully made sign into a Wool Shop!");
			return;
		}
	}
}
