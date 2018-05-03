package org.iaox.druid.data;

import org.iaox.druid.travel.TravelException;
import org.iaox.druid.travel.TravelType;

public class TravelExceptions {
	
	/**
	 * Data for when no exceptions shall be handled
	 */
	
	public static TravelException[] NONE = new TravelException[] {};
	
	/**
	 * Data for druid
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
	//Draft Druid travelExceptions to Fight
	public static TravelException[] druidTravelExceptionsToFight = new TravelException[] { surfaceToTaverleyDungeon, taverleyDungeonToDruids };

	
	//initialize a travel exception for when the player is in taverley dungeon and shall teleport to falador
	public static TravelException taverleyDungeonToFalador = new TravelException(TravelType.TELEPORT,
			Areas.TAVERLEY_DRUIDS.getArea(), "Falador teleport");
	
	//Draft Druid travelExceptions to Bank
	public static TravelException[] druidTravelExceptionsToBank = new TravelException[] { taverleyDungeonToFalador};

}
