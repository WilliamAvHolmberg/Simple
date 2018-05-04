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
	private String string;

	/**
	 * IaoxEquipmentis an object that contains information about which equipment
	 * that are required For instance, it can be used in a fightAssignment when
	 * either one or more items are required for the assignment
	 * 
	 * @param Arrays.asList(requiredItems)
	 */
	public IaoxEquipment(RequiredEquipment[] requiredEquipment) {

		this.requiredEquipment = new ArrayList<RequiredEquipment>();
		
		//init empty equipments
		Arrays.asList(EquipmentSlot.values()).forEach(slot -> {
			this.requiredEquipment.add(new RequiredEquipment(slot, null));
		});
		
		//init the required equipment
		Arrays.asList(requiredEquipment).forEach(item -> {
			setEquipment(item);
		});
	}

	public List<RequiredEquipment> getRequiredEquipment() {
		return requiredEquipment;
	}

	/**
	 * Shall remove current Equipment from requiredEquipment Shall add new
	 * Equipment
	 */
	public void setEquipment(EquipmentSlot slot, IaoxItem item) {
		// delete current Equipment from requiredEquipment
		requiredEquipment.remove(getEquipment(slot));
		requiredEquipment.add(new RequiredEquipment(slot, item));
	}

	public void setEquipment(RequiredEquipment item) {
		// delete current Equipment from requiredEquipment
		requiredEquipment.remove(getEquipment(item.getSlot()));
		requiredEquipment.add(item);
	}

	/**
	 * Shall return the equipment for the specific slot that is given
	 */
	public RequiredEquipment getEquipment(EquipmentSlot slot) {
		for(RequiredEquipment equipment : requiredEquipment){
			if(equipment.getSlot() == slot){
				return equipment;
			}
		}
		return null;
	}
	
	public String toString() {
		string = null;
		requiredEquipment.forEach(reqItem -> {
			if(reqItem.getIaoxItem() != null){
			string += "hello" + reqItem.getItemName();
			}
		});
		return string;
	}

}
