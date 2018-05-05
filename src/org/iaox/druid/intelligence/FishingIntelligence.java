package org.iaox.druid.intelligence;

import org.iaox.druid.Simple;
import org.iaox.druid.assignment.Assignment;
import org.iaox.druid.assignment.AssignmentType;
import org.iaox.druid.assignment.fishing.FishingAssignment;
import org.iaox.druid.task.Task;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;

public class FishingIntelligence {

	private MethodProvider methodProvider;
	private FishingAssignment fishingAssignment;
	private Skill skill;
	private int experienceGoal;
	private int currentLevel;
	private int currentExperience;
	
	public FishingIntelligence(MethodProvider methodProvider) {
		this.methodProvider = methodProvider;
	}

	/**
	 * Perform some checks to see that we are maximizing the assignment Such as:
	 * check so that the best assignment is set TODO - Hop worlds if crowded etc.
	 */
	public void check() {
		
		if(getAssignment() != getAppropiateAssignment()){
			fishingAssignment = getAppropiateAssignment();
			getTask().getAssignment().updateFishingAssignment(fishingAssignment);
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
		skill = Skill.FISHING;
		experienceGoal = getExperienceGoal(skill);
		return new Task(skill, experienceGoal, createfishingAssignment());
	}

	public Assignment createfishingAssignment() {
		fishingAssignment = getAppropiateAssignment();
		return new Assignment(fishingAssignment, AssignmentType.FISHING, null, null);
	}

	private FishingAssignment getAppropiateAssignment() {
		return FishingAssignment.SCHRIMPS_LUBRIDGE_AREA_1;
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

	public FishingAssignment getAssignment() {
		return Simple.TASK_HANDLER.getCurrentTask().getAssignment().getFishingAssignment();
	}
	
	public Task getTask(){
		return Simple.TASK_HANDLER.getCurrentTask();
	}

}
