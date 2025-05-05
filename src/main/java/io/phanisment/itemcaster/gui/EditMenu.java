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
import java.util.UUID;

public class EditMenu extends PaginatedFastInv {
	public CasterItem ci;
	public static Map<UUID, EditData> chatInput = new HashMap();
	private InventoryScheme scheme = new InventoryScheme()
		.mask("111111111")
		.mask("111111111")
		.mask("111111111")
		.mask("111111111")
		.mask("111111111")
		.bindPagination('1');
	
	public EditMenu(CasterItem ci) {
		super(54, Message.get("gui.edit.title", "Item Editor"));
		this.ci = ci;
		
		previousPageItem(52, p -> ItemEditConfig.icon.get("previous").getItemStack());
		nextPageItem(53, p -> ItemEditConfig.icon.get("next").getItemStack());
		setItem(45, ItemEditConfig.icon.get("back").getItemStack(), e -> new MainMenu().open((Player)e.getWhoClicked()));

		List<ItemEditInterface> interfaces = ItemEditConfig.items;
		for (ItemEditInterface icon : interfaces) {
			addContent(icon.getItemStack(), e -> {
				Player player = (Player)e.getWhoClicked();
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 0.5F, 2.0F);
				
				switch (icon.type) {
					case TYPE:
						chatInput(player, EditType.TYPE);
						Message.send(player, "Editing the item Type.");
						break;
					case NBT:
						if (e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT) {
							update(ci.resetNbt().save(), player);
						} else {
							chatInput(player, EditType.NBT);
							Message.send(player, "Editing the item nbt.");
						}
						break;
					case DISPLAY_NAME:
						if (e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT) {
							new EditMenu(ci.resetDisplayName().save()).open(player);
						} else {
							chatInput(player, EditType.DISPLAY_NAME);
							Message.send(player, "Editing the item nbt.");
						}
						break;
					case MODEL_DATA:
						if (e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT) {
							update(ci.resetModelData().save(), player);
						} else {
							chatInput(player, EditType.DISPLAY_NAME);
							Message.send(player, "Editing the item Model Data.");
						}
						break;
					case UNBREAKABLE:
						if (e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT) {
							update(ci.resetUnbreakable().save(), player);
						} else {
							CasterItem item = ci.setUnbreakable(!ci.getItemMeta().isUnbreakable()).save();
							update(item, player);
							Message.send(player, "Set the item Unbreakable to: " + item.getItemMeta().isUnbreakable());
						}
						break;
					case COLOR:
						if (e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT) {
							update(ci.resetColor().save(), player);
						} else {
							chatInput(player, EditType.COLOR);
							Message.send(player, "Editing the item Color.");
						}
						break;
					case DAMAGE:
						if (e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT) {
							update(ci.resetDamage().save(), player);
						} else {
							chatInput(player, EditType.DAMAGE);
							Message.send(player, "Editing the item Damage.");
						}
						break;
					case REPAIR_COST:
						if (e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT) {
							update(ci.resetRepairCost().save(), player);
						} else {
							chatInput(player, EditType.DISPLAY_NAME);
							Message.send(player, "Editing the item Repair Cost.");
						}
						break;
					case AXOLOTL_VARIANT:
						if (e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT) {
							update(ci.resetAxolotlVariant().save(), player);
						} else {
							chatInput(player, EditType.AXOLOTL_VARIANT);
							Message.send(player, "Editing the axolotl variant in the bucket.");
						}
						break;
					case ABILITIES:
						if (e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT) {
							update(ci.resetAxolotlVariant().save(), player);
						} else {
							player.closeInventory();
							new AbilitiesMenu(ci).open(player);
						}
						break;
					case LORE:
						if (e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT) {
							update(ci.resetLores().save(), player);
						} else if (e.getClick() == ClickType.RIGHT) {
							List<String> lore = ci.lore;
							if (!lore.isEmpty()) {
								lore.remove(lore.size() - 1);
							}
							update(ci.setLore(lore).save(), player);
						} else {
							chatInput(player, EditType.LORE);
						}
						break;
					case ENCHANTS:
						if (e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT) {
							update(ci.resetEnchants().save(), player);
						} else if (e.getClick() == ClickType.RIGHT) {
							List<String> list = ci.enchants;
							if (!list.isEmpty()) {
								list.remove(list.size() - 1);
							}
							update(ci.setEnchants(list).save(), player);
						} else {
							chatInput(player, EditType.ENCHANTS);
						}
						break;
					case HIDE_FLAGS:
						if (e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT) {
							update(ci.resetEnchants().save(), player);
						} else if (e.getClick() == ClickType.RIGHT) {
							List<String> list = ci.enchants;
							if (!list.isEmpty()) {
								list.remove(list.size() - 1);
							}
							update(ci.setFlags(list).save(), player);
						} else {
							chatInput(player, EditType.HIDE_FLAGS);
						}
						break;
					case ATTRIBUTES:
						if (e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT) {
							new EditMenu(ci.resetAttributes().save()).open(player);
						} else if (e.getClick() == ClickType.RIGHT) {
							List<String> list = ci.enchants;
							if (!list.isEmpty()) {
								list.remove(list.size() - 1);
							}
							new EditMenu(ci.setAttributes(list).save()).open(player);
						} else {
							chatInput(player, EditType.ATTRIBUTES);
						}
						break;
					case CROSSBOW_PROJECTILES:
						if (e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT) {
							new EditMenu(ci.resetCrossbowProjectiles().save()).open(player);
						} else if (e.getClick() == ClickType.RIGHT) {
							List<String> list = ci.enchants;
							if (!list.isEmpty()) {
								list.remove(list.size() - 1);
							}
							new EditMenu(ci.setCrossbowProjectiles(list).save()).open(player);
						} else {
							chatInput(player, EditType.CROSSBOW_PROJECTILES);
						}
						break;
					case BUNDLE_ITEMS:
						if (e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT) {
							new EditMenu(ci.resetBundleItems().save()).open(player);
						} else if (e.getClick() == ClickType.RIGHT) {
							List<String> list = ci.enchants;
							if (!list.isEmpty()) {
								list.remove(list.size() - 1);
							}
							new EditMenu(ci.setBundleItems(list).save()).open(player);
						} else {
							chatInput(player, EditType.BUNDLE_ITEMS);
						}
						break;
					default:
						Message.send(player, "Unknown edit Type");
						break;
				}
			});
		}
		
		setItem(49, ItemConfig.getItem(ci.getId()).getItemStack(), e -> {
			Player player = (Player)e.getWhoClicked();
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 2.0F, 0.5F);
			player.getInventory().addItem(ci.getItemStack());
			Message.send(player, "<color:#f7d340>" + ci.getId() + "</color> added to your inventory.");
		});
		scheme.apply(this);
	}
	
	public void update(CasterItem item, Player player) {
		player.closeInventory();
		new EditMenu(item).open(player);
	}
	
	private void chatInput(Player player, EditType type) {
		player.closeInventory();
		chatInput.put(player.getUniqueId(), new EditData(ci, type));
	}
	
	public static class EditData {
		public CasterItem item;
		public EditType type;
		public EditData(CasterItem item, EditType type) {
			this.item = item;
			this.type = type;
		}
	}
	
	public static enum EditType {
		TYPE,
		NBT,
		DISPLAY_NAME,
		MODEL_DATA,
		UNBREAKABLE,
		COLOR,
		DAMAGE,
		REPAIR_COST,
		AXOLOTL_VARIANT,
		LORE,
		ENCHANTS,
		HIDE_FLAGS,
		ATTRIBUTES,
		CROSSBOW_PROJECTILES,
		BUNDLE_ITEMS,
		ABILITIES;
	}
}