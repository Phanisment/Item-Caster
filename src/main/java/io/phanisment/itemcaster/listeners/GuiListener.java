package io.phanisment.itemcaster.listeners;

import org.bukkit.Color;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.ConfigurationSection;

import io.phanisment.itemcaster.config.item.CasterItem;
import io.phanisment.itemcaster.ItemCaster;
import io.phanisment.itemcaster.util.Message;
import io.phanisment.itemcaster.util.Legacy;
import io.phanisment.itemcaster.gui.EditMenu;
import io.phanisment.itemcaster.gui.MainMenu;
import io.phanisment.itemcaster.gui.AbilityMenu;
import io.phanisment.itemcaster.config.ItemConfig;
import io.phanisment.itemcaster.config.item.Pack;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class GuiListener implements Listener {
	@EventHandler
	public void onJoin(PlayerLoginEvent event) {
		Player player = event.getPlayer();
		EditMenu.chatInput.remove(player.getUniqueId());
		MainMenu.create_item.remove(player.getUniqueId());
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		EditMenu.chatInput.remove(player.getUniqueId());
		MainMenu.create_item.remove(player.getUniqueId());
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		String value = event.getMessage();
		if (EditMenu.chatInput.containsKey(player.getUniqueId())) {
			event.setCancelled(true);
			EditMenu.EditData data = EditMenu.chatInput.remove(player.getUniqueId());
			CasterItem item = data.item;
			if (value.equalsIgnoreCase("exit") || value.equalsIgnoreCase("cancel")) {
				final CasterItem ci = item;
				Bukkit.getScheduler().runTask(ItemCaster.getInst(), () -> new EditMenu(ci).open(player));
				Message.send(player, "Edit Cancelled.");
				return;
			}
			switch (data.type) {
				case TYPE:
					CasterItem ci = item.setType(value).save();
					if (item.setType(value).save() != null) {
						item = ci;
					}
					break;
				case NBT:
					item = item.setNbt(value).save();
					break;
				case DISPLAY_NAME:
					item = item.setDisplayName(value).save();
					break;
				case MODEL_DATA:
					try {
						item = item.setModelData(Integer.parseInt(value.trim())).save();
					} catch (NumberFormatException e) {
						Message.send(player, "The value is must be Number!");
						return;
					}
					break;
				case COLOR:
					item = item.setColor(value).save();
					break;
				case DAMAGE:
					try {
						item = item.setDamage(Integer.parseInt(value.trim())).save();
					} catch (NumberFormatException e) {
						Message.send(player, "The value is must be Number!");
						return;
					}
				case REPAIR_COST:
					try {
						item = item.setRepairCost(Integer.parseInt(value.trim())).save();
					} catch (NumberFormatException e) {
						Message.send(player, "The value is must be Number!");
						return;
					}
				default:
					break;
			}
			
			final CasterItem ci = item;
			Bukkit.getScheduler().runTask(ItemCaster.getInst(), () -> new EditMenu(ci).open(player));
		}
		
		if (MainMenu.create_item.contains(player.getUniqueId())) {
			event.setCancelled(true);
			MainMenu.create_item.remove(player.getUniqueId());
			File files = new File(ItemCaster.getInst().getDataFolder(), "items");
			String[] part = value.trim().split(":");
			if (value.equalsIgnoreCase("exit") || value.equalsIgnoreCase("cancel")) {
				Bukkit.getScheduler().runTask(ItemCaster.getInst(), () -> new MainMenu().open(player));
				Message.send(player, "Create item Cancelled.");
				return;
			}
			if (part.length == 2) {
				File namespace_loc = new File(files, part[0] + ".yml");
				if (namespace_loc.exists()) {
					if (namespace_loc.isFile()) {
						YamlConfiguration content = YamlConfiguration.loadConfiguration(namespace_loc);
						content.createSection("items." + part[1]);
						Bukkit.getScheduler().runTask(ItemCaster.getInst(), () -> new MainMenu().open(player));
						ItemCaster.getInst().getItemConfig().items.put(part[0] + ":" + part[1], new CasterItem(new Pack(namespace_loc, content), content.getConfigurationSection("items." + part[1]), part).load());
						try {
							content.save(namespace_loc);
						} catch (Exception e) {
							Message.send(player, "Failed to make the item");
						}
					}
				} else {
					try {
						files.createNewFile();
						YamlConfiguration content = YamlConfiguration.loadConfiguration(namespace_loc);
						content.createSection("items." + part[1]);
						Bukkit.getScheduler().runTask(ItemCaster.getInst(), () -> new MainMenu().open(player));
						content.save(namespace_loc);
						ItemCaster.getInst().getItemConfig().items.put(part[0] + ":" + part[1], new CasterItem(new Pack(namespace_loc, content), content.getConfigurationSection("items." + part[1]), part).load());
					} catch (Exception e) {
						Message.send(player, "Failed to make the item");
					}
				}
			} else {
				MainMenu.create_item.add(player.getUniqueId());
				Message.send(player, "The value is must be '<namesace>:<item_id>'.");
			}
		}
		
		if (AbilityMenu.edit.containsKey(player.getUniqueId())) {
			event.setCancelled(true);
			AbilityMenu.AbilityData data = AbilityMenu.edit.remove(player.getUniqueId());
			CasterItem item = data.item;
			Map<String, Object> map = data.data;
			int index = data.index;
			List<Map<String, Object>> l_m = item.abilities;
			
			if (value.equalsIgnoreCase("exit") || value.equalsIgnoreCase("cancel")) {
				final CasterItem ci = item;
				Bukkit.getScheduler().runTask(ItemCaster.getInst(), () -> new AbilityMenu(ci, map, index).open(player));
				Message.send(player, "Edit Cancelled.");
				return;
			}
			switch (data.type) {
				case SKILL:
					map.put("skill", value);
					l_m.set(index, map);
					item = item.setAbilities(l_m);
					break;
				case ACTIVATOR:
					map.put("activator", value);
					l_m.set(index, map);
					item = item.setAbilities(l_m);
					break;
				case POWER:
					try {
						map.put("power", Integer.parseInt(value.trim()));
						l_m.set(index, map);
						item = item.setAbilities(l_m);
					} catch (NumberFormatException e) {
						Message.send(player, "The value is must be Number!");
						return;
					}
				case SIGNAL:
					map.put("signal", value);
					l_m.set(index, map);
					item = item.setAbilities(l_m);
					break;
				default:
					break;
			}
			final CasterItem ci = item;
			Bukkit.getScheduler().runTask(ItemCaster.getInst(), () -> new AbilityMenu(ci, map, index).open(player));
		}
	}
	
	public static void tick(Player player) {
		if (EditMenu.chatInput.containsKey(player.getUniqueId()) || AbilityMenu.edit.containsKey(player.getUniqueId())) {
			player.sendTitle(Legacy.serializer("<yellow>Editing Mode"), Legacy.serializer("<gray>Type for set the value"), 0, 40, 0);
		}
	}
}