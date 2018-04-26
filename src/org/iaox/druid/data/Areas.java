package org.iaox.druid.data;


import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.Entity;

public enum Areas {
	
	TAVERLEY_DRUIDS(new Area(new Position(2942,9859,0),new Position(2926,9840,0))),
	TAVERLEY_DUNGEON(new Area(new Position(2942,9859,0),new Position(2881,9794,0))),
	TAVERLEY_DUNGEON_STAIRS(new Area(new Position(2886,9795,0),new Position(2882,9800,0))),
	FALADOR(new Area(2937, 3392, 3066, 3308)),
	WHOLE_RUNESCAPE(new Area(1305, 4055, 3897, 2467));
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
