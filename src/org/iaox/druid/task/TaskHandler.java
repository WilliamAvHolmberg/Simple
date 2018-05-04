package org.iaox.druid.task;

import java.util.ArrayList;
import java.util.List;

import org.iaox.druid.Simple;
import org.iaox.druid.assignment.Assignment;
import org.iaox.druid.assignment.AssignmentType;
import org.iaox.druid.assignment.combat.FightAssignment;
import org.iaox.druid.assignment.woodcutting.WoodcuttingAssignment;
import org.iaox.druid.data.Areas;
import org.iaox.druid.data.IaoxItem;
import org.iaox.druid.equipment.IaoxEquipment;
import org.iaox.druid.equipment.RequiredEquipment;
import org.iaox.druid.intelligence.WoodcuttingIntelligence;
import org.iaox.druid.inventory.IaoxInventory;
import org.iaox.druid.inventory.RequiredItem;
import org.iaox.druid.node.Node;
import org.osbot.rs07.api.ui.EquipmentSlot;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;

public class TaskHandler {

	private List<Task> taskList;
	private Task currentTask;
	public List<Node> nodes;
	private MethodProvider methodProvider;

	public TaskHandler(MethodProvider methodProvider) {
		this.methodProvider = methodProvider;
		this.taskList = new ArrayList<Task>();
		this.nodes = new ArrayList<Node>();
	}

	public List<Task> getTasks() {
		return taskList;
	}

	public Task getCurrentTask() {
		return currentTask;
	}

	public boolean hasTask() {
		return getCurrentTask() != null;
	}


	public void setNewTask(Task task){
		currentTask = task;
		// initialize experience tracker for strength
		methodProvider.experienceTracker.getExperienceTracker().start(currentTask.getSkill());
		setNodes(currentTask.getAssignment().getAssignmentType());
	}

	private void setNodes(AssignmentType assignmentType) {
		//reset current nodes
		nodes.clear();
		Simple.ALL_NODES.forEach(node -> {
			methodProvider.log(node.getAssignmentType());
			if(node.getAssignmentType().equals(assignmentType)){
				nodes.add(node);
			}
		});
	}

	public List<Node> getNodes(){
		return this.nodes;
	}

	private int getExperience(Skill skill) {
		return methodProvider.skills.getExperience(skill);
	}
	
	/**
	 * Task is completed when goal level is equal or above levelGoal for the
	 * task
	 * 
	 * @return
	 */
	public boolean taskIsCompleted() {
		return getExperience(currentTask.getSkill()) >= currentTask.getExperienceGoal();
	}

}
