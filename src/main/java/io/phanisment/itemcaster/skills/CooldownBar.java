package io.phanisment.itemcaster.skills;

import io.phanisment.itemcaster.ItemCaster;
import io.phanisment.itemcaster.skills.SkillCooldown;
import io.phanisment.itemcaster.util.Legacy;

public class CooldownBar {
	private ItemCaster pl = ItemCaster.getInst();
	private int cooldown;
	private String skill;
	private int maxCooldown;
	
	public CooldownBar(String skill, int maxCooldown, int cooldown) {
		this.skill = skill;
		this.maxCooldown = maxCooldown;
		this.cooldown = cooldown;
	}
	
	public String generate() {
		int size = pl.getConfig().getInt("abilities.actionbar.cooldown_bar.size");
		String font = pl.getConfig().getString("abilities.actionbar.cooldown_bar.font");
		String empty = pl.getConfig().getString("abilities.actionbar.cooldown_bar.empty_color") + font;
		String fill = pl.getConfig().getString("abilities.actionbar.cooldown_bar.fill_color") + font;
		int filled = (int)(((double) cooldown / maxCooldown) * size);
		StringBuilder bar = new StringBuilder();
		for (int i = 0; i < size; i++) {
			bar.append(i < filled ? fill : empty);
		}
		
		String cd;
		switch (pl.getConfig().getString("abilities.actionbar.cooldown_type").toLowerCase()) {
			case "second":
				cd = String.format("%.1f", cooldown / 20.0);
				break;
			case "minute":
				cd = String.format("%.1f", cooldown / 1200.0);
				break;
			default:
				cd = String.valueOf(cooldown);
				break;
		}
		
		String format = pl.getConfig().getString("abilities.actionbar.format")
			.replace("{skill}", skill.replace("_", " "))
			.replace("{cooldown_bar}", bar.toString())
			.replace("{cooldown}", cd);
		return Legacy.serializer(format);
	}
}