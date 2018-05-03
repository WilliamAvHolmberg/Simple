package org.iaox.druid.data;

import org.iaox.druid.inventory.IaoxInventory;
import org.iaox.druid.inventory.RequiredItem;

public class RequiredInventories {
	
	/**
	 * Data for when no inventory is required
	 */
	
	/**
	 * Data for Druid Assignment
	 */
	
	// initialize all required items
	private static RequiredItem faladorTeleport = new RequiredItem(1, IaoxItem.FALADOR_TELEPORT, false, () -> false);
	// initialize the required inventory
	public static IaoxInventory druidInventory = new IaoxInventory(new RequiredItem[] { faladorTeleport });

}
