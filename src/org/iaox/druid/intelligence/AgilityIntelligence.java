package org.iaox.druid.intelligence;

import org.iaox.druid.Simple;
import org.iaox.druid.assignment.Assignment;
import org.iaox.druid.assignment.AssignmentType;
import org.iaox.druid.assignment.agility.AgilityAssignment;
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

public class AgilityIntelligence {

	private MethodProvider methodProvider;
	private AgilityAssignment agilityAssignment;
	private Skill skill;
	private int experienceGoal;
	private int currentLevel;
	private int currentExperience;

	private int level;
	private IaoxInventory requiredInventory;

	public AgilityIntelligence(MethodProvider methodProvider) {
		this.methodProvider = methodProvider;
	}

	/**
	 * Perform some checks to see that we are maximizing the assignment Such as:
	 * check for best axe TODO - Hop worlds if crowded etc.
	 */
	public void check() {
		//nothing to check right now
	}

	/**
	 * Generate a new Task Get random skill Get the experience that shall be
	 * achieved during the task
	 * 
	 * @return the new Task
	 */
	public Task generateNewTask() {
		skill = Skill.AGILITY;
		experienceGoal = getExperienceGoal(skill);
		return new Task(skill, experienceGoal, createAgilityAssignment());
	}
	

	public Assignment createAgilityAssignment() {
		agilityAssignment = getAppropiateAssignment();
		/*
		 * If agility course is left to white wolf mountain, add cammy teleport to required inventory
		 */
		if(agilityAssignment == AgilityAssignment.GNOME_COURSE){
			requiredInventory = RequiredInventories.getGnomeInventory(methodProvider);
			return new Assignment(agilityAssignment, AssignmentType.AGILITY, requiredInventory, null);
		}
		return null;
	}



	private AgilityAssignment getAppropiateAssignment() {
		return AgilityAssignment.GNOME_COURSE;
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
	
	public Task getTask(){
		return Simple.TASK_HANDLER.getCurrentTask();
	}

}
