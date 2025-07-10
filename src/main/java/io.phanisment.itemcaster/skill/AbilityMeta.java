package io.phanisment.itemcaster.skill;

import de.tr7zw.nbtapi.iface.ReadableNBT;
import io.phanisment.itemcaster.skill.SkillActivator.Activator;
import java.util.Set;

public record AbilityMeta(String skill, Activator activator, SkillAttributes attributes) {
	public static record SkillAttributes(float power, String signal, ReadableNBT variables) {
		public Set<String> getVarName() {
			return variables != null ? variables.getKeys() : Set.of();
		}
	}
}