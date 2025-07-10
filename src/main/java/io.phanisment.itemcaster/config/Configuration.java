package io.phanisment.itemcaster.config;

import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

import io.lumine.mythic.bukkit.events.MythicReloadedEvent;
import io.lumine.mythic.bukkit.events.MythicMobItemGenerateEvent;
import io.lumine.mythic.bukkit.utils.plugin.ReloadableModule;
import io.lumine.mythic.bukkit.events.MythicPreReloadEvent;
import io.lumine.mythic.bukkit.utils.terminable.TerminableConsumer;
import io.lumine.mythic.bukkit.utils.Events;
import io.lumine.mythic.core.items.MythicItem;
import io.lumine.mythic.core.items.ItemExecutor;
import io.lumine.mythic.api.items.ItemManager;
import io.lumine.mythic.api.packs.Pack;

import io.phanisment.itemcaster.ItemCaster;
import io.phanisment.itemcaster.recipe.RecipeManager;
import io.phanisment.itemcaster.item.CasterItem;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.io.File;

public class Configuration extends ReloadableModule<ItemCaster> {
	private static RecipeManager recipe;
	
	public Configuration(ItemCaster plugin) {
		super(plugin, false);
		this.load(plugin);
	}
	
	@Override
	public void load(ItemCaster plugin) {
		this.load();
		ItemCaster.getMythicInst().getItemManager().load(ItemCaster.getMythicInst());
		Events.subscribe(MythicReloadedEvent.class).handler(e -> this.load()).bindWith(this);
		Events.subscribe(MythicMobItemGenerateEvent.class).handler(this::loadItem).bindWith(this);
	}
	
	private void load() {
		recipe = new RecipeManager(this);
		for (Pack pack : ItemCaster.getMythicInst().getPackManager().getPacks()) {
			File file = pack.getPackFolder("Recipes");
			if (!file.exists()) file.mkdirs();
			recipe.load(pack, file);
		}
	}
	
	private void loadItem(MythicMobItemGenerateEvent e) {
		CasterItem item = new CasterItem(e.getItem());
		item.applyData(e);
	}
	
	@Override
	public void unload() {
	}
	
	public static RecipeManager getRecipeManager() {
		return recipe;
	}
}