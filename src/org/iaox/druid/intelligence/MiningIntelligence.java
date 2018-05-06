package org.iaox.druid.intelligence;

import org.iaox.druid.Simple;
import org.iaox.druid.assignment.Assignment;
import org.iaox.druid.assignment.AssignmentType;
import org.iaox.druid.assignment.mining.MiningAssignment;
import org.iaox.druid.assignment.woodcutting.WoodcuttingAssignment;
import org.iaox.druid.data.IaoxItem;
import org.iaox.druid.data.RequiredInventories;
import org.iaox.druid.equipment.IaoxEquipment;
import org.iaox.druid.equipment.RequiredEquipment;
import org.iaox.druid.inventory.IaoxInventory;
import org.iaox.druid.inventory.RequiredItem;
import org.iaox.druid.task.Task;
import org.osbot.rs07.api.ui.EquipmentSlot;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;

public class MiningIntelligence {

	private MethodProvider methodProvider;
	private IaoxItem bestPickaxe;
	private MiningAssignment miningAssignment;
	private Skill skill;
	private int experienceGoal;
	private int currentLevel;
	private int currentExperience;
	private IaoxEquipment equipment;
	private RequiredEquipment currentAxe;
	private IaoxInventory inventory;
	private boolean foundAxe;
	private boolean replacedAxe;
	private int level;
	private boolean inInventory;

	public MiningIntelligence(MethodProvider methodProvider) {
		this.methodProvider = methodProvider;
	}

	/**
	 * Perform some checks to see that we are maximizing the assignment Such as:
	 * check for best axe TODO - Hop worlds if crowded etc.
	 */
	public void check() {
		bestPickaxe = getPickAxe();
		// check so the best available axe is assigned to assignment
		if (getAssignment().getPickaxe().getID() != bestPickaxe.getID()) {
			methodProvider.log("best axe not selected");
			getAssignment().setAxe(bestPickaxe);
		}

		//check if the best axe is required - either in inventory or equipment
		if (!canEquipAxe() && !bestPickaxeIsInRequiredInventory()) {
			setRequiredInventory();
		}else if ( !bestPickaxeIsInRequiredEquipment()) {
			setRequiredEquipment();
		}
		
		if(getAssignment() != getAppropiateAssignment()){
			miningAssignment = getAppropiateAssignment();
			miningAssignment.setAxe(bestPickaxe);
			getTask().getAssignment().updateMiningAssignment(miningAssignment);
		}else{
		}
	
	}

	private boolean canEquipAxe() {
		switch (getPickAxe()) {
		case BRONZE_PICKAXE:
			return true;
		case MITHRIL_PICKAXE:
			return getLevel(Skill.ATTACK) >= 20;
		case ADAMANT_PICKAXE:
			return getLevel(Skill.ATTACK) >= 30;
		case RUNE_PICKAXE:
			return getLevel(Skill.ATTACK) >= 40;
		default:
			return false;
		}
	}

	private boolean bestPickaxeIsInRequiredEquipment() {
		equipment = Simple.TASK_HANDLER.getCurrentTask().getAssignment().getRequiredEquipment();
		bestPickaxe = getPickAxe();
		if (equipment.getEquipment(EquipmentSlot.WEAPON).getIaoxItem() == null || (equipment.getEquipment(EquipmentSlot.WEAPON).getIaoxItem() != null && equipment.getEquipment(EquipmentSlot.WEAPON).getItemID() != bestPickaxe.getID())) {
			return false;
		}
		return true;
	}

	private boolean bestPickaxeIsInRequiredInventory() {
		inventory = Simple.TASK_HANDLER.getCurrentTask().getAssignment().getRequiredInventory();
		bestPickaxe = getPickAxe();
		inInventory = false;
		foundAxe = false;
		for (RequiredItem requiredItem : inventory.getRequiredItems()) {
			if (requiredItem.getIaoxItem() != null && requiredItem.getIaoxItem().name().contains("pickaxe")) {
				// nested is bad.. but in this case, it becomes clearer
				if (requiredItem.getItemID() != bestPickaxe.getID()) {
					return false;
				}else{
					foundAxe = true;
				}
			}
		}
		return foundAxe;
	}
	
	private void setRequiredInventory() {
		inventory = Simple.TASK_HANDLER.getCurrentTask().getAssignment().getRequiredInventory();
		bestPickaxe = getPickAxe();
		inInventory = false;
		replacedAxe = false;
		for (RequiredItem requiredItem : inventory.getRequiredItems()) {
			if (requiredItem.getIaoxItem() != null && requiredItem.getIaoxItem().name().contains("pickaxe")) {
				// nested is bad.. but in this case, it becomes clearer
				if (requiredItem.getItemID() != bestPickaxe.getID()) {
					// remove item from requiredInv
					inventory.getRequiredItems().remove(requiredItem);
					// add best axe to inv
					inventory.getRequiredItems().add(new RequiredItem(1, bestPickaxe, false, () -> false));
					replacedAxe = true;
				}
			}
		}
		if(!replacedAxe){
			inventory.getRequiredItems().add(new RequiredItem(1, bestPickaxe, false, () -> false));
		}
	}

	/**
	 * Shall set equipment to new best equipment
	 */
	private void setRequiredEquipment() {
		equipment = Simple.TASK_HANDLER.getCurrentTask().getAssignment().getRequiredEquipment();
		bestPickaxe = getPickAxe();
		currentAxe = equipment.getEquipment(EquipmentSlot.WEAPON);
		if (currentAxe.getIaoxItem() == null || currentAxe.getIaoxItem().getID() != bestPickaxe.getID()) {
			currentAxe.replaceItem(bestPickaxe);
		}
	}

	/**
	 * Generate a new Task Get random skill Get the experience that shall be
	 * achieved during the task
	 * 
	 * @return the new Task
	 */
	public Task generateNewTask() {
		skill = Skill.MINING;
		experienceGoal = getExperienceGoal(skill);
		return new Task(skill, experienceGoal, createMiningAssignment());
	}

	public Assignment createMiningAssignment() {
		miningAssignment = getAppropiateAssignment();
		bestPickaxe = getPickAxe();
		miningAssignment.setAxe(bestPickaxe);
		return new Assignment(miningAssignment, AssignmentType.MINING, null, null);
	}

	/**
	 * TODO - Add more mining assignments
	 * @return
	 */
	private MiningAssignment getAppropiateAssignment() {
		return MiningAssignment.TIN_ORE_VARROCK;
	}

	/**
	 * @return the best available axe
	 */
	private IaoxItem getPickAxe() {
		if (methodProvider.getSkills().getStatic(Skill.MINING) < 21) {
			return IaoxItem.BRONZE_PICKAXE;
		}

		if (methodProvider.getSkills().getStatic(Skill.MINING) < 31) {
			return IaoxItem.MITHRIL_PICKAXE;
		}

		if (methodProvider.getSkills().getStatic(Skill.MINING) < 41) {
			return IaoxItem.ADAMANT_PICKAXE;
		}

		return IaoxItem.RUNE_PICKAXE;
	}



	/**
	 * Generate how much experience that shall be achieved
	 * 
	 * @param skill
	 * @return experience that should be achieved
	 */
	private int getExperienceGoal(Skill skill) {
		currentLevel = getLevel(skill);
		currentExperience = getExperience(skill);
		if (currentLevel < 10) {
			return currentExperience + 1000 + Simple.random(500);
		}
		if (currentLevel < 20) {
			return currentExperience + 2000 + Simple.random(1500);
		}
		if (currentLevel < 30) {
			return currentExperience + 3000 + Simple.random(2000);
		}
		if (currentLevel < 40) {
			return currentExperience + 3500 + Simple.random(3000);
		}
		if (currentLevel < 50) {
			return currentExperience + 4000 + Simple.random(5000);
		}
		if (currentLevel < 60) {
			return currentExperience + 5000 + Simple.random(5000);
		}
		if (currentLevel < 70) {
			return currentExperience + 7000 + Simple.random(10000);
		}
		return currentExperience + 10000 + Simple.random(15000);
	}

	private int getLevel(Skill skill) {
		return methodProvider.skills.getStatic(skill);
	}

	private int getExperience(Skill skill) {
		return methodProvider.skills.getExperience(skill);
	}

	public MiningAssignment getAssignment() {
		return Simple.TASK_HANDLER.getCurrentTask().getAssignment().getMiningAssignment();
	}
	
	public Task getTask(){
		return Simple.TASK_HANDLER.getCurrentTask();
	}

}
