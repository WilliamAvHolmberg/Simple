package org.iaox.druid.intelligence;

import java.util.List;

import org.iaox.druid.Simple;
import org.iaox.druid.assignment.AssignmentType;
import org.iaox.druid.data.Areas;
import org.iaox.druid.data.IaoxItem;
import org.iaox.druid.data.RequiredInventories;
import org.iaox.druid.data.TravelExceptions;
import org.iaox.druid.equipment.IaoxEquipment;
import org.iaox.druid.equipment.RequiredEquipment;
import org.iaox.druid.inventory.IaoxInventory;
import org.iaox.druid.inventory.RequiredItem;
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
	private MiningIntelligence miningIntelligence;
	private FishingIntelligence fishingIntelligence;
	private CraftingIntelligence craftingIntelligence;
	
	private AssignmentType type;
	private int randomInteger;
	public boolean RUNNING = false;
	private Task task;
	private IaoxInventory requiredInventory;


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
		this.miningIntelligence = new MiningIntelligence(methodProvider);
		this.fishingIntelligence = new FishingIntelligence(methodProvider);
		this.craftingIntelligence = new CraftingIntelligence(methodProvider);
		
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
		while (methodProvider.getClient().isLoggedIn() && Simple.RUNNING) {
			if (Simple.TASK_HANDLER.getCurrentTask() != null) {
				teleportCheck();
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
				case MINING:
					miningIntelligence.check();
					//nothing to check right now
					break;
				case FISHING:
					fishingIntelligence.check();
					//nothing to check right now
					break;
				case CRAFTING:
					craftingIntelligence.check();
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
	 * Remove teleport from req inv if not needed anymore
	 */
	private void teleportCheck() {
		
		requiredInventory = task.getAssignment().getRequiredInventory();
		if(requiredInventory.contains(IaoxItem.FALADOR_TELEPORT) && !needFaladorTeleport()){
			requiredInventory.remove(IaoxItem.FALADOR_TELEPORT);
		}
		
		if(requiredInventory.contains(IaoxItem.CAMELOT_TELEPORT) && !needCamelotTeleport()){
			requiredInventory.remove(IaoxItem.CAMELOT_TELEPORT);
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
		//Shall do combat 70% of the time
		type = generateRandomAssignmentType();
		switch(type){
		case COMBAT:
			task = combatIntelligence.generateNewTask();
			break;
		case WOODCUTTING:
			task = woodcuttingIntelligence.generateNewTask();
			break;
		case AGILITY:
			task = agilityIntelligence.generateNewTask();
			break;
		case MINING:
			task = miningIntelligence.generateNewTask();
			break;
		case FISHING:
			task = fishingIntelligence.generateNewTask();
			break;
		case CRAFTING:
			task = craftingIntelligence.generateNewTask();
			break;
		}
		handleTravelExceptions(task);
		return task;
		
	}

	private void handleTravelExceptions(Task task) {
		//handle travel exceptions for when player is on either side of white wolf mountain and should be on the other side
		
		//if player is in left side of white mountain and should be on the right side
		//add falador teleport and travel exception
		if(needFaladorTeleport()){
			//add req item fally tele
			task.getAssignment().getRequiredInventory().addItem(RequiredInventories.getLeftSideMountainInventory(methodProvider));
			//add travel exception
			task.getAssignment().addTravelExceptionToAction(TravelExceptions.leftSideOfWhiteWolfMountainToRightSide);
			task.getAssignment().addTravelExceptionToBank(TravelExceptions.leftSideOfWhiteWolfMountainToRightSide);
		}else if(needCamelotTeleport()){
			//add req item cammy tele
			task.getAssignment().getRequiredInventory().addItem(RequiredInventories.getRightSideMountainInventory(methodProvider));
			//add travel exception
			task.getAssignment().addTravelExceptionToAction(TravelExceptions.rightSideOfWhiteWolfMountainToLeftSide);
			task.getAssignment().addTravelExceptionToBank(TravelExceptions.rightSideOfWhiteWolfMountainToLeftSide);

		}
		
	}
	
	/**
	 * If original required items contains fally tele, return true
	 * If we are on l
	 * @return
	 */
	public boolean needFaladorTeleport(){
		return ((task.getAssignment().getAssignmentType() == AssignmentType.COMBAT && task.getAssignment().getFightAssignment().getRequiredInventory().contains(IaoxItem.FALADOR_TELEPORT))) || (Areas.LEFT_SIDE_OF_WHITE_MOUNTAIN.contains(methodProvider.myPlayer()) && Areas.RIGHT_SIDE_OF_WHITE_MOUNTAIN.getArea().contains(task.getAssignment().getActionArea().getRandomPosition()));
	}
	
	public boolean needCamelotTeleport(){
		return Areas.RIGHT_SIDE_OF_WHITE_MOUNTAIN.contains(methodProvider.myPlayer()) && Areas.LEFT_SIDE_OF_WHITE_MOUNTAIN.getArea().contains(task.getAssignment().getActionArea().getRandomPosition());
	}

	/**
	 * Always do agility if level is below 30
	 * Shall do combat 50% of time
	 * Agility 10%
	 * Woodcutting 20%
	 * Mining 20%
	 * @return
	 */
	private AssignmentType generateRandomAssignmentType() {
		//always train agility to level 30 first thing we do
		if(methodProvider.getSkills().getStatic(Skill.AGILITY) < 5){
			return AssignmentType.AGILITY;
		}
		
		randomInteger = Simple.random(100);
		if(randomInteger < 7 && methodProvider.myPlayer().getCombatLevel() > 52){
			return AssignmentType.WOODCUTTING;
		}else if(randomInteger < 14 && methodProvider.myPlayer().getCombatLevel() > 12){
			return AssignmentType.MINING;
		}else if(randomInteger < 21){
			return AssignmentType.FISHING;
		}else if(randomInteger < 28){
			return AssignmentType.AGILITY;
		}else if(randomInteger < 35){
			return AssignmentType.CRAFTING;
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
