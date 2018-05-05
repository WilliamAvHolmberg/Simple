package org.iaox.druid.data;


import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.Entity;

public enum Areas {
	
	TAVERLEY_DRUIDS(new Area(new Position(2942,9859,0),new Position(2926,9840,0))),
	TAVERLEY_DUNGEON(new Area(new Position(2942,9859,0),new Position(2881,9794,0))),
	TAVERLEY_DUNGEON_STAIRS(new Area(new Position(2886,9795,0),new Position(2882,9800,0))),
	FALADOR(new Area(2937, 3392, 3066, 3308)),
	WHOLE_RUNESCAPE(new Area(1305, 4055, 3897, 2467)), 
	LEFT_SIDE_OF_WHITE_MOUNTAIN(new Area(2858, 3767, 2006, 2669)),
	RIGHT_SIDE_OF_WHITE_MOUNTAIN(new Area(2865, 3828, 3474, 2873)),
	SEAGULL_PORT_SARIM_NORTH(new Area(
		    new int[][]{
		        { 3025, 3238 },
		        { 3022, 3238 },
		        { 3022, 3242 },
		        { 3030, 3241 },
		        { 3030, 3238 },
		        { 3029, 3237 },
		        { 3034, 3238 },
		        { 3033, 3234 },
		        { 3030, 3233 },
		        { 3030, 3228 },
		        { 3026, 3228 }
		    }));
	private Area area;
	
	Areas(Area area){
		this.area = area;
	}
	
	public boolean contains(Entity entity){
		return getArea().contains(entity);
	}
	
	public Area getArea(){
		return area;
	}
	
	
}
