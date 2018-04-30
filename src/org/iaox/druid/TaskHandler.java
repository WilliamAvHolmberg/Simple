package org.iaox.druid;

import java.util.ArrayList;
import java.util.List;

import org.iaox.druid.data.Areas;
import org.iaox.druid.data.IaoxItem;
import org.iaox.druid.gear.IaoxEquipment;
import org.iaox.druid.gear.RequiredEquipment;
import org.iaox.druid.inventory.IaoxInventory;
import org.iaox.druid.inventory.RequiredItem;
import org.iaox.druid.node.assignment.CombatAssignment;
import org.iaox.druid.node.assignment.FightAssignment;
import org.osbot.rs07.api.ui.EquipmentSlot;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;

public class TaskHandler {
	
	private List<Task> taskList;
	private Task currentTask;
	private MethodProvider methodProvider;
	private RequiredItem food;
	private IaoxInventory inventory;
	private RequiredEquipment weapon;
	private IaoxEquipment equipment;
	private CombatAssignment combatAssignment;
	
	public TaskHandler(MethodProvider methodProvider) {
		this.methodProvider = methodProvider;
		this.taskList = new ArrayList<Task>();
	}
	
	public List<Task> getTasks(){
		return taskList;
	}
	
	public Task getCurrentTask() {
		return currentTask;
	}
	
	public boolean hasTask() {
		return getCurrentTask() != null;
	}
	
	public void getNewTask() {
		if(getLevel(Skill.STRENGTH) < 30 || getLevel(Skill.ATTACK) < 30) {
			//return Seagulls
		}else if(getLevel(Skill.STRENGTH) > 50) {
			currentTask = new Task(Skill.STRENGTH, 99, getDruidAssignment());
		}
	}
	
	/**
	 * generate a new druidAssignment
	 * @return
	 */
	private CombatAssignment getDruidAssignment() {
		// exception: if player is in fight place and does not have to eat, then food is not required
		food = new RequiredItem(getFoodAmount(), IaoxItem.TROUT, true,
				() -> Areas.TAVERLEY_DRUIDS.contains(methodProvider.myPlayer()) && methodProvider.myPlayer().getHealthPercent() > 40);	
		// initialize the required inventory
		inventory = new IaoxInventory(new RequiredItem[] {food});
		// intialize all required equipments
		weapon = new RequiredEquipment(EquipmentSlot.WEAPON, getBestWeapon());
		// initialize the required equipment
		equipment = new IaoxEquipment(new RequiredEquipment[] { weapon });
		
		return new CombatAssignment(FightAssignment.CHAOS_DRUIDS_TAVERLEY, inventory, equipment, IaoxItem.TROUT);

	}

	/**
	 * Return the appropriate weapon
	 * TODO add more weapons
	 * @return
	 */
	private IaoxItem getBestWeapon() {
		return IaoxItem.RUNE_SCIMITAR;
	}

	/**
	 * Depending on which defence level you are, different amount of food is required
	 * TODO Depending on which task you are gonna do :)
	 * @return
	 */
	private int getFoodAmount() {
		if(getLevel(Skill.DEFENCE) < 10) {
			return 7;
		}else if(getLevel(Skill.DEFENCE) < 20) {
			return 6;
		}else if(getLevel(Skill.DEFENCE) < 30) {
			return 4;
		}else if(getLevel(Skill.DEFENCE) < 40) {
			return 2;
		}
		return 1;
	}

	public int getLevel(Skill skill) {
		return methodProvider.skills.getStatic(skill);
	}

	/**
	 * Task is completed when goal level is equal or above levelGoal for the task
	 * @return
	 */
	public boolean taskIsCompleted() {
		return getLevel(currentTask.getSkill()) >= currentTask.getLevelGoal();
	}

}
