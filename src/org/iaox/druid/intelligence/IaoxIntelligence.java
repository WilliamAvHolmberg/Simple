package org.iaox.druid.intelligence;

import org.iaox.druid.Simple;
import org.iaox.druid.assignment.AssignmentType;
import org.iaox.druid.data.IaoxItem;
import org.iaox.druid.equipment.IaoxEquipment;
import org.iaox.druid.equipment.RequiredEquipment;
import org.iaox.druid.task.Task;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.ui.EquipmentSlot;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;

public class IaoxIntelligence implements Runnable {

	private MethodProvider methodProvider;
	private int level;
	private IaoxEquipment equipment;
	private RequiredEquipment bestEquipment;
	
	private CombatIntelligence combatIntelligence;
	private WoodcuttingIntelligence woodcuttingIntelligence;
	private AgilityIntelligence agilityIntelligence;

	private Object skill;
	private AssignmentType type;
	private int randomInteger;
	public boolean RUNNING = false;
	/**
	 * Supposed to work as the "brain" of this script Make sure that the right
	 * task is done Make sure that the rigth gear is choosen
	 */

	/**
	 * Main Thread - shall be ran every 5 sec check best equipment TODO - Check
	 * how many players in area TODO - Check if player should deposit gold to
	 * mule
	 */

	public IaoxIntelligence(MethodProvider methodProvider) {
		this.methodProvider = methodProvider;
		this.combatIntelligence = new CombatIntelligence(methodProvider);
		this.woodcuttingIntelligence = new WoodcuttingIntelligence(methodProvider);
		this.agilityIntelligence = new AgilityIntelligence(methodProvider);
		
		//set running to true as thread is started when intelligence is initialized
		RUNNING = true;
	}

	/**
	 * Depending on which assignment we are doing we should check different things
	 * For instance, when we are performing a combat assignment we shall check so we are equipping the right gear
	 * That is not necessary when performing a woodcutting assignment 
	 */
	public void run() {
		//running is a variable that is used in the main loop to check if this thread is running
		//set to true when this thread is started
		methodProvider.log("intelligence started");
		if(!RUNNING){
			RUNNING = true;
		}
		while (methodProvider.getClient().isLoggedIn()) {
			if (Simple.TASK_HANDLER.getCurrentTask() != null) {
				switch(Simple.TASK_HANDLER.getCurrentTask().getAssignment().getAssignmentType()){
				case COMBAT:
					combatIntelligence.check();
					break;
				case WOODCUTTING:
					woodcuttingIntelligence.check();
					//check so we are doing the best task
					//check so we got the right axe
					//if we can equip axe - add axe to requiredEquipment
					//else add axe to requiredInventory
					break;
				case AGILITY:
					agilityIntelligence.check();
					//nothing to check right now
					break;
				default:
					break;
				
				}
			}
			sleep(5000);
		}
		//set running to false when thread stops running, meaning when player logs out
		RUNNING = false;
		methodProvider.log("intelligence shut down");
	}
	
	
	/**
	 * Generate a new Task Get random skill Get the experience that shall be
	 * achieved during the task
	 * 
	 * @return the new Task
	 */
	public Task generateNewTask() {
		//TODO - get a randomized task - can either be combat or skilling
		//Shall do combat 70% of the time
		type = generateRandomAssignmentType();
		switch(type){
		case COMBAT:
			return combatIntelligence.generateNewTask();
		case WOODCUTTING:
			return woodcuttingIntelligence.generateNewTask();
		case AGILITY:
			return agilityIntelligence.generateNewTask();
		}
		return combatIntelligence.generateNewTask();
		
	}

	/**
	 * Always do agility if level is below 30
	 * Shall do combat 70% of time
	 * Agility 15%
	 * Woodcutting 15%
	 * @return
	 */
	private AssignmentType generateRandomAssignmentType() {
		//always train agility to level 30 first thing we do
		if(methodProvider.getSkills().getStatic(Skill.AGILITY) < 30){
			return AssignmentType.AGILITY;
		}
		
		randomInteger = Simple.random(100);
		if(randomInteger < 25 && methodProvider.myPlayer().getCombatLevel() > 52){
			return AssignmentType.WOODCUTTING;
		}else if(randomInteger < 50){
			return AssignmentType.AGILITY;
		}
		
		return AssignmentType.COMBAT;
	}

	public void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
