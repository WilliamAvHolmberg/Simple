package org.iaox.druid.equipment;

import java.util.function.BooleanSupplier;

import org.iaox.druid.data.IaoxItem;
import org.osbot.rs07.api.ui.EquipmentSlot;

public class RequiredEquipment {
	
	private EquipmentSlot slot;
	private IaoxItem iaoxItem;
	
	/**
	 * A RequiredEquipment is an object that contains a slot and an IaoxItem
	 * It is used in IaoxEquipment
	 * IaoxEquipment can contain multiple RequiredItems
	 * In order to know which slot the item should be in, we use RequiredEquipment
	 * @param Slot
	 * @param iaoxItem
	 */
	public RequiredEquipment(EquipmentSlot slot, IaoxItem iaoxItem){
		this.slot = slot;
		this.iaoxItem = iaoxItem;
	}
	
	public EquipmentSlot getSlot(){
		return slot;
	}
	
	public IaoxItem getIaoxItem(){
		return iaoxItem;
	}
	
	public String getItemName() {
		return iaoxItem.getName();
	}
	
	public int getItemID(){
		return iaoxItem.getID();
	}	
	
	/**
	 * If IaoxIntelligence wants to replace item we use this method
	 * For instance, if we level up and want to change gear.
	 */
	public void replaceItem(IaoxItem item){
		this.iaoxItem = item;
	}

}
