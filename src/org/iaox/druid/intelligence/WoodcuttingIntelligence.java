package org.iaox.druid.intelligence;

import org.iaox.druid.Simple;
import org.iaox.druid.assignment.Assignment;
import org.iaox.druid.assignment.AssignmentType;
import org.iaox.druid.assignment.woodcutting.WoodcuttingAssignment;
import org.iaox.druid.data.IaoxItem;
import org.iaox.druid.equipment.IaoxEquipment;
import org.iaox.druid.equipment.RequiredEquipment;
import org.iaox.druid.inventory.IaoxInventory;
import org.iaox.druid.inventory.RequiredItem;
import org.iaox.druid.task.Task;
import org.osbot.rs07.api.ui.EquipmentSlot;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;

public class WoodcuttingIntelligence {

	private MethodProvider methodProvider;
	private IaoxItem bestAxe;
	private WoodcuttingAssignment woodcuttingAssignment;
	private Skill skill;
	private int experienceGoal;
	private int currentLevel;
	private int currentExperience;
	private IaoxEquipment equipment;
	private RequiredEquipment currentAxe;
	private IaoxInventory inventory;
	private boolean inInventory;
	private boolean foundAxe;
	private boolean replacedAxe;

	public WoodcuttingIntelligence(MethodProvider methodProvider) {
		this.methodProvider = methodProvider;
	}

	/**
	 * Perform some checks to see that we are maximizing the assignment Such as:
	 * check for best axe TODO - Hop worlds if crowded etc.
	 */
	public void check() {
		bestAxe = getAxe();

		// check so the best available axe is assigned to assignment
		if (getAssignment().getAxe().getID() != bestAxe.getID()) {
			methodProvider.log("best axe not selected");
			getAssignment().setAxe(bestAxe);
		}

		//check if the best axe is required - either in inventory or equipment
		if (!canEquipAxe() && !bestAxeIsInRequiredInventory()) {
			methodProvider.log("best axe not in inv");
			setRequiredInventory();
		}else if ( !bestAxeIsInRequiredEquipment()) {
			methodProvider.log("best axe not not in req equipment");
			setRequiredEquipment();
		}
	}

	private boolean canEquipAxe() {
		switch (getAxe()) {
		case BRONZE_AXE:
			return true;
		case MITHRIL_AXE:
			return getLevel(Skill.ATTACK) >= 20;
		case ADAMANT_AXE:
			return getLevel(Skill.ATTACK) >= 30;
		case RUNE_AXE:
			return getLevel(Skill.ATTACK) >= 40;
		default:
			return false;
		}
	}

	private boolean bestAxeIsInRequiredEquipment() {
		equipment = Simple.TASK_HANDLER.getCurrentTask().getAssignment().getRequiredEquipment();
		bestAxe = getAxe();
		if (equipment.getEquipment(EquipmentSlot.WEAPON).getIaoxItem() == null || (equipment.getEquipment(EquipmentSlot.WEAPON).getIaoxItem() != null && equipment.getEquipment(EquipmentSlot.WEAPON).getItemID() != bestAxe.getID())) {
			return false;
		}
		return true;
	}

	private boolean bestAxeIsInRequiredInventory() {
		inventory = Simple.TASK_HANDLER.getCurrentTask().getAssignment().getRequiredInventory();
		bestAxe = getAxe();
		inInventory = false;
		foundAxe = false;
		for (RequiredItem requiredItem : inventory.getRequiredItems()) {
			if (requiredItem.getIaoxItem() != null && requiredItem.getIaoxItem().name().contains("axe")) {
				// nested is bad.. but in this case, it becomes clearer
				if (requiredItem.getItemID() != bestAxe.getID()) {
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
		bestAxe = getAxe();
		inInventory = false;
		replacedAxe = false;
		for (RequiredItem requiredItem : inventory.getRequiredItems()) {
			if (requiredItem.getIaoxItem() != null && requiredItem.getIaoxItem().name().contains("axe")) {
				// nested is bad.. but in this case, it becomes clearer
				if (requiredItem.getItemID() != bestAxe.getID()) {
					// remove item from requiredInv
					inventory.getRequiredItems().remove(requiredItem);
					// add best axe to inv
					inventory.getRequiredItems().add(new RequiredItem(1, bestAxe, false, () -> false));
					replacedAxe = true;
				}
			}
		}
		if(!replacedAxe){
			inventory.getRequiredItems().add(new RequiredItem(1, bestAxe, false, () -> false));
		}
	}

	/**
	 * Shall set equipment to new best equipment
	 */
	private void setRequiredEquipment() {
		equipment = Simple.TASK_HANDLER.getCurrentTask().getAssignment().getRequiredEquipment();
		bestAxe = getAxe();
		currentAxe = equipment.getEquipment(EquipmentSlot.WEAPON);
		if (currentAxe.getIaoxItem() == null || currentAxe.getIaoxItem().getID() != bestAxe.getID()) {
			currentAxe.replaceItem(bestAxe);
		}
	}

	/**
	 * Generate a new Task Get random skill Get the experience that shall be
	 * achieved during the task
	 * 
	 * @return the new Task
	 */
	public Task generateNewTask() {
		skill = Skill.WOODCUTTING;
		experienceGoal = getExperienceGoal(skill);
		return new Task(skill, experienceGoal, createWoodcuttingAssignment());
	}

	public Assignment createWoodcuttingAssignment() {
		woodcuttingAssignment = WoodcuttingAssignment.NORMAL_TREE_DRAYNOR_LOCATION_1;
		bestAxe = getAxe();
		woodcuttingAssignment.setAxe(bestAxe);
		return new Assignment(woodcuttingAssignment, AssignmentType.WOODCUTTING, null, null);
	}

	/**
	 * @return the best available axe
	 */
	private IaoxItem getAxe() {
		if (methodProvider.getSkills().getStatic(Skill.WOODCUTTING) < 21) {
			return IaoxItem.BRONZE_AXE;
		}

		if (methodProvider.getSkills().getStatic(Skill.WOODCUTTING) < 31) {
			return IaoxItem.MITHRIL_AXE;
		}

		if (methodProvider.getSkills().getStatic(Skill.WOODCUTTING) < 41) {
			return IaoxItem.ADAMANT_AXE;
		}

		return IaoxItem.RUNE_AXE;
	}

	/**
	 * @return the best available axe
	 */
	public static IaoxItem getAxe(MethodProvider methodProvider) {
		if (methodProvider.getSkills().getStatic(Skill.WOODCUTTING) < 21) {
			return IaoxItem.BRONZE_AXE;
		}

		if (methodProvider.getSkills().getStatic(Skill.WOODCUTTING) < 31) {
			return IaoxItem.MITHRIL_AXE;
		}

		if (methodProvider.getSkills().getStatic(Skill.WOODCUTTING) < 41) {
			return IaoxItem.ADAMANT_AXE;
		}

		return IaoxItem.RUNE_AXE;
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

	public WoodcuttingAssignment getAssignment() {
		return Simple.TASK_HANDLER.getCurrentTask().getAssignment().getWoodcuttingAssignment();
	}

}
