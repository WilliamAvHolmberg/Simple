package org.iaox.druid.assignment.fishing;

import java.util.Arrays;
import java.util.List;

import org.iaox.druid.data.Areas;
import org.iaox.druid.data.IaoxItem;
import org.iaox.druid.data.LootItems;
import org.iaox.druid.data.RequiredEquipments;
import org.iaox.druid.data.RequiredInventories;
import org.iaox.druid.data.TravelExceptions;
import org.iaox.druid.equipment.IaoxEquipment;
import org.iaox.druid.inventory.IaoxInventory;
import org.iaox.druid.travel.TravelException;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.constants.Banks;

public enum FishingAssignment {
	SCHRIMPS_LUBRIDGE_AREA_1(FishingData.SHRIMP_FISHING_SPOT_IDS, 1, 
									FishingAreas.SHRIMP_LUMBRIDGE_AREA_1, Banks.LUMBRIDGE_UPPER, 
									TravelExceptions.leftSideOfWolfMountainToRightSide, TravelExceptions.druidTravelExceptionsToBank,
									RequiredInventories.SCHRIMP_INVENTORY, RequiredEquipments.NONE, "Net");


	private Integer[] fishingSpotIDs;
	private int levelRequired;
	private Area treeArea;
	private Area bankArea;
	private List<TravelException> travelExceptionsToFishingSpot;
	private List<TravelException> travelExceptionsToBank;
	private IaoxInventory requiredInventory;
	private IaoxEquipment requiredEquipment;
	private String action;
	
	FishingAssignment(Integer[] fishingSpotIDs, int levelRequired, Area fishingArea, Area bankArea, TravelException[] travelExceptionsToFishingSpot, TravelException[] travelExceptionsToBank,  IaoxInventory requiredInventory, IaoxEquipment requiredEquipment, String action){
		this.fishingSpotIDs = fishingSpotIDs;
		this.levelRequired = levelRequired;
		this.treeArea = fishingArea;
		this.bankArea = bankArea;
		this.travelExceptionsToFishingSpot = Arrays.asList(travelExceptionsToFishingSpot);
		this.travelExceptionsToBank = Arrays.asList(travelExceptionsToBank);
		this.requiredInventory = requiredInventory;
		this.requiredEquipment = requiredEquipment;
		this.action = action;
	}
	
	public Integer[] getFishingSpotIDs(){
		return fishingSpotIDs;
	}
	
	public int getLevelRequired(){
		return levelRequired;
	}
	
	public Area getFishingArea(){
		return treeArea;
	}
	
	public Area getBankArea(){
		return bankArea;
	}
	
	public List<TravelException> getTravelExceptionsToFishingSpot() {
		return travelExceptionsToFishingSpot;
	}
	
	public List<TravelException> getTravelExceptionsToBank() {
		return travelExceptionsToBank;
	}
	
	public IaoxEquipment getRequiredEquipment() {
		return requiredEquipment;
	}
	
	public IaoxInventory getRequiredInventory(){
		return requiredInventory;
	}
	
	public String getAction(){
		return action;
	}

	
}
