package io.phanisment.itemcaster.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.event.inventory.ClickType;

import fr.mrmicky.fastinv.PaginatedFastInv;
import fr.mrmicky.fastinv.ItemBuilder;
import fr.mrmicky.fastinv.InventoryScheme;

import io.phanisment.itemcaster.ItemCaster;
import io.phanisment.itemcaster.util.Legacy;
import io.phanisment.itemcaster.util.Message;
import io.phanisment.itemcaster.config.item.CasterItem;
import io.phanisment.itemcaster.config.ItemEditConfig;
import io.phanisment.itemcaster.config.item.ItemAbilityInterface;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

public class AbilityMenu extends PaginatedFastInv {
	public static Map<UUID, AbilityData> edit = new HashMap<>();
	public Map<String, Object> data;
	public int index;
	public CasterItem item;
	private InventoryScheme scheme = new InventoryScheme()
		.mask("111111111")
		.mask("111111111")
		.mask("111111111")
		.mask("111111111")
		.mask("111111111")
		.bindPagination('1');
	
	public AbilityMenu(CasterItem item, Map<String, Object> data, int index) {
		super(54, Message.get("gui.abilities.title", "Abilities"));
		this.item = item;
		this.index = index; 
		this.data = data;
		
		previousPageItem(52, p -> ItemEditConfig.icon.get("previous").getItemStack());
		nextPageItem(53, p -> ItemEditConfig.icon.get("next").getItemStack());
		setItem(45, ItemEditConfig.icon.get("back").getItemStack(), e -> new AbilitiesMenu(item).open((Player)e.getWhoClicked()));
		
		List<ItemAbilityInterface> interfaces = ItemEditConfig.ability_icons;
		for (ItemAbilityInterface icon : interfaces) {
			addContent(icon.getItemStack(), e -> {
				Player player = (Player)e.getWhoClicked();
				switch (icon.type) {
					case SKILL:
						chatInput(player, AbilityType.SKILL);
						break;
					case ACTIVATOR:
						chatInput(player, AbilityType.ACTIVATOR);
						break;
					case POWER:
						chatInput(player, AbilityType.POWER);
						break;
					case SHOW_LORE:
						List<Map<String, Object>> l_m = item.abilities;
						data.put("show_in_lore", ((Boolean)data.get("show_in_lore")) ? false : true);
						l_m.set(index, data);
						CasterItem ci = item.setAbilities(l_m);
						break;
					case SIGNAL:
						chatInput(player, AbilityType.SIGNAL);
						break;
					default:
						Message.send(player, "Unknown edit Type");
						break;
				}
			});
		}
		
		List<String> lore_data = new ArrayList<>();
		for (Map.Entry<String, Object> ability : data.entrySet()) {
			lore_data.add(ability.getKey() + ": " + ability.getValue());
		}
		
		setItem(49, ItemEditConfig.icon.get("ability").setLore(lore_data).load().getItemStack());
		scheme.apply(this);
	}
	
	private void chatInput(Player player, AbilityType type) {
		player.closeInventory();
		final Map<String, Object> final_data = data;
		this.edit.put(player.getUniqueId(), new AbilityData(this.item, final_data, this.index, type));
	}
	
	public static class AbilityData {
		public int index;
		public Map<String, Object> data;
		public CasterItem item;
		public AbilityType type;
		
		public AbilityData(CasterItem item, Map<String, Object> data, int index, AbilityType type) {
			this.index = index;
			this.data = data;
			this.item = item;
			this.type = type;
		}
	}
	
	public static enum AbilityType {
		SKILL,
		ACTIVATOR,
		POWER,
		SHOW_LORE,
		SIGNAL,
		CANCEL_EVENT;
	}
}