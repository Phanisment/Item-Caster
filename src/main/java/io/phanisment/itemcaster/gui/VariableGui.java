package io.phanisment.itemcaster.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.Sound;

import fr.mrmicky.fastinv.PaginatedFastInv;
import fr.mrmicky.fastinv.ItemBuilder;
import fr.mrmicky.fastinv.InventoryScheme;

import io.phanisment.itemcaster.ItemCaster;
import io.phanisment.itemcaster.util.Legacy;
import io.phanisment.itemcaster.util.Message;
import io.phanisment.itemcaster.config.item.CasterItem;
import io.phanisment.itemcaster.config.ItemEditConfig;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

public class VariableGui extends PaginatedFastInv {
	public static Map<UUID, VariableData> var_data = new HashMap<>();
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
	
	public VariableGui(CasterItem item, Map<String, Object> data, int index) {
		super(54, Message.get("gui.variable.title", "Ability Variable"));
		this.item = item;
		this.index = index; 
		this.data = data;
		
		previousPageItem(52, p -> ItemEditConfig.icon.get("previous").getItemStack());
		nextPageItem(53, p -> ItemEditConfig.icon.get("next").getItemStack());
		setItem(45, ItemEditConfig.icon.get("back").getItemStack(), e -> new AbilityMenu(item, data, index).open((Player)e.getWhoClicked()));
		setItem(50, ItemEditConfig.icon.get("create_variable").getItemStack(), e -> {
			Player player = (Player)e.getWhoClicked();
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 0.5F, 2.0F);
			chatInput(player, VariableType.CREATE);
		});
		
		List<Map<String, Object>> abilities = item.abilities;
		data.putIfAbsent("variable", new HashMap<>());
		Map<String, Object> variable = (Map<String, Object>)data.get("variable");
		
		if (variable != null) {
			for (String name : variable.keySet()) {
				List<String> lr = new ArrayList<>();
				lr.add("<gray>" + name + ": " + variable.get(name));
				List<String> inf = (List<String>)ItemCaster.getInst().getConfig().getList("gui.variable.interface.lore");
				inf.forEach(lr::add);
				addContent(ItemEditConfig.icon.get("variable").setLore(lr).load().getItemStack(), e -> {
					Player player = (Player)e.getWhoClicked();
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 0.5F, 2.0F);
					variable.remove(name);
					data.put("variable", variable);
					abilities.set(index, data);
					new VariableGui(item.setAbilities(abilities).save(), data, index).open(player);
				});
			}
			
			List<String> lore_data = new ArrayList<>();
			for (Map.Entry<String, Object> dat : variable.entrySet()) {
				lore_data.add("<gray>" + dat.getKey() + ": " + dat.getValue());
			}
			setItem(49, ItemEditConfig.icon.get("variable").setLore(lore_data).load().getItemStack(), e -> {
			Player player = (Player)e.getWhoClicked();
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 2.0F, 0.5F);
			player.getInventory().addItem(item.getItemStack());
			Message.send(player, "<color:#f7d340>" + item.getId() + "</color> added to your inventory.");
		});
		}
		scheme.apply(this);
	}
	
	private void chatInput(Player player, VariableType type) {
		player.closeInventory();
		final Map<String, Object> final_data = data;
		this.var_data.put(player.getUniqueId(), new VariableData(this.item, final_data, this.index, type));
	}
	
	public static class VariableData {
		public int index;
		public Map<String, Object> data;
		public CasterItem item;
		public VariableType type;
		
		public VariableData(CasterItem item, Map<String, Object> data, int index, VariableType type) {
			this.index = index;
			this.data = data;
			this.item = item;
			this.type = type;
		}
	}
	
	public static enum VariableType {
		CREATE, EDIT;
	}
} 