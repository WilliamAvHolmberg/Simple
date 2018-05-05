package org.iaox.druid.assignment.mining;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;

public class MiningAreas {
	public static Area PORT_SARIM_DEPOSIT_AREA = new Area(new Position[] { new Position(3046, 3235, 0),
			new Position(3040, 3235, 0), new Position(3043, 3238, 0), new Position(3047, 3238, 0) });

	public static Area RIMMINGTON_MINING_AREA = new Area(
			new Position[] { new Position(2986, 3232, 0), new Position(2984, 3229, 0), new Position(2978, 3228, 0),
					new Position(2973, 3230, 0), new Position(2967, 3235, 0), new Position(2965, 3240, 0),
					new Position(2968, 3246, 0), new Position(2974, 3250, 0), new Position(2981, 3251, 0),
					new Position(2988, 3246, 0), new Position(2990, 3241, 0) });

	public static Area RIMMINGTON_TIN_1 = new Area(2984,3237,2986,3235);
	
	public static Area RIMMINGTON_IRON_1 = new Area(2981, 3234, 2982, 3233);
	public static Area RIMMINGTON_IRON_2 = new Area(2968,3239,2970,3242);
	
	public static Area RIMMINGTON_GOLD_1 = new Area(2975,3234,2977,3233);
	
	public static Area VARROCK_WEST_MINING_AREA = new Area(3186,3379,3171,3364);
	
	public static Area VARROCK_TIN_1 = new Area(3181,3378,3183,3375);
			
	public static Area VARROCK_IRON_1 = new Area(3176,3366,3174,3368);
}