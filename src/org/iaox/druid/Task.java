package org.iaox.druid;

import org.iaox.druid.node.assignment.CombatAssignment;
import org.osbot.rs07.api.ui.Skill;

public class Task {
	
	private Skill skill;
	private int experienceGoal;
	private CombatAssignment combatAssignment;
	
	public Task(Skill skill, int experienceGoal, CombatAssignment combatAssignment) {
		this.skill = skill;
		this.experienceGoal = experienceGoal;
		this.combatAssignment = combatAssignment;
	}
	
	public Skill getSkill() {
		return skill;
	}
	
	public int getExperienceGoal() {
		return experienceGoal;
	}
	
	public CombatAssignment getCombatAssignment() {
		return combatAssignment;
	}
	
	public String toString() {
		return skill + " -> " + experienceGoal;
	}

}
