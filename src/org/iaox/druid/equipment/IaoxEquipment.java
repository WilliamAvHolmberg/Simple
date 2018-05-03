package org.iaox.druid.equipment;

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

	/**
	 * IaoxEquipmentis an object that contains information about which equipment that are required
	 * For instance, it can be used in a fightAssignment when either one or more items are required 
	 * for the assignment
	 * @param Arrays.asList(requiredItems)
	 */
	public IaoxEquipment(RequiredEquipment[] requiredEquipment){
		
		//EquipmentSlot.each
		//requiredEquipment.add(new RequiredEquipment(slot, null)
		this.requiredEquipment = new ArrayList<RequiredEquipment>();
		Arrays.asList(requiredEquipment).forEach(item -> {
			//this.requiredEquipment.setEquipment(item);
		});
	}
	
	public List<RequiredEquipment> getRequiredEquipment(){
		return requiredEquipment;
	}
	
	/**
	 * Shall remove current Equipment from requiredEquipment
	 * Shall add new Equipment
	 */
	public void setEquipment(EquipmentSlot slot, IaoxItem item) {
		//delete current Equipment from requiredEquipment
		//requiredEquipment.remove(getEquipment(slot))
		requiredEquipment.add(new RequiredEquipment(slot, item));
	}
	
	public void setEquipment(RequiredEquipment item) {
		//delete current Equipment from requiredEquipment
		//requiredEquipment.remove(getEquipment(item.getSlot()))
		requiredEquipment.add(item);
	}
	
	/**
	 * Shall return the equipment for the specific slot that is given
	 */
	public RequiredEquipment getEquipment(EquipmentSlot slot) {
		//requiredEquipment.each |equipment|
		//if equipment.getSlot() == slot
		//return equipment
		//else
		//return null
	}
	


	
	

}
