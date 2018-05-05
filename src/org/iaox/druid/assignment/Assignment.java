	package org.iaox.druid.assignment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.iaox.druid.assignment.agility.AgilityAssignment;
import org.iaox.druid.assignment.combat.FightAssignment;
import org.iaox.druid.assignment.fishing.FishingAssignment;
import org.iaox.druid.assignment.mining.MiningAssignment;
import org.iaox.druid.assignment.woodcutting.WoodcuttingAssignment;
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
	private WoodcuttingAssignment woodcuttingAssignment;
	private AgilityAssignment agilityAssignment;
	private MiningAssignment miningAssignment;
	private FishingAssignment fishingAssignment;
	
	

	/**
	 * In order to make the script more flexible I created Assignment which
	 * Is an object that contains information about the current Assignment
	 * This object makes it possible to create a common/general method assign different assignments such as combat or skilling
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
	
	
	public Assignment(WoodcuttingAssignment woodcuttingAssignment, AssignmentType assignmentType,IaoxInventory requiredInventory, IaoxEquipment requiredEquipment){
		this.woodcuttingAssignment = woodcuttingAssignment;
		//inherited from fixed fightAssignment
		this.assignmentType = assignmentType;
		this.actionArea = woodcuttingAssignment.getTreeArea();
		this.bankArea = woodcuttingAssignment.getBankArea();
		this.requiredInventory = woodcuttingAssignment.getRequiredInventory();
		this.requiredEquipment = woodcuttingAssignment.getRequiredEquipment();
		this.travelExceptionsToAction = woodcuttingAssignment.getTravelExceptionsToTree();
		this.travelExceptionsToBank = woodcuttingAssignment.getTravelExceptionsToBank();

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
	
	public Assignment(MiningAssignment miningAssignment, AssignmentType assignmentType,IaoxInventory requiredInventory, IaoxEquipment requiredEquipment){
		this.miningAssignment = miningAssignment;
		//inherited from fixed fightAssignment
		this.assignmentType = assignmentType;
		this.actionArea = miningAssignment.getRockArea();
		this.bankArea = miningAssignment.getBankArea();
		this.requiredInventory = miningAssignment.getRequiredInventory();
		this.requiredEquipment = miningAssignment.getRequiredEquipment();
		this.travelExceptionsToAction = miningAssignment.getTravelExceptionsToRock();
		this.travelExceptionsToBank = miningAssignment.getTravelExceptionsToBank();

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
	
	
	public Assignment(AgilityAssignment agilityAssignment, AssignmentType assignmentType,IaoxInventory requiredInventory, IaoxEquipment requiredEquipment){
		this.agilityAssignment = agilityAssignment;
		//inherited from fixed fightAssignment
		this.assignmentType = assignmentType;
		this.actionArea = agilityAssignment.getCourseArea();
		this.bankArea = agilityAssignment.getBankArea();
		this.requiredInventory = agilityAssignment.getRequiredInventory();
		this.requiredEquipment = agilityAssignment.getRequiredEquipment();
		this.travelExceptionsToAction = agilityAssignment.getTravelExceptionsToCourse();
		this.travelExceptionsToBank = agilityAssignment.getTravelExceptionsToBank();

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
	
	public Assignment(FishingAssignment fishingAssignment, AssignmentType assignmentType,IaoxInventory requiredInventory, IaoxEquipment requiredEquipment){
		this.fishingAssignment = fishingAssignment;
		//inherited from fixed fightAssignment
		this.assignmentType = assignmentType;
		this.actionArea = fishingAssignment.getFishingArea();
		this.bankArea = fishingAssignment.getBankArea();
		this.requiredInventory = fishingAssignment.getRequiredInventory();
		this.requiredEquipment = fishingAssignment.getRequiredEquipment();
		this.travelExceptionsToAction = fishingAssignment.getTravelExceptionsToFishingSpot();
		this.travelExceptionsToBank = fishingAssignment.getTravelExceptionsToBank();

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


	public Area getActionArea(){
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

	public WoodcuttingAssignment getWoodcuttingAssignment() {
		return woodcuttingAssignment;
	}

	public void updateWoodcuttingAssignment(WoodcuttingAssignment newAssignment) {
		woodcuttingAssignment = newAssignment;
	}
	
	public AgilityAssignment getAgilityAssignment() {
		return agilityAssignment;
	}
	
	public MiningAssignment getMiningAssignment() {
		return miningAssignment;
	}

	public void updateMiningAssignment(MiningAssignment newAssignment) {
		miningAssignment = newAssignment;
	}
	
	public FishingAssignment getFishingAssignment() {
		return fishingAssignment;
	}

	public void updateFishingAssignment(FishingAssignment newAssignment) {
		fishingAssignment = newAssignment;
	}
	
	
	
	public AssignmentType getAssignmentType(){
		return assignmentType;
	}





	
	

}
