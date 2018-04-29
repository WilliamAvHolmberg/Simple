package org.iaox.druid.gear;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.iaox.druid.Simple;
import org.iaox.druid.data.IaoxItem;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.script.MethodProvider;

public class IaoxEquipment {
	
	private List<RequiredEquipment> requiredEquipment;
	private MethodProvider methodProvider;

	/**
	 * IaoxEquipmentis an object that contains information about which equipment that are required
	 * For instance, it can be used in a fightAssignment when either one or more items are required 
	 * for the assignment
	 * @param Arrays.asList(requiredItems)
	 */
	public IaoxEquipment(RequiredEquipment[] requiredEquipment, MethodProvider methodProvider){
		this.requiredEquipment = Arrays.asList(requiredEquipment);
		this.methodProvider = methodProvider;
	}
	
	public List<RequiredEquipment> getRequiredEquipment(){
		return requiredEquipment;
	}
	
	/**
	 * Valid equipment means that the player has the required gear for the specific task.
	 * @return
	 */
	public boolean hasValidEquipment(){
		//loop through each slot
		//if requiredEquipemtn exists for that slot
		//check if requiredEquipment is equipped
		//if requiredEquipment does not exist
		//check if bestEquipment() is equipped
		methodProvider.log(requiredEquipment);
		for(RequiredEquipment equipment : requiredEquipment){
			methodProvider.log(equipment.getItemName());
			if(!methodProvider.equipment.isWearingItem(equipment.getSlot(), equipment.getItemName())){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * @return a list of items that are required but that player does not have in his inventory
	 */
	public List<RequiredEquipment> getNeededItems(){
		List<RequiredEquipment> neededItems = new ArrayList<RequiredEquipment>();
		for(RequiredEquipment item : requiredEquipment){
			if(methodProvider.equipment.isWearingItem(item.getSlot(), item.getItemName())){
				neededItems.add(item);
			}
		}
		return neededItems;
	}
	/**
	 * @return a complete list of all item ids that are required
	 */
	private List<Integer> getRequiredItemIDs(){
		List<Integer> requiredItemIDs = new ArrayList<Integer>();
		requiredEquipment.forEach(item -> {
			requiredItemIDs.add(item.getItemID());
		});
		return requiredItemIDs;
	}
	
	/**
	 * Shall return a list of items that shall be deposited from the players inventory
	 * @return a complete list of items that inventory contains which is not required
	 */
	public List<Item> getUnecessaryItems(){
		List<Item> unecessaryItems = new ArrayList<Item>();
		//get all items from inventory
		Item[] inventoryItems = methodProvider.inventory.getItems();
		//check if inventory contains any items
		if(inventoryItems != null && inventoryItems.length > 0 && getRequiredItemIDs() != null && !getRequiredItemIDs().isEmpty()){
		//loop through each item and check if it is a required item
			for(Item item : inventoryItems){
				//if it is not a required item ,it shall be added to the list of unecessary items
				if(item != null && !getRequiredItemIDs().contains(item.getId()))
					unecessaryItems.add(item);
			}
		}
		return unecessaryItems;
	}
	
	
	public boolean inventoryContainsUnecessaryItems(){
		List<Item> unecessaryItems = getUnecessaryItems();
		return unecessaryItems != null && !unecessaryItems.isEmpty();
	}
	

}
