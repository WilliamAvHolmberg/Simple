package org.iaox.druid.inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.iaox.druid.data.IaoxItem;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.script.MethodProvider;

public class IaoxInventory {
	
	private List<RequiredItem> requiredItems;
	private MethodProvider methodProvider;

	/**
	 * IaoxInventory is an object that contains information about which items that are required
	 * For instance, it can be used in a fightAssignment when either one or more items are required 
	 * for the assignment
	 * @param Arrays.asList(requiredItems)
	 */
	public IaoxInventory(RequiredItem[] requiredItems, MethodProvider methodProvider){
		this.requiredItems = Arrays.asList(requiredItems);
		this.methodProvider = methodProvider;
	}
	
	public List<RequiredItem> getRequiredItems(){
		return requiredItems;
	}
	
	/**
	 * Valid inventory means that the player has the exact amount of required items for the certain task.
	 * @return
	 */
	public boolean hasValidInventory(){
		for(RequiredItem item : requiredItems){
			if(!item.getException().getAsBoolean() && methodProvider.inventory.getAmount(item.getItemID()) != item.getAmount()){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * @return a list of items that are required but that player does not have in his inventory
	 */
	public List<RequiredItem> getNeededItems(){
		List<RequiredItem> neededItems = new ArrayList<RequiredItem>();
		for(RequiredItem item : requiredItems){
			if(methodProvider.inventory.getAmount(item.getItemID()) != item.getAmount()){
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
		requiredItems.forEach(item -> {
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
