package org.iaox.druid.intelligence;

import org.iaox.druid.Simple;
import org.iaox.druid.assignment.Assignment;
import org.iaox.druid.assignment.AssignmentType;
import org.iaox.druid.assignment.agility.AgilityAssignment;
import org.iaox.druid.assignment.combat.FightAssignment;
import org.iaox.druid.assignment.woodcutting.WoodcuttingAssignment;
import org.iaox.druid.data.IaoxItem;
import org.iaox.druid.data.RequiredInventories;
import org.iaox.druid.equipment.IaoxEquipment;
import org.iaox.druid.equipment.RequiredEquipment;
import org.iaox.druid.inventory.RequiredItem;
import org.iaox.druid.task.Task;
import org.osbot.rs07.api.ui.EquipmentSlot;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;

public class CombatIntelligence {

	private MethodProvider methodProvider;
	private IaoxEquipment equipment;
	private RequiredEquipment bestEquipment;
	private int level;
	private FightAssignment fightAssignment;
	private Assignment combatAssignment;
	private RequiredItem food;
	private Skill skill;
	private int experienceGoal;
	
	public CombatIntelligence(MethodProvider methodProvider) {
		this.methodProvider = methodProvider;
	}
	
	/**
	 * Perform a check so that we are have the correct equipment
	 */
	public void check() {
		if(!bestFightingEquipmentIsSelected()){
			setNewEquipment();
		}	
	}
	
	/**
	 * Generate a new Task Get random skill Get the experience that shall be
	 * achieved during the task
	 * 
	 * @return the new Task
	 */
	public Task generateNewTask() {
		skill = getRandomCombatSkill();
		experienceGoal = getExperienceGoal(skill);
		return new Task(skill, experienceGoal, createCombatAssignment());
	}


	/**
	 * Shall return the most appropriate equipment Depending on: Level Current
	 * Task
	 * 
	 * @return
	 */
	private RequiredEquipment getBestEquipment(EquipmentSlot slot) {
		switch (slot) {
		case AMULET:
			return new RequiredEquipment(slot, IaoxItem.AMULET_OF_STRENGTH);
		case ARROWS:
			break;
		case CAPE:
			level = getLevel(Skill.DEFENCE);
			if (level < 20) {
				return new RequiredEquipment(slot, IaoxItem.BLACK_CAPE);
			}
			break;
		case CHEST:
			// case (chest
			// depends on defence level
			// if < 40
			// return monks robe
			// if < 70 && range >= 50
			// return blue d hide body
			break;
		case FEET:
			break;
		case HANDS:
			break;
		case HAT:
			break;
		case LEGS:
			break;
		case RING:
			break;
		case SHIELD:
			break;
		case WEAPON:
			level = getLevel(Skill.ATTACK);
			if (level < 20) {
				return new RequiredEquipment(slot, IaoxItem.IRON_SCIMITAR);
			}
			if (level < 30) {
				return new RequiredEquipment(slot, IaoxItem.MITHRIL_SCIMITAR);
			}
			if (level < 40) {
				return new RequiredEquipment(slot, IaoxItem.ADAMANT_SCIMITAR);
			}
			if (level < 70) {
				return new RequiredEquipment(slot, IaoxItem.RUNE_SCIMITAR);
			}
			break;
		}
		return null;
	}

	/**
	 * Shall check if task requires gear Shall loop through the Required
	 * Equipment and check if each item is the most appropriate
	 * 
	 * @return
	 */
	private boolean bestFightingEquipmentIsSelected() {
		equipment = Simple.TASK_HANDLER.getCurrentTask().getAssignment().getRequiredEquipment();
		for (RequiredEquipment currentEquipment : equipment.getRequiredEquipment()) {
			bestEquipment = getBestEquipment(currentEquipment.getSlot());
			if ((currentEquipment.getIaoxItem() != null && currentEquipment.getItemID() == bestEquipment.getItemID())
					|| (currentEquipment.getIaoxItem() == null && bestEquipment != null)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Shall set equipment to new best equipment
	 */
	private void setNewEquipment() {
		equipment = Simple.TASK_HANDLER.getCurrentTask().getAssignment().getRequiredEquipment();
		for (RequiredEquipment currentEquipment : equipment.getRequiredEquipment()) {
			bestEquipment = getBestEquipment(currentEquipment.getSlot());
			if (bestEquipment != null && (currentEquipment.getIaoxItem() == null
					|| currentEquipment.getItemID() != bestEquipment.getItemID())) {
				currentEquipment.replaceItem(bestEquipment.getIaoxItem());
			}
		}
	}
	
	
	/**
	 * Shall create a combat assignment
	 * gets the most suitable fight assignment
	 * depending on fightAssignment
	 * add food or not
	 * @return
	 */
	public Assignment createCombatAssignment() {
		fightAssignment = getSuitableFightAssignment();
		fightAssignment.getRequiredInventory().getRequiredItems().forEach(item -> {
			methodProvider.log(item.getIaoxItem().getName());
		});
		combatAssignment = null;
		switch(fightAssignment){
		case CHAOS_DRUIDS_TAVERLEY:
			//in the future, use a method like getFood() to get most suitable food
			food = new RequiredItem(getFoodAmount(), IaoxItem.TROUT, true, () -> fightAssignment.getNpcArea().contains(methodProvider.myPlayer()) && !(getHealth() < 25 && !methodProvider.inventory.contains(IaoxItem.TROUT.getID())));
			fightAssignment.getRequiredInventory().addItem(food);
			//add support for teleporting if on left side of white mountain
			fightAssignment.getRequiredInventory().addItem(RequiredInventories.getLeftSideMountainInventory(methodProvider));
			fightAssignment.setFood(food.getIaoxItem());
			combatAssignment = new Assignment(fightAssignment,AssignmentType.COMBAT, null, null);
			break;
		case SEAGULLS_PORT_SARIM:
			//no food required
			//add support for teleporting if on left side of white mountain
			fightAssignment.getRequiredInventory().addItem(RequiredInventories.getLeftSideMountainInventory(methodProvider));
			
			combatAssignment = new Assignment(fightAssignment,AssignmentType.COMBAT, null, null);
			break;
		}
		return combatAssignment;
	}
	
	public int getHealth(){
		return methodProvider.getSkills().getDynamic(Skill.HITPOINTS);
	}

	/**
	 * Generate how much experience that shall be achieved
	 * 
	 * @param skill
	 * @return experience that should be achieved
	 */
	private int getExperienceGoal(Skill skill) {
		int currentLevel = getLevel(skill);
		int currentExperience = getExperience(skill);
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

	/**
	 * Shall decide which combat skill that we should train for the task.
	 * @return a random combat skill
	 */
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
	 * Depending on which defence level you are, different amount of food is
	 * required TODO Depending on which task you are gonna do :)
	 * 
	 * @return
	 */
	private int getFoodAmount() {
		if (getLevel(Skill.DEFENCE) < 10) {
			return 20;
		} else if (getLevel(Skill.DEFENCE) < 20) {
			return 7;
		} else if (getLevel(Skill.DEFENCE) < 30) {
			return 6;
		} else if (getLevel(Skill.DEFENCE) < 40) {
			return 2;
		}
		return 1;
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

	private int getLevel(Skill skill) {
		return methodProvider.skills.getStatic(skill);
	}
	
	private int getExperience(Skill skill) {
		return methodProvider.skills.getExperience(skill);
	}

	public FightAssignment getAssignment(){
		return Simple.TASK_HANDLER.getCurrentTask().getAssignment().getFightAssignment();
	}

}
