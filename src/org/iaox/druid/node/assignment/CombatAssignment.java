package org.iaox.druid.node.assignment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.iaox.druid.data.IaoxItem;
import org.iaox.druid.gear.IaoxEquipment;
import org.iaox.druid.inventory.IaoxInventory;
import org.iaox.druid.travel.TravelException;
import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.Item;

public class CombatAssignment {
	
	private String npcName;
	private Area npcArea;
	private Area bankArea;
	private IaoxInventory requiredInventory;
	private IaoxEquipment requiredEquipment;
	private IaoxItem food;
	private List<IaoxItem> loot;
	private List<TravelException> travelExceptions;
	

	/**
	 * In order to make the script more flexible I created CombatAssignment which
	 * Is an object that contains information about the CombatAssignment
	 * This object makes it possible to create a common/general method to kill different npcs
	 * @param npcName
	 * @param npcArea
	 * @param requiredInventory
	 * @param travelExceptions if there is any case when webwalking should not be used we can use
	 * 		  a custom method called travelException that overrides the usual webwalking method
	 */
	public CombatAssignment(String npcName, Area npcArea, Area bankArea, IaoxInventory requiredInventory, IaoxEquipment requiredEquipment, IaoxItem food, IaoxItem[] loot, TravelException[] travelExceptions){
		this.npcName = npcName;
		this.npcArea = npcArea;
		this.bankArea = bankArea;
		this.requiredInventory = requiredInventory;
		this.requiredEquipment = requiredEquipment;
		this.food = food;
		this.loot = Arrays.asList(loot);
		this.travelExceptions = Arrays.asList(travelExceptions);
	}
	
	public String getNpcName(){
		return npcName;
	}
	
	public Area getNpcArea(){
		return npcArea;
	}
	
	public Area getBankArea(){
		return bankArea;
	}
	
	public IaoxEquipment getRequiredEquipment() {
		return requiredEquipment;
	}
	
	public IaoxInventory getRequiredInventory(){
		return requiredInventory;
	}
	
	public IaoxItem getFood(){
		return food;
	}
	
	public List<IaoxItem> getLoot(){
		return loot;
	}

	public List<TravelException> getTravelExceptions() {
		return travelExceptions;
	}

	

	
	
	

}
