package org.iaox.druid.intelligence;

import org.iaox.druid.Simple;
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
	
	private WoodcuttingIntelligence woodcuttingIntelligence;
	private CombatIntelligence combatIntelligence;
	private Object skill;

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
	}

	/**
	 * Depending on which assignment we are doing we should check different things
	 * For instance, when we are performing a combat assignment we shall check so we are equipping the right gear
	 * That is not necessary when performing a woodcutting assignment 
	 */
	public void run() {

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
				default:
					break;
				
				}
			}
			sleep(5000);
		}
	}
	
	
	/**
	 * Generate a new Task Get random skill Get the experience that shall be
	 * achieved during the task
	 * 
	 * @return the new Task
	 */
	public Task generateNewTask() {
		//TODO - get a randomized task - can either be combat or skilling
		//return combatIntelligence.generateNewTask();
		return woodcuttingIntelligence.generateNewTask();
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