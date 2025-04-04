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
import io.phanisment.itemcaster.config.item.ItemEditInterface;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

public class MainMenu extends PaginatedFastInv {
	public static List<UUID> create_item = new ArrayList<>();
	private InventoryScheme scheme = new InventoryScheme()
		.mask("111111111")
		.mask("111111111")
		.mask("111111111")
		.mask("111111111")
		.mask("111111111")
		.bindPagination('1');
	
	public MainMenu() {
		super(54, Message.get("gui.main.title", "Item Browser"));
		
		previousPageItem(52, p -> ItemEditConfig.icon.get("previous").getItemStack());
		nextPageItem(53, p -> ItemEditConfig.icon.get("next").getItemStack());
		setItem(49, ItemEditConfig.icon.get("create_item").getItemStack(), e -> {
			Player player = (Player)e.getWhoClicked();
			player.closeInventory();
			create_item.add(player.getUniqueId());
			Message.send(player, "Type namespace and the item id to Create a new Item: '<namespace>:<item_id>'");
		});
		
		Map<String, CasterItem> items = ItemCaster.getInst().getItemConfig().getItemList();
		for (CasterItem ci : items.values()) {
			ItemStack is = new ItemStack(ci.getItemStack());
			if (is != null) {
				addContent(new ItemBuilder(is).addLore(Legacy.serializer((List<String>)ItemCaster.getInst().getConfig().getList("gui.main.interface.content.lore"))).build(), e -> {
					Player player = (Player)e.getWhoClicked();
					if (e.getClick() == ClickType.RIGHT) {
						player.closeInventory();
						new EditMenu(ci).open(player);
					} else if (e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT) {
						ci.remove();
						update(player);
					} else {
						player.getInventory().addItem(ci.getItemStack());
						Message.send(player, "<color:#f7d340>" + ci.getId() + "</color> added to your inventory.");
					}
				});
			}
			scheme.apply(this);
		}
	}
	
	private void update(Player player) {
		player.closeInventory();
		new MainMenu().open(player);
	}
}