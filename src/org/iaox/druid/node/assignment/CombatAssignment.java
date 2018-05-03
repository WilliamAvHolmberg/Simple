	package org.iaox.druid.node.assignment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.iaox.druid.data.IaoxItem;
import org.iaox.druid.equipment.IaoxEquipment;
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
	private List<TravelException> travelExceptionsToFight;
	private List<TravelException> travelExceptionsToBank;
	

	/**
	 * In order to make the script more flexible I created CombatAssignment which
	 * Is an object that contains information about the CombatAssignment
	 * This object makes it possible to create a common/general method to kill different npcs
	 * @param npcName
	 * @param npcArea
	 * @param requiredInventory
	 * @param druidTravelExceptionsToFight if there is any case when webwalking should not be used we can use
	 * 		  a custom method called travelException that overrides the usual webwalking method
	 * @param travelExceptionsToBank if there is any case when webwalking should not be used we can use
	 * 		  a custom method called travelException that overrides the usual webwalking method	
	 */
	public CombatAssignment(FightAssignment fightAssignment, IaoxInventory requiredInventory, IaoxEquipment requiredEquipment, IaoxItem food){
		//inherited from fixed fightAssignment
		this.npcName = fightAssignment.getNpcName();
		this.npcArea = fightAssignment.getNpcArea();
		this.bankArea = fightAssignment.getBankArea();
		this.requiredInventory = fightAssignment.getRequiredInventory();
		this.requiredEquipment = fightAssignment.getRequiredEquipment();
		this.loot = fightAssignment.getLoot();
		this.travelExceptionsToFight = fightAssignment.getTravelExceptionsToFight();
		this.travelExceptionsToBank = fightAssignment.getTravelExceptionsToBank();

		//input when new CombatAssignment is created
		this.food = food;
		//if there is any specific item that user wants to require, we add them
		if(requiredInventory != null && !requiredInventory.getRequiredItems().isEmpty()) {
			requiredInventory.getRequiredItems().forEach(item -> {
				this.requiredInventory.AddItem(item);
			});
		}	
		//if there is any specific equipment that user wants to require, we add them
				if(requiredEquipment != null && !requiredEquipment.getRequiredEquipment().isEmpty()) {
					requiredEquipment.getRequiredEquipment().forEach(item -> {
						this.requiredEquipment.setEquipment(item);
					});
				}
		
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

	public List<TravelException> getTravelExceptionsToFight() {
		return travelExceptionsToFight;
	}
	
	public List<TravelException> getTravelExceptionsToBank() {
		return travelExceptionsToBank;
	}
	
	

}
