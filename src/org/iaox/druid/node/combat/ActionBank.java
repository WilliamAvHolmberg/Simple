package org.iaox.druid.node.combat;

import org.iaox.druid.Timing;
import org.iaox.druid.gear.RequiredEquipment;
import org.iaox.druid.inventory.RequiredItem;
import org.iaox.druid.node.Node;

public class ActionBank extends Node{

	@Override
	public boolean active() {
		return !combatProvider.shouldFight() && combatProvider.inBankArea();
	}

	/**
	 * Shall open bank if bank is not open
	 * Shall deposit all unecessary items if inventory contains
	 * Shall withdraw all required items
	 * For the future - Perhaps add support so that the player eats up to full hp 
	 */
	@Override
	public void run() {
		if(!methodProvider.bank.isOpen()){
			openBank();
		}else if(combatProvider.getAssignment().getRequiredInventory().inventoryContainsUnecessaryItems()){
			//Deposit all unecessary items
			combatProvider.getAssignment().getRequiredInventory().getUnecessaryItems().forEach(item -> {
				methodProvider.bank.depositAll(item.getId());
			});
		}else if(!combatProvider.getAssignment().getRequiredEquipment().hasValidEquipment()){
			combatProvider.getAssignment().getRequiredEquipment().getNeededItems().forEach(item -> {
				withdraw(item);
			});
		}else if(!combatProvider.getAssignment().getRequiredInventory().hasValidInventory()){
			combatProvider.getAssignment().getRequiredInventory().getNeededItems().forEach(item -> {
				withdraw(item);
			});
		}
	}

	/**
	 * Shall open the bank and sleep until the bank is open
	 */
	private void openBank() {
		try {
			methodProvider.bank.open();
			Timing.waitCondition(() -> methodProvider.bank.isOpen(), 600,5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}

	/**
	 * Withdraw the required item
	 * If inventory already contain the item, but not the whole required amount
	 * Shall withdraw the 'rest'
	 * @param item
	 */
	private void withdraw(RequiredItem item) {
		//calculate the quantity that has to be withdrawn
		int amount = (int) (item.getAmount() - methodProvider.inventory.getAmount(item.getItemID()));
		//check if bank contains the item that is going to be withdrawn
		//if bank does not contain the item, stop script.
		//For the future - add item to withdraw list
		if(methodProvider.bank.getAmount(item.getItemID()) >= amount) {
			methodProvider.bank.withdraw(item.getItemID(), amount);
			//sleep until inventory contains the item
			Timing.waitCondition(() -> methodProvider.inventory.getAmount(item.getItemID()) == item.getAmount(), 600, 3000);		
		}else {
			methodProvider.log("Stop script due to bank not containing the required item: " + item.getIaoxItem().getName());
			try {
				methodProvider.getBot().getScriptExecutor().stop();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Withdraw the required equipment
	 * If inventory already contain the item, but not the whole required amount
	 * Shall withdraw the 'rest'
	 * @param item
	 */
	private void withdraw(RequiredEquipment item) {
		//calculate the quantity that has to be withdrawn
		int amount = 1;
		//check if bank contains the item that is going to be withdrawn
		//if bank does not contain the item, stop script.
		//For the future - add item to withdraw list
		if(methodProvider.bank.getAmount(item.getItemID()) >= amount) {
			methodProvider.bank.withdraw(item.getItemID(), amount);
			//sleep until inventory contains the item
			Timing.waitCondition(() -> methodProvider.inventory.getAmount(item.getItemID()) == 1, 600, 3000);		
		}else {
			methodProvider.log("Stop script due to bank not containing the required item: " + item.getIaoxItem().getName());
			try {
				methodProvider.getBot().getScriptExecutor().stop();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
