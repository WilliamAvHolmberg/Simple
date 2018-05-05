package org.iaox.druid.task;

import org.iaox.druid.assignment.Assignment;
import org.iaox.druid.assignment.woodcutting.WoodcuttingAssignment;
import org.osbot.rs07.api.ui.Skill;

public class Task {
	
	private Skill skill;
	private int experienceGoal;
	private Assignment assignment;
	
	public Task(Skill skill, int experienceGoal, Assignment assignment) {
		this.skill = skill;
		this.experienceGoal = experienceGoal;
		this.assignment = assignment;
	}
	
	public Skill getSkill() {
		return skill;
	}
	
	public int getExperienceGoal() {
		return experienceGoal;
	}
	
	public Assignment getAssignment() {
		return assignment;
	}
	
	public String toString() {
		return skill + " -> " + experienceGoal;
	}

}
