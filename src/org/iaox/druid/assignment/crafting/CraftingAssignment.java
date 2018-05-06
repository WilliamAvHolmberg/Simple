package org.iaox.druid.assignment.crafting;

import java.util.ArrayList;
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

public enum CraftingAssignment {
	MOLTEN_GLASS(1, CraftingAreas.FURNACE_AREA, Banks.EDGEVILLE, 
					TravelExceptions.NONE, TravelExceptions.druidTravelExceptionsToBank,
					RequiredInventories.NONE, RequiredEquipments.NONE, "Net", 20);


	private int levelRequired;
	private Area craftingArea;
	private Area bankArea;
	private List<TravelException> travelExceptionsToCraftingArea;
	private List<TravelException> travelExceptionsToBank;
	private IaoxInventory requiredInventory;
	private IaoxEquipment requiredEquipment;
	private String action;
	private int xpPerAction;
	
	CraftingAssignment(int levelRequired, Area craftingArea, Area bankArea, TravelException[] travelExceptionsToCraftingArea, TravelException[] travelExceptionsToBank,  IaoxInventory requiredInventory, IaoxEquipment requiredEquipment, String action, int xpPerAction){
		this.levelRequired = levelRequired;
		this.craftingArea = craftingArea;
		this.bankArea = bankArea;
		this.travelExceptionsToCraftingArea = Arrays.asList(travelExceptionsToCraftingArea);
		this.travelExceptionsToBank = Arrays.asList(travelExceptionsToBank);
		this.requiredInventory = requiredInventory;
		this.requiredEquipment = requiredEquipment;
		this.action = action;
		this.xpPerAction = xpPerAction;
	}

	
	public int getLevelRequired(){
		return levelRequired;
	}
	
	public Area getCraftingArea(){
		return craftingArea;
	}
	
	public Area getBankArea(){
		return bankArea;
	}
	
	public List<TravelException> getTravelExceptionsToCraftingArea() {
		return  travelExceptionsToCraftingArea;
	}
	
	public List<TravelException> getTravelExceptionsToBank() {
		return  travelExceptionsToBank;
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
	
	public int getXpPerAction(){
		return xpPerAction;
	}

	
}
