package org.iaox.druid.data;

import org.iaox.druid.inventory.IaoxInventory;
import org.iaox.druid.inventory.RequiredItem;
import org.osbot.rs07.script.MethodProvider;

public class RequiredInventories {
	
	private static RequiredItem teleport;
	
	/**
	 * Data for when no inventory is required
	 * not working atm
	 */
	public final static IaoxInventory NONE2 = new IaoxInventory(new RequiredItem[]{new RequiredItem(0, IaoxItem.FEATHER, false, () -> false)});

	/**
	 * Data for when player is on left side of white mountain and should be on right side
	 */
	//initialize all required items
	public static RequiredItem getLeftSideMountainInventory(MethodProvider methodProvider){
		return new RequiredItem(1, IaoxItem.FALADOR_TELEPORT, false, () -> methodProvider.myPosition().getX() > 2874);
	}
	
	/**
	 * Data for when player is on left side of white mountain and should be on right side
	 */
	//initialize all required items
	public static RequiredItem getRightSideMountainInventory(MethodProvider methodProvider){
		return new RequiredItem(1, IaoxItem.CAMELOT_TELEPORT, false, () -> methodProvider.myPosition().getX() < 2874);
	}
	
	/**
	 * Data for Druid Assignment
	 */
	
	// initialize all required items
	private static RequiredItem faladorTeleport = new RequiredItem(1, IaoxItem.FALADOR_TELEPORT, false, () -> false);
	// initialize the required inventory
	public static IaoxInventory druidInventory = new IaoxInventory(new RequiredItem[] { faladorTeleport });

	
	/**
	 * Data for gnome course assignment
	 */
	
	//initialize all required items
	public static IaoxInventory getGnomeInventory(MethodProvider methodProvider){
	teleport = new RequiredItem(1, IaoxItem.CAMELOT_TELEPORT, false, () -> methodProvider.myPosition().getX() < 2874);
		return new IaoxInventory(new RequiredItem[]{teleport});
	}
	
	/**
	 * Fishing data
	 */
	
	private static RequiredItem smallFishingNet = new RequiredItem(1, IaoxItem.SMALL_FISHING_NET, false, () -> false);
	
	public static IaoxInventory SCHRIMP_INVENTORY = new IaoxInventory(new RequiredItem[] {smallFishingNet});


}
