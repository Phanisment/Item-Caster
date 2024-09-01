import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.v1_20_R0.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class NBTUtil {
	public static NBTTagCompound getNBTTag(BukkitItemStack item) {
		ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		NBTTagCompound nbtTag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
		return nbtTag;
	}
}