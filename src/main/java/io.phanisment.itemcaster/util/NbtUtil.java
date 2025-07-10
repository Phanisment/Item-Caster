package io.phanisment.itemcaster.util;

import de.tr7zw.nbtapi.NBTType;
import de.tr7zw.nbtapi.iface.ReadableNBT;

public class NbtUtil {
	
	private NbtUtil() {
	}
	
	public static int getIntSafe(ReadableNBT nbt, String key, int def) {
		if (nbt.hasTag(key, NBTType.NBTTagInt)) return nbt.getInteger(key);
		if (nbt.hasTag(key, NBTType.NBTTagFloat)) return nbt.getFloat(key).intValue();
		if (nbt.hasTag(key, NBTType.NBTTagDouble)) return nbt.getDouble(key).intValue();
		return def;
	}
	
	public static double getDoubleSafe(ReadableNBT nbt, String key, double def) {
		if (nbt.hasTag(key, NBTType.NBTTagDouble)) return nbt.getDouble(key);
		if (nbt.hasTag(key, NBTType.NBTTagInt)) return nbt.getInteger(key).doubleValue();
		if (nbt.hasTag(key, NBTType.NBTTagFloat)) return nbt.getFloat(key).doubleValue();
		return def;
	}
	
	public static float getFloatSafe(ReadableNBT nbt, String key, float def) {
		if (nbt.hasTag(key, NBTType.NBTTagFloat)) return nbt.getFloat(key);
		if (nbt.hasTag(key, NBTType.NBTTagInt)) return nbt.getInteger(key).floatValue();
		if (nbt.hasTag(key, NBTType.NBTTagDouble)) return nbt.getDouble(key).floatValue();
		return def;
	}
}