package org.iaox.druid.task;

import java.util.ArrayList;
import java.util.List;

import org.iaox.druid.Simple;
import org.iaox.druid.data.Areas;
import org.iaox.druid.data.IaoxItem;
import org.iaox.druid.equipment.IaoxEquipment;
import org.iaox.druid.equipment.RequiredEquipment;
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
	private Skill skill;
	private int experienceGoal;
	private FightAssignment fightAssignment;

	public TaskHandler(MethodProvider methodProvider) {
		this.methodProvider = methodProvider;
		this.taskList = new ArrayList<Task>();
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

	/**
	 * Shall return seagulls if atk || str < 30
	 * Else return chaos druids
	 */
	public FightAssignment getSuitableFightAssignment() {
		if (getLevel(Skill.STRENGTH) < 30 || getLevel(Skill.ATTACK) < 30) {
			return FightAssignment.SEAGULLS_PORT_SARIM;
		}
		return FightAssignment.CHAOS_DRUIDS_TAVERLEY;
	}

	/**
	 * Generate a new Task Get random skill Get the experience that shall be
	 * achieved during the task
	 * 
	 * @return the new Task
	 */
	public void generateNewTask() {
		skill = getRandomCombatSkill();
		experienceGoal = getExperienceGoal(skill);
		// should be createCombatAssignment(getSuitableFightAssignment())
		// createCombatAssignment should get the most appropiate food
		// get the required inventory
		// get the required gear
		// get best weapon etc...
		currentTask = new Task(skill, experienceGoal, createCombatAssignment());
	}

	/**
	 * Shall create a combat assignment
	 * gets the most suitable fight assignment
	 * depending on fightAssignment
	 * add food or not
	 * @return
	 */
	private CombatAssignment createCombatAssignment() {
		fightAssignment = getSuitableFightAssignment();
		combatAssignment = null;
		switch(fightAssignment){
		case CHAOS_DRUIDS_TAVERLEY:
			//in the future, use a method like getFood() to get most suitable food
			food = new RequiredItem(getFoodAmount(), IaoxItem.TROUT, true, () -> fightAssignment.getNpcArea().contains(methodProvider.myPlayer()) && methodProvider.myPlayer().getHealthPercent() > 40);
			fightAssignment.getRequiredInventory().addItem(food);
			combatAssignment = new CombatAssignment(getSuitableFightAssignment(), null, null, food.getIaoxItem());
			break;
		case SEAGULLS_PORT_SARIM:
			//no food required
			combatAssignment = new CombatAssignment(getSuitableFightAssignment(), null, null, null);
			break;
		}

		return combatAssignment;
	}

	/**
	 * Generate how much experience that shall be achieved
	 * 
	 * @param skill
	 * @return experience that should be achieved
	 */
	private int getExperienceGoal(Skill skill) {
		int currentLevel = methodProvider.getSkills().getStatic(skill);
		int currentExperience = methodProvider.getSkills().getExperience(skill);
		switch (skill) {
		case ATTACK:
		case DEFENCE:
			if (currentLevel < 10) {
				return currentExperience + 500 + Simple.random(1000);
			}
			if (currentLevel < 20) {
				return currentExperience + 1500 + Simple.random(2000);
			}
			if (currentLevel < 30) {
				return currentExperience + 2000 + Simple.random(3000);
			}
			if (currentLevel < 40) {
				return currentExperience + 3000 + Simple.random(4000);
			}
			if (currentLevel < 50) {
				return currentExperience + 4000 + Simple.random(5000);
			}
			if (currentLevel < 60) {
				return currentExperience + 4500 + Simple.random(7500);
			}
			return currentExperience + 5000 + Simple.random(10000);

		case STRENGTH:
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
		return 0;

	}

	public Skill getRandomCombatSkill() {
		int task = Simple.random(1, 3);
		//if strength level is below attack level
		if (methodProvider.getSkills().getStatic(Skill.ATTACK) >= methodProvider.getSkills()
				.getStatic(Skill.STRENGTH)) {
			return Skill.STRENGTH;
		}
		
		//if str level is 30 and atk is not 30
		//We want the bot to get 30 atk 30 str as soon as possible
		if (methodProvider.getSkills().getStatic(Skill.ATTACK) < 30 &&
				methodProvider.getSkills().getStatic(Skill.STRENGTH) >= 30 ) {
			return Skill.STRENGTH;
		}
		switch (task) {
		case 1:
			return Skill.ATTACK;
		case 2:
			return Skill.STRENGTH;
		case 3:
			if(Simple.TRAIN_DEFENCE){
				return Skill.DEFENCE;
			}else{
				return Skill.STRENGTH;
			}
		default:
			return Skill.STRENGTH;

		}
	}

	/**
	 * generate a new druidAssignment
	 * 
	 * @return
	 */
	private CombatAssignment getDruidAssignment() {
		// exception: if player is in fight place and does not have to eat, then
		// food is not required
		food = new RequiredItem(getFoodAmount(), IaoxItem.TROUT, true,
				() -> Areas.TAVERLEY_DRUIDS.contains(methodProvider.myPlayer())
						&& methodProvider.myPlayer().getHealthPercent() > 40);
		// initialize the required inventory
		inventory = new IaoxInventory(new RequiredItem[] { food });
		// intialize all required equipments
		weapon = new RequiredEquipment(EquipmentSlot.WEAPON, getBestWeapon());
		// initialize the required equipment
		equipment = new IaoxEquipment(new RequiredEquipment[] { weapon });

		return new CombatAssignment(FightAssignment.CHAOS_DRUIDS_TAVERLEY, inventory, equipment, IaoxItem.TROUT);

	}

	/**
	 * Return the appropriate weapon TODO add more weapons
	 * 
	 * @return
	 */
	private IaoxItem getBestWeapon() {
		return IaoxItem.RUNE_SCIMITAR;
	}

	/**
	 * Depending on which defence level you are, different amount of food is
	 * required TODO Depending on which task you are gonna do :)
	 * 
	 * @return
	 */
	private int getFoodAmount() {
		if (getLevel(Skill.DEFENCE) < 10) {
			return 12;
		} else if (getLevel(Skill.DEFENCE) < 20) {
			return 7;
		} else if (getLevel(Skill.DEFENCE) < 30) {
			return 6;
		} else if (getLevel(Skill.DEFENCE) < 40) {
			return 2;
		}
		return 1;
	}

	public int getLevel(Skill skill) {
		return methodProvider.skills.getStatic(skill);
	}

	public int getExperience(Skill skill) {
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
