package org.iaox.druid.assignment.woodcutting;

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

public enum WoodcuttingAssignment {
	NORMAL_TREE_DRAYNOR_LOCATION_1(WCObjectIDs.NORMAL_TREE_ID, 1, 
									WCAreas.WHOLE_DRAYNOR_AREA, Banks.DRAYNOR, 
									TravelExceptions.NONE, TravelExceptions.NONE,
									RequiredInventories.NONE, RequiredEquipments.NONE);
	private Integer[] treeIDs;
	private int levelRequired;
	private Area treeArea;
	private Area bankArea;
	private List<TravelException> travelExceptionsToTree;
	private List<TravelException> travelExceptionsToBank;
	private IaoxInventory requiredInventory;
	private IaoxEquipment requiredEquipment;
	private IaoxItem currentAxe;
	
	WoodcuttingAssignment(Integer[] treeID, int levelRequired, Area treeArea, Area bankArea, TravelException[] travelExceptionsToTree, TravelException[] travelExceptionsToBank,  IaoxInventory requiredInventory, IaoxEquipment requiredEquipment){
		this.treeIDs = treeID;
		this.levelRequired = levelRequired;
		this.treeArea = treeArea;
		this.bankArea = bankArea;
		this.travelExceptionsToTree = Arrays.asList(travelExceptionsToTree);
		this.travelExceptionsToBank = Arrays.asList(travelExceptionsToBank);
		this.requiredInventory = requiredInventory;
		this.requiredEquipment = requiredEquipment;
	}
	
	public Integer[] getTreeIDs(){
		return treeIDs;
	}
	
	public int getLevelRequired(){
		return levelRequired;
	}
	
	public Area getTreeArea(){
		return treeArea;
	}
	
	public Area getBankArea(){
		return bankArea;
	}
	
	
	public List<TravelException> getTravelExceptionsToFight() {
		return travelExceptionsToTree;
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
		this.currentAxe = axe;
	}
	
	public IaoxItem getAxe(){
		return this.currentAxe;
	}
	
}