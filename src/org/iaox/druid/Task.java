package org.iaox.druid;

import org.iaox.druid.node.assignment.CombatAssignment;
import org.osbot.rs07.api.ui.Skill;

public class Task {
	
	private Skill skill;
	private int levelGoal;
	private CombatAssignment combatAssignment;
	
	public Task(Skill skill, int levelGoal, CombatAssignment combatAssignment) {
		this.skill = skill;
		this.levelGoal = levelGoal;
		this.combatAssignment = combatAssignment;
	}
	
	public Skill getSkill() {
		return skill;
	}
	
	public int getLevelGoal() {
		return levelGoal;
	}
	
	public CombatAssignment getCombatAssignment() {
		return combatAssignment;
	}
	
	public String toString() {
		return skill + " -> " + levelGoal;
	}

}
