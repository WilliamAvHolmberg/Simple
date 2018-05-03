package org.iaox.druid;

public class IaoxIntelligence {

	/**
	 * Supposed to work as the "brain" of this script
	 * Make sure that the right task is done
	 * Make sure that the rigth gear is choosen
	 */
	
	
	/**
	 * Main Thread - shall be ran every 5 sec
	 * check best equipment
	 * TODO - Check how many players in area
	 * TODO - Check if player should deposit gold to mule
	 */
	
	public void loop() {
		//if !bestEquipmentIsSelected
		//setNewEquipment()
		
		//sleep 5000ms
	}
	
	/**
	 * Shall return the most appropriate equipment
	 * Depending on:
	 * Level
	 * Current Task
	 * @return
	 */
	public RequiredEquipment getBestEquipment(EquipmentSlot slot) {
		//switch (slot)
		//case (Weapon)
			//depends on attack level
			//if < 20
			//return Iron Scimitar
			//if < 30
			//return Mithril Scimitar
			//if < 40
			//return Adamant Scimitar
			//if < 70
			//return Rune Scimitar
		//case (chest
			//depends on defence level
			//if < 40
			//return monks robe
			//if < 70 && range >= 50
			//return blue d hide body
		//osv...
	}
	
	
	/**
	 * Shall check if task requires gear
	 * Shall loop through the Required Equipment and check if each item is the most appropriate
	 */
	public boolean bestEquipmentIsSelected() {
		//RequiredEquipment.each |currentEquipment|
		//bestEquipment = getBestEquipment()
		//if currentEquipment.item != bestEquipment
		//return false	
		}
	
	
	/**
	 * Shall set equipment to new best equipment
	 */
	public void setNewEquipment() {
		//RequiredEquipment.each |currentEquipment|
		//bestEquipment = getBestEquipment()
		//if currentEquipment.item != bestEquipment
		//RequiredItem.setEquipment(currentEquipment.getSlot(), bestEquipment
	}
	
}
