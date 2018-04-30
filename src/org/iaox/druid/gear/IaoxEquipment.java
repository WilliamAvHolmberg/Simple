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
	private ArrayList<RequiredEquipment> neededItems;
	private ArrayList<Integer> requiredItemIDs;
	private ArrayList<Item> unecessaryItems;

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
		neededItems = new ArrayList<RequiredEquipment>();
		for(RequiredEquipment item : requiredEquipment){
			if(!methodProvider.equipment.isWearingItem(item.getSlot(), item.getItemName())){
				neededItems.add(item);
			}
		}
		return neededItems;
	}

	
	

}
