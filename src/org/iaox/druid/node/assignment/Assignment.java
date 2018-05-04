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

public class Assignment {
	
	
	//common variables that all assignments shall have
	private AssignmentType assignmentType;
	private Area actionArea;
	private Area bankArea;
	private IaoxInventory requiredInventory;
	private IaoxEquipment requiredEquipment;
	private List<TravelException> travelExceptionsToAction;
	private List<TravelException> travelExceptionsToBank;
	
	//variables that are unique to each assignment
	private FightAssignment fightAssignment;
	
	

	/**
	 * In order to make the script more flexible I created CombatAssignment which
	 * Is an object that contains information about the CombatAssignment
	 * This object makes it possible to create a common/general method to kill different npcs
	 * @param npcName
	 * @param actionArea
	 * @param requiredInventory
	 * @param druidTravelExceptionsToFight if there is any case when webwalking should not be used we can use
	 * 		  a custom method called travelException that overrides the usual webwalking method
	 * @param travelExceptionsToBank if there is any case when webwalking should not be used we can use
	 * 		  a custom method called travelException that overrides the usual webwalking method	
	 */
	public Assignment(FightAssignment fightAssignment, AssignmentType assignmentType,IaoxInventory requiredInventory, IaoxEquipment requiredEquipment){
		this.fightAssignment = fightAssignment;
		//inherited from fixed fightAssignment
		this.assignmentType = assignmentType;
		this.actionArea = fightAssignment.getNpcArea();
		this.bankArea = fightAssignment.getBankArea();
		this.requiredInventory = fightAssignment.getRequiredInventory();
		this.requiredEquipment = fightAssignment.getRequiredEquipment();
		this.travelExceptionsToAction = fightAssignment.getTravelExceptionsToFight();
		this.travelExceptionsToBank = fightAssignment.getTravelExceptionsToBank();

		//if there is any specific item that user wants to require, we add them
		if(requiredInventory != null && !requiredInventory.getRequiredItems().isEmpty()) {
			requiredInventory.getRequiredItems().forEach(item -> {
				this.requiredInventory.addItem(item);
			});
		}	
		//if there is any specific equipment that user wants to require, we add them
				if(requiredEquipment != null && !requiredEquipment.getRequiredEquipment().isEmpty()) {
					requiredEquipment.getRequiredEquipment().forEach(item -> {
						this.requiredEquipment.setEquipment(item);
					});
				}
		
	}
	
	
	public Area getNpcArea(){
		return actionArea;
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

	public List<TravelException> getTravelExceptionsToFight() {
		return travelExceptionsToAction;
	}
	
	public List<TravelException> getTravelExceptionsToBank() {
		return travelExceptionsToBank;
	}


	public FightAssignment getFightAssignment() {
		return fightAssignment;
	}
	
	public AssignmentType getAssignmentType(){
		return assignmentType;
	}
	
	

}
