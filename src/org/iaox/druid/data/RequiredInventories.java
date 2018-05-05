package org.iaox.druid.data;

import org.iaox.druid.inventory.IaoxInventory;
import org.iaox.druid.inventory.RequiredItem;
import org.osbot.rs07.script.MethodProvider;

public class RequiredInventories {
	
	/**
	 * Data for when no inventory is required
	 */
	public static IaoxInventory NONE = new IaoxInventory(new RequiredItem[]{});

	
	/**
	 * Data for Druid Assignment
	 */
	
	// initialize all required items
	private static RequiredItem faladorTeleport = new RequiredItem(1, IaoxItem.FALADOR_TELEPORT, false, () -> false);
	// initialize the required inventory
	public static IaoxInventory druidInventory = new IaoxInventory(new RequiredItem[] { faladorTeleport });


	private static RequiredItem camelotTeleport;

	
	/**
	 * Data for gnome course assignment
	 */
	
	//initialize all required items
	public static IaoxInventory getGnomeInventory(MethodProvider methodProvider){
	camelotTeleport = new RequiredItem(1, IaoxItem.CAMELOT_TELEPORT, false, () -> methodProvider.myPosition().getX() < 2874);
		return new IaoxInventory(new RequiredItem[]{camelotTeleport});
	}
	
	/**
	 * Fishing data
	 */
	
	private static RequiredItem smallFishingNet = new RequiredItem(1, IaoxItem.SMALL_FISHING_NET, false, () -> false);
	
	public static IaoxInventory SCHRIMP_INVENTORY = new IaoxInventory(new RequiredItem[] {smallFishingNet});
}
