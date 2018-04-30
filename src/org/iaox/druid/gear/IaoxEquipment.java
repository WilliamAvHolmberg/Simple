package org.iaox.druid.gear;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BooleanSupplier;

import org.iaox.druid.Simple;
import org.iaox.druid.data.IaoxItem;
import org.iaox.druid.inventory.RequiredItem;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.ui.EquipmentSlot;
import org.osbot.rs07.script.MethodProvider;

public class IaoxEquipment {
	
	private List<RequiredEquipment> requiredEquipment;
	private ArrayList<RequiredEquipment> neededItems;

	/**
	 * IaoxEquipmentis an object that contains information about which equipment that are required
	 * For instance, it can be used in a fightAssignment when either one or more items are required 
	 * for the assignment
	 * @param Arrays.asList(requiredItems)
	 */
	public IaoxEquipment(RequiredEquipment[] requiredEquipment){
		this.requiredEquipment = new ArrayList<RequiredEquipment>();
		Arrays.asList(requiredEquipment).forEach(item -> {
			this.requiredEquipment.add(item);
		});
	}
	
	public List<RequiredEquipment> getRequiredEquipment(){
		return requiredEquipment;
	}
	
	/**
	 * TODO
	 */
	public void AddItem(EquipmentSlot slot, IaoxItem item) {
		requiredEquipment.add(new RequiredEquipment(slot, item));
	}
	
	public void AddItem(RequiredEquipment item) {
		requiredEquipment.add(item);
	}
	


	
	

}
