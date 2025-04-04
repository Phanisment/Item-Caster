package io.phanisment.itemcaster.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.inventory.ClickType;

import fr.mrmicky.fastinv.PaginatedFastInv;
import fr.mrmicky.fastinv.ItemBuilder;
import fr.mrmicky.fastinv.InventoryScheme;

import io.phanisment.itemcaster.ItemCaster;
import io.phanisment.itemcaster.config.item.CasterItem;
import io.phanisment.itemcaster.util.Message;
import io.phanisment.itemcaster.util.Legacy;
import io.phanisment.itemcaster.config.ItemConfig;
import io.phanisment.itemcaster.config.ItemEditConfig;
import io.phanisment.itemcaster.config.item.ItemEditInterface;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

public class AbilitiesMenu extends PaginatedFastInv {
	public CasterItem ci;
	private InventoryScheme scheme = new InventoryScheme()
		.mask("111111111")
		.mask("111111111")
		.mask("111111111")
		.mask("111111111")
		.mask("111111111")
		.bindPagination('1');
	
	public AbilitiesMenu(CasterItem ci) {
		super(54, Message.get("gui.abilities.title", "Abilities"));
		this.ci = ci;
		
		previousPageItem(52, p -> ItemEditConfig.icon.get("previous").getItemStack());
		nextPageItem(53, p -> ItemEditConfig.icon.get("next").getItemStack());
		setItem(45, ItemEditConfig.icon.get("back").getItemStack(), e -> new EditMenu(ci).open((Player)e.getWhoClicked()));
		
		List<Map<String, Object>> abilities = ci.abilities;
		int ability_count = 0;
		for (Map<String, Object> ability : abilities) {
			final int index = ability_count;
			List<String> lore_data = new ArrayList<>();
			for (Map.Entry<String, Object> data : ability.entrySet()) {
				lore_data.add(data.getKey() + ": " + data.getValue());
			}
			addContent(ItemEditConfig.icon.get("ability").setLore(lore_data).load().getItemStack(), e -> {
				Player player = (Player)e.getWhoClicked();
				player.closeInventory();
				final CasterItem item = ci;
				final Map<String, Object> final_ability = ability;
				new AbilityMenu(item, final_ability, index).open(player);
			});
			ability_count++;
		}
		setItem(49, ItemConfig.getItem(ci.getId()).getItemStack());
		scheme.apply(this);
	}
}