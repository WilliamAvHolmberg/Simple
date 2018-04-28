package org.iaox.druid.travel;

import java.util.Arrays;
import java.util.List;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;

public class TravelException {
	
	private TravelType type;
	private Area area;
	private List<Position> path;
	private String teleportName;
	private Area webWalkArea;

	public TravelException(TravelType type, Area area, Position[] path){
		this.type = type;
		this.area = area;
		this.path = Arrays.asList(path);
	}
	
	public TravelException(TravelType type, Area area, String teleportName){
		this.type = type;
		this.area = area;
		this.teleportName = teleportName;
	}
	
	public TravelException(TravelType type, Area area, Area webWalkArea){
		this.type = type;
		this.area = area;
		this.webWalkArea = webWalkArea;
	}
	
	public TravelType getType(){
		return type;
	}
	
	public Area getArea(){
		return area;
	}
	
	public List<Position> getPath(){
		return path;
	}
	
	public Area getWebWalkArea(){
		return webWalkArea;
	}
	
	public String teleportName(){
		return teleportName;
	}

	public String getTeleportName() {
		return teleportName;
	}
	
	

}
