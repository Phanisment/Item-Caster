package io.phanisment.itemcaster.util;

import io.phanisment.itemcaster.ItemCaster;
import io.phanisment.itemcaster.Constants;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Debug Utility and automatically know when is debug mode
 * or not.
 * 
 * Placeholder is use default logger formating, `{<index>}`
 */
public class DebugUtil {
	public static final Logger LOGGER = ItemCaster.getInst().getLogger();
	
	/**
	 * Prevention for Instantiation.
	*/
	private DebugUtil() {
	}
	
	public static void error(String format, Object... values) {
		if (isDebugMode()) LOGGER.log(Level.SEVERE, "DEBUG | " + format, values);
	}
	
	public static void warn(String format, Object... values) {
		if (isDebugMode()) LOGGER.log(Level.WARNING, "DEBUG | " + format, values);
	}
	
	public static void info(String format, Object... values) {
		if (isDebugMode()) LOGGER.log(Level.INFO, "DEBUG | " + format, values);
	}
	
	private static boolean isDebugMode() {
		return Constants.debugging;
	}
}