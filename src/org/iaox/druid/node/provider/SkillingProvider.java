package org.iaox.druid.node.provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.iaox.druid.Simple;
import org.iaox.druid.Timing;
import org.iaox.druid.assignment.Assignment;
import org.iaox.druid.assignment.agility.AgilityAssignment;
import org.iaox.druid.assignment.agility.AgilityObstacle;
import org.iaox.druid.assignment.agility.GnomeData;
import org.iaox.druid.data.Areas;
import org.iaox.druid.data.IaoxItem;
import org.iaox.druid.data.LootItems;
import org.iaox.druid.data.WebBank;
import org.iaox.druid.equipment.RequiredEquipment;
import org.iaox.druid.inventory.RequiredItem;
import org.iaox.druid.loot.Loot;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.EquipmentSlot;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;

public class SkillingProvider {

	public MethodProvider methodProvider;

	private List<RequiredEquipment> equipment;
	private boolean valid;
	private ArrayList<Item> unecessaryItems;
	private ArrayList<Integer> requiredItemIDs;
	private Item[] inventoryItems;
	private ArrayList<RequiredEquipment> neededEquipmentItems;
	private ArrayList<RequiredItem> neededInventoryItems;

	public RS2Object lastInteractedObject;

	private RS2Object tree;

	public SkillingProvider(MethodProvider methodProvider) {
		this.methodProvider = methodProvider;
	}

	/**
	 * @return the current combat assignment that is initialized onStart
	 */
	public Assignment getAssignment() {
		return Simple.TASK_HANDLER.getCurrentTask().getAssignment();
	}
	
	public Area getSkillingArea(){
		return getAssignment().getActionArea();
	}

	/**
	 * @return if player is in the fight area
	 */
	public boolean inSkillingArea() {
		return getSkillingArea().contains(methodProvider.myPlayer());
	}

	/**
	 * @return if player is in the bank
	 */
	public boolean inBankArea() {
		return getBankArea().contains(methodProvider.myPlayer());
	}
	
	public Area getBankArea(){
		if(getAssignment().getBankArea() != null){
			return getAssignment().getBankArea();
		}
		return WebBank.getNearest(methodProvider).getArea();	
	}

	/**
	 * @return if player has to eat
	 */
	public boolean shouldEat() {
		return methodProvider.myPlayer().getHealthPercent() < 50;
	}

	public boolean inventoryContainFood() {
		return methodProvider.inventory.contains(getAssignment().getFightAssignment().getFood().getID());
	}



	/**
	 * Shall return true if inventory is full 
	 * 
	 * @return
	 */
	public boolean needDepositItems() {
		return methodProvider.inventory.isFull();
	}

	/**
	 * Player should perform skilling if does not need food is in player fight area does
	 * not need to deposit items
	 * 
	 * @return if player is ready to fight
	 */
	public boolean shouldPeformSkillingAction() {
		return !needDepositItems() && hasValidEquipment() && hasValidInventory();
	}

	/**
	 * Loop through each required item if player is wearing item - do nothing -
	 * PROBABLY HAVE TO FIX THIS TO MAKE IT CLEANER if player has item in
	 * inventory - equip else means that player does not have item equipped nor
	 * in inventory
	 * 
	 * @return
	 */
	public boolean hasValidEquipment() {
		equipment = getNeededEquipmentItems();
		valid = true;
		for (RequiredEquipment equipment : equipment) {
			if (equipment.getIaoxItem() == null) {
				// do not check this item :)
			}
			if (methodProvider.equipment.isWearingItem(equipment.getSlot(), equipment.getItemName())) {
				// everything is fine :)
			} else if (methodProvider.inventory.contains(equipment.getItemName())) {
				equip(equipment);
				valid = false;
			} else {
				valid = false;
			}
		}
		return valid;
	}

	/**
	 * Shall equip item Close bank if bank is open, sleep until it is closed
	 * Equip again Equip item if bank is not open
	 * 
	 * @param equipment
	 */
	public void equip(RequiredEquipment equipment) {
		if (methodProvider.bank.isOpen()) {
			methodProvider.log("closing bank");
			methodProvider.bank.close();
			Timing.waitCondition(() -> !methodProvider.bank.isOpen(), 300, 5000);
			equip(equipment);
		} else {
			methodProvider.log("Equipping item");
			methodProvider.getEquipment().equip(equipment.getSlot(), equipment.getItemName());
			Timing.waitCondition(
					() -> methodProvider.getEquipment().isWearingItem(equipment.getSlot(), equipment.getItemName()),
					300, 5000);
		}

	}



	

	private Skill getSkill() {
		return Simple.TASK_HANDLER.getCurrentTask().getSkill();
	}

	/**
	 * Method shall return the last object that we interacted with
	 * 
	 * @return the object that our player last interacted with
	 */
	@SuppressWarnings("unchecked")
	public RS2Object getLastInteractedObject() {
		return lastInteractedObject;
	}

	
	
	/**
	 * Checks if the tree that we last tried to chop still exists
	 */
	public boolean lastInteractedObjectStillExists() {
		if(lastInteractedObject != null && lastInteractedObject.exists()){
			return true;
		}
		return false;
	}

	


	
	/**
	 * @return a complete list of all item ids that are required
	 */
	private List<Integer> getRequiredItemIDs() {
		requiredItemIDs = new ArrayList<Integer>();
		getAssignment().getRequiredInventory().getRequiredItems().forEach(item -> {
			requiredItemIDs.add(item.getItemID());
		});
		getAssignment().getRequiredEquipment().getRequiredEquipment().forEach(item -> {
			if (item.getIaoxItem() != null) {
				requiredItemIDs.add(item.getItemID());
			}
		});
		return requiredItemIDs;
	}

	/**
	 * Shall return a list of items that shall be deposited from the players
	 * inventory
	 * 
	 * @return a complete list of items that inventory contains which is not
	 *         required
	 */
	public List<Item> getUnecessaryItems() {
		unecessaryItems = new ArrayList<Item>();
		// get all items from inventory
		inventoryItems = methodProvider.inventory.getItems();
		// check if inventory contains any items
		if (inventoryItems != null && inventoryItems.length > 0 && getRequiredItemIDs() != null
				&& !getRequiredItemIDs().isEmpty()) {
			// loop through each item and check if it is a required item
			for (Item item : inventoryItems) {
				// if it is not a required item ,it shall be added to the list
				// of unecessary items
				if (item != null && !getRequiredItemIDs().contains(item.getId())) {
					methodProvider.log("unecessary item" + item.getName());
					unecessaryItems.add(item);
				}
			}
		}
		return unecessaryItems;
	}

	/**
	 * Shall return if inventory contains items that are not necessary for the
	 * specific trip.
	 * 
	 * @return
	 */
	public boolean inventoryContainsUnecessaryItems() {
		unecessaryItems = (ArrayList<Item>) getUnecessaryItems();
		return unecessaryItems != null && !unecessaryItems.isEmpty();
	}

	/**
	 * Valid inventory means that the player has the exact amount of required
	 * items for the certain task.
	 * 
	 * @return
	 */
	public boolean hasValidInventory() {
		for (RequiredItem item : getAssignment().getRequiredInventory().getRequiredItems()) {
			if (!item.getException().getAsBoolean()
					&& methodProvider.inventory.getAmount(item.getItemID()) != item.getAmount()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @return a list of items that are required but that player does not have
	 *         in his inventory
	 */
	public List<RequiredEquipment> getNeededEquipmentItems() {
		neededEquipmentItems = new ArrayList<RequiredEquipment>();
		for (RequiredEquipment item : getAssignment().getRequiredEquipment().getRequiredEquipment()) {
			if (item.getIaoxItem() != null
					&& !methodProvider.equipment.isWearingItem(item.getSlot(), item.getItemName())) {
				neededEquipmentItems.add(item);
			}
		}
		return neededEquipmentItems;
	}

	/**
	 * @return a list of items that are required but that player does not have
	 *         in his inventory
	 */
	public List<RequiredItem> getNeededInventoryItems() {
		neededInventoryItems = new ArrayList<RequiredItem>();
		for (RequiredItem item : getAssignment().getRequiredInventory().getRequiredItems()) {
			if (methodProvider.inventory.getAmount(item.getItemID()) != item.getAmount()) {
				neededInventoryItems.add(item);
			}
		}
		return neededInventoryItems;
	}

	public void breakTab(Item teleport) {
		if (methodProvider.bank.isOpen()) {
			methodProvider.log("closing bank");
			methodProvider.bank.close();
			Timing.waitCondition(() -> !methodProvider.bank.isOpen(), 300, 5000);
			breakTab(teleport);
		} else {
			methodProvider.log("breaking tab");
			teleport.interact("Break");
		}
	}
	
	public AgilityAssignment getAgilityAssignment(){
		return getAssignment().getAgilityAssignment();
	}
	
	/**
	 * Check if player is in agility area
	 * Loop through each and every obstacle area and check if player is in agility area
	 * if not, return false;
	 * @return
	 */
	public boolean inAgilityArea(){
		//this checks all "roof levels" (Z coord)
		for(Area area : getAgilityAssignment().getCourseAreaAllPlanes()){
			if(playerInArea(area)){
				return true;
			}
		}

		for(AgilityObstacle course : getAssignment().getAgilityAssignment().getObstacles()){
			if(course.getArea().contains(methodProvider.myPlayer())){
				return true;
			}
		}
		return false;
	}

	public boolean playerInArea(Area area) {
		return area.contains(methodProvider.myPlayer());
	}


}
