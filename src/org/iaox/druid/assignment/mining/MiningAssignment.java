package org.iaox.druid.assignment.mining;

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

public enum MiningAssignment {
	TIN_ORE_VARROCK(MiningObjectIDs.tinRockID, 1, MiningAreas.VARROCK_TIN_1, Banks.VARROCK_WEST, TravelExceptions.leftSideOfWolfMountainToRightSide,TravelExceptions.leftSideOfWolfMountainToRightSide, RequiredInventories.NONE, RequiredEquipments.NONE);
	
	//IRON_ORE_VARROCK(MiningAreas.VARROCK_WEST_MINING_AREA, MiningAreas.VARROCK_IRON_1, Banks.VARROCK_WEST, MiningObjectIDs.ironRockID, 15),

	//GOLD_ORE_RIMMINGTON(MiningAreas.RIMMINGTON_MINING_AREA, MiningAreas.RIMMINGTON_GOLD_1, MiningAreas.PORT_SARIM_DEPOSIT_AREA, MiningObjectIDs.goldRockID, 40);
	
	private Integer[] rockIDs;
	private int levelRequired;
	private Area rockArea;
	private Area bankArea;
	private List<TravelException> travelExceptionsToRock;
	private List<TravelException> travelExceptionsToBank;
	private IaoxInventory requiredInventory;
	private IaoxEquipment requiredEquipment;
	private IaoxItem currentPickaxe;
	
	MiningAssignment(Integer[] rockIDs, int levelRequired, Area rockArea, Area bankArea, TravelException[] travelExceptionsToRock, TravelException[] travelExceptionsToBank,  IaoxInventory requiredInventory, IaoxEquipment requiredEquipment){
		this.rockIDs = rockIDs;
		this.levelRequired = levelRequired;
		this.rockArea = rockArea;
		this.bankArea = bankArea;
		this.travelExceptionsToRock = Arrays.asList(travelExceptionsToRock);
		this.travelExceptionsToBank = Arrays.asList(travelExceptionsToBank);
		this.requiredInventory = requiredInventory;
		this.requiredEquipment = requiredEquipment;
	}
	
	public Integer[] getMiningIDs(){
		return rockIDs;
	}
	
	public int getLevelRequired(){
		return levelRequired;
	}
	
	public Area getRockArea(){
		return rockArea;
	}
	
	public Area getBankArea(){
		return bankArea;
	}
	
	
	public List<TravelException> getTravelExceptionsToRock() {
		return travelExceptionsToRock;
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
	
	public void setAxe(IaoxItem axe){
		this.currentPickaxe = axe;
	}
	
	public IaoxItem getPickaxe(){
		return this.currentPickaxe;
	}
	
}
