package org.iaox.druid.node.assignment;

import org.iaox.druid.data.Areas;
import org.iaox.druid.data.IaoxItem;
import org.iaox.druid.data.Paths;
import org.iaox.druid.gear.IaoxEquipment;
import org.iaox.druid.gear.RequiredEquipment;
import org.iaox.druid.inventory.IaoxInventory;
import org.iaox.druid.inventory.RequiredItem;
import org.iaox.druid.travel.TravelException;
import org.iaox.druid.travel.TravelType;

public class DataDruid {
	/**
	 * Data for Druid Assignment
	 */
	
	// initialize a travel exception for when the player is not in taverley dungeon
	// we want to use webwalking as long as player is not in the taverley dungeon
	public static TravelException surfaceToTaverleyDungeon = new TravelException(TravelType.WEBWALK,
			Areas.WHOLE_RUNESCAPE.getArea(), Areas.TAVERLEY_DUNGEON_STAIRS.getArea());
	// initialize a travel exception for when the player is in taverley dungeon.
	// we do not want to use webwalking in this case as it tries to go past the gate
	// that has guards
	// that is a waste of time so instead we use a custom path.
	public static TravelException taverleyDungeonToDruids = new TravelException(TravelType.PATH, Areas.TAVERLEY_DUNGEON.getArea(),
			Paths.TAVERLEY_DUNGEON_TO_DRUIDS);
	//Draft travelExceptions
	public static TravelException[] travelExceptions = new TravelException[] { surfaceToTaverleyDungeon, taverleyDungeonToDruids };

	
	// initialize all required items
	private static RequiredItem faladorTeleport = new RequiredItem(1, IaoxItem.FALADOR_TELEPORT, false, () -> false);
	// initialize the required inventory
	public static IaoxInventory druidInventory = new IaoxInventory(new RequiredItem[] { faladorTeleport });
	// initialize the required equipment
	public static IaoxEquipment druidEquipment = new IaoxEquipment(new RequiredEquipment[] {});
	
}
