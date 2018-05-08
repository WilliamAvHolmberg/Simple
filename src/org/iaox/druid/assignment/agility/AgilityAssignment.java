package org.iaox.druid.assignment.agility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.iaox.druid.data.RequiredEquipments;
import org.iaox.druid.data.RequiredInventories;
import org.iaox.druid.data.TravelExceptions;
import org.iaox.druid.equipment.IaoxEquipment;
import org.iaox.druid.inventory.IaoxInventory;
import org.iaox.druid.inventory.RequiredItem;
import org.iaox.druid.travel.TravelException;
import org.osbot.rs07.api.map.Area;

public enum AgilityAssignment {
	GNOME_COURSE(GnomeData.GNOME_OBSTACLES, 1,
					GnomeData.gnomeAgilityAreaPlane0, new Area[]{GnomeData.gnomeAgilityAreaPlane0, GnomeData.gnomeAgilityAreaPlane1,GnomeData.gnomeAgilityAreaPlane2,},
					null,
					TravelExceptions.NONE, TravelExceptions.NONE,
					new IaoxInventory(new RequiredItem[]{}), RequiredEquipments.NONE);
	
	private List<AgilityObstacle> obstacles;
	private int requiredLevel;
	private Area courseArea;
	private Area bankArea;
	private List<TravelException> travelExceptionsToCourse;
	private List<TravelException> travelExceptionsToBank;
	private IaoxInventory requiredInventory;
	private IaoxEquipment requiredEquipment;
	private List<Area> courseAreaAllPlanes;


	AgilityAssignment(AgilityObstacle[] obstacles, int requiredLevel, Area courseArea, Area[] courseAreaAllPlanes, Area bankArea, TravelException[] travelExceptionsToCourse, TravelException[] travelExceptionsToBank,  IaoxInventory requiredInventory, IaoxEquipment requiredEquipment) {
		this.obstacles = Arrays.asList(obstacles);
		this.courseArea = courseArea;
		this.courseAreaAllPlanes = Arrays.asList(courseAreaAllPlanes);
		this.bankArea = bankArea;
		this.requiredLevel = requiredLevel;
		this.travelExceptionsToCourse = Arrays.asList(travelExceptionsToCourse);
		this.travelExceptionsToBank = Arrays.asList(travelExceptionsToBank);
		this.requiredInventory = requiredInventory;
		this.requiredEquipment = requiredEquipment;
	}
	
	public List<AgilityObstacle> getObstacles(){
		return obstacles;
	}
	
	public int getRequiredLevel(){
		return requiredLevel;
	}
	
	public Area getCourseArea(){
		return courseArea;
	}
	
	public List<Area> getCourseAreaAllPlanes(){
		return courseAreaAllPlanes;
	}
	
	public Area getBankArea(){
		return bankArea;
	}
	
	
	public List<TravelException> getTravelExceptionsToCourse(){
		return travelExceptionsToCourse;
	}
	
	public List<TravelException> getTravelExceptionsToBank(){
		return  travelExceptionsToBank;
	}
	
	public IaoxEquipment getRequiredEquipment() {
		return requiredEquipment;
	}
	
	public IaoxInventory getRequiredInventory(){
		return requiredInventory;
	}

}
