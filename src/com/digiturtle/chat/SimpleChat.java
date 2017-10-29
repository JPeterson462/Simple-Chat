package com.digiturtle.chat;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleChat extends JavaPlugin {
	
	private Prefix[] prefixes;
	
	private String defaultPrefix;
	
	private String overridePermission;
	
	private int distance;
	
	public void onEnable() {
	    getConfig().options().copyDefaults(true);
	    saveConfig();
		List<String> prefixNames = getConfig().getStringList("prefixes");
		prefixes = new Prefix[prefixNames.size()];
		for (int i = 0; i < prefixNames.size(); i++) {
			Prefix prefix = new Prefix();
			prefix.permission = getConfig().getString(prefixNames.get(i) + "-permission");
			prefix.prefix = getConfig().getString(prefixNames.get(i) + "-prefix");
			prefixes[i] = prefix;
		}
		defaultPrefix = getConfig().getString("default", null);
		overridePermission = getConfig().getString("override", null);
		distance = getConfig().getInt("distance", Integer.MAX_VALUE);
		getServer().getPluginManager().registerEvents(new ChatAdapter(), this);
	}
	
	public void onDisable() {
		saveConfig();
	}
	
	public class Prefix {
		
		private String permission;
		
		private String prefix;
		
	}
	
	public class ChatAdapter implements Listener {
		
		@EventHandler
		public void onChat(AsyncPlayerChatEvent event) {
			Player sender = event.getPlayer();
			Set<Player> recipients = event.getRecipients();
			Iterator<Player> iterator = recipients.iterator();
			while (iterator.hasNext()) {
				Player recipient = iterator.next();
				if ((overridePermission == null || !recipient.hasPermission(overridePermission)) && recipient.getLocation().distance(sender.getLocation()) > distance) {
					iterator.remove();
				}
			}
			String prefixText = null;
			for (int i = 0; i < prefixes.length; i++) {
				Prefix prefix = prefixes[i];
				if (sender.hasPermission(prefix.permission)) {
					prefixText = prefix.prefix;
				}
			}
			if (prefixText == null) {
				prefixText = defaultPrefix;
			}
			if (prefixText == null) {
				prefixText = "";
			}
			prefixText = prefixText.replaceAll("&0", ChatColor.BLACK.toString());
			prefixText = prefixText.replaceAll("&1", ChatColor.DARK_BLUE.toString());
			prefixText = prefixText.replaceAll("&2", ChatColor.DARK_GREEN.toString());
			prefixText = prefixText.replaceAll("&3", ChatColor.DARK_AQUA.toString());
			prefixText = prefixText.replaceAll("&4", ChatColor.DARK_RED.toString());
			prefixText = prefixText.replaceAll("&5", ChatColor.DARK_PURPLE.toString());
			prefixText = prefixText.replaceAll("&6", ChatColor.GOLD.toString());
			prefixText = prefixText.replaceAll("&7", ChatColor.GRAY.toString());
			prefixText = prefixText.replaceAll("&8", ChatColor.DARK_GRAY.toString());
			prefixText = prefixText.replaceAll("&9", ChatColor.BLUE.toString());
			prefixText = prefixText.replaceAll("&a", ChatColor.GREEN.toString());
			prefixText = prefixText.replaceAll("&b", ChatColor.AQUA.toString());
			prefixText = prefixText.replaceAll("&c", ChatColor.RED.toString());
			prefixText = prefixText.replaceAll("&d", ChatColor.LIGHT_PURPLE.toString());
			prefixText = prefixText.replaceAll("&e", ChatColor.YELLOW.toString());
			prefixText = prefixText.replaceAll("&f", ChatColor.WHITE.toString());
			prefixText = prefixText.replaceAll("&k", ChatColor.MAGIC.toString());
			prefixText = prefixText.replaceAll("&l", ChatColor.BOLD.toString());
			prefixText = prefixText.replaceAll("&m", ChatColor.STRIKETHROUGH.toString());
			prefixText = prefixText.replaceAll("&n", ChatColor.UNDERLINE.toString());
			prefixText = prefixText.replaceAll("&o", ChatColor.ITALIC.toString());
			prefixText = prefixText.replaceAll("&r", ChatColor.RESET.toString());
			event.setFormat(prefixText + "%s: %s");
		}
		
	}

}
