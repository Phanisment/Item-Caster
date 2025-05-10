package io.phanisment.itemcaster.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.Sound;

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
		setItem(50, ItemEditConfig.icon.get("create_ability").getItemStack(), e -> {
			Player player = (Player)e.getWhoClicked();
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 0.5F, 2.0F);
			Map<String, Object> n_a = new HashMap<>();
			n_a.put("skill", "empty");
			ci.abilities.add(n_a);
			new AbilityMenu(ci, n_a, ci.abilities.size() - 1).open(player);
		});
		
		List<Map<String, Object>> abilities = ci.abilities;
		int ability_count = 0;
		for (Map<String, Object> ability : abilities) {
			final int index = ability_count;
			List<String> lore_data = new ArrayList<>();
			for (Map.Entry<String, Object> data : ability.entrySet()) {
				lore_data.add("<gray>" + data.getKey() + ": " + data.getValue());
			}
			List<String> info = (List<String>)ItemCaster.getInst().getConfig().getList("gui.abilities.interface.lore");
			info.forEach(line -> {
				lore_data.add(line);
			});
			addContent(ItemEditConfig.icon.get("ability").setLore(lore_data).load().getItemStack(), e -> {
				Player player = (Player)e.getWhoClicked();
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 0.5F, 2.0F);
				if (e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT) {
					abilities.remove(index);
					final CasterItem item = ci;
					new AbilitiesMenu(ci.setAbilities(abilities).save()).open(player);
				} else if (e.getClick() == ClickType.RIGHT) {
					Map<String, Object> new_map = new HashMap<>(ability);
					abilities.add(new_map);
					final CasterItem item = ci;
					new AbilitiesMenu(ci.setAbilities(abilities).save()).open(player);
				} else {
					final CasterItem item = ci;
					final Map<String, Object> final_ability = ability;
					new AbilityMenu(item, final_ability, index).open(player);
				}
			});
			ability_count++;
		}
		setItem(49, ci.getItemStack(), e -> {
			Player player = (Player)e.getWhoClicked();
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 0.5F, 2.0F);
			player.getInventory().addItem(ci.getItemStack());
			Message.send(player, "<color:#f7d340>" + ci.getId() + "</color> added to your inventory.");
		});
		scheme.apply(this);
	}
}