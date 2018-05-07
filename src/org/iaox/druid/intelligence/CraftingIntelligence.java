package org.iaox.druid.intelligence;

import org.iaox.druid.Simple;
import org.iaox.druid.assignment.Assignment;
import org.iaox.druid.assignment.AssignmentType;
import org.iaox.druid.assignment.crafting.CraftingAssignment;
import org.iaox.druid.assignment.fishing.FishingAssignment;
import org.iaox.druid.data.Areas;
import org.iaox.druid.data.IaoxItem;
import org.iaox.druid.data.RequiredInventories;
import org.iaox.druid.inventory.IaoxInventory;
import org.iaox.druid.inventory.RequiredItem;
import org.iaox.druid.task.Task;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;

public class CraftingIntelligence {

	private MethodProvider methodProvider;
	private CraftingAssignment craftingAssignment;
	private Skill skill;
	private int experienceGoal;
	private int currentLevel;
	private int currentExperience;
	private RequiredItem sodaAsh;
	private RequiredItem bucketOfSand;
	private IaoxInventory moltenGlassInventory;
	
	public CraftingIntelligence(MethodProvider methodProvider) {
		this.methodProvider = methodProvider;
	}

	/**
	 * Perform some checks to see that we are maximizing the assignment Such as:
	 * check so that the best assignment is set TODO - Hop worlds if crowded etc.
	 */
	public void check() {
		
		if(getAssignment() != getAppropiateAssignment()){
			craftingAssignment = getAppropiateAssignment();
			getTask().getAssignment().updateCraftingAssignment(craftingAssignment);
		}else{
		}
	
	}
	
	

	/**
	 * Generate a new Task Get random skill Get the experience that shall be
	 * achieved during the task
	 * 
	 * @return the new Task
	 */
	public Task generateNewTask() {
		skill = Skill.CRAFTING;
		experienceGoal = getExperienceGoal(skill);
		return new Task(skill, experienceGoal, createCraftingAssignment());
	}

	public Assignment createCraftingAssignment() {
		craftingAssignment = getAppropiateAssignment();
		//Exception - if inv contains the item, amount is not required to be set at 14
		sodaAsh = new RequiredItem(14, IaoxItem.SODA_ASH, false, () ->  (playerInLeftSideOfMountain() || craftingAssignment.getCraftingArea().contains(methodProvider.myPlayer()) && methodProvider.inventory.contains(IaoxItem.SODA_ASH.getID())));
		
		//Exception - if inv contains the item, amount is not required to be set at 14
		bucketOfSand = new RequiredItem(14, IaoxItem.BUCKET_OF_SAND, false, () -> playerInLeftSideOfMountain() || craftingAssignment.getCraftingArea().contains(methodProvider.myPlayer()) && methodProvider.inventory.contains(IaoxItem.BUCKET_OF_SAND.getID()));
		
		moltenGlassInventory = new IaoxInventory(new RequiredItem[]{sodaAsh, bucketOfSand,RequiredInventories.getLeftSideMountainInventory(methodProvider)});
		return new Assignment(craftingAssignment, AssignmentType.CRAFTING, moltenGlassInventory, null);
	}
	
	private boolean playerInLeftSideOfMountain(){
		return Areas.LEFT_SIDE_OF_WHITE_MOUNTAIN.contains(methodProvider.myPlayer());
	}

	private CraftingAssignment getAppropiateAssignment() {
		return CraftingAssignment.MOLTEN_GLASS;
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
		return currentExperience + 50;
	}

	private int getLevel(Skill skill) {
		return methodProvider.skills.getStatic(skill);
	}

	private int getExperience(Skill skill) {
		return methodProvider.skills.getExperience(skill);
	}

	public CraftingAssignment getAssignment() {
		return Simple.TASK_HANDLER.getCurrentTask().getAssignment().getCraftingAssignment();
	}
	
	public Task getTask(){
		return Simple.TASK_HANDLER.getCurrentTask();
	}

}
