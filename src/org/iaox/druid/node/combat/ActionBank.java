package org.iaox.druid.node.combat;

import org.iaox.druid.Timing;
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
		int amount = (int) (item.getAmount() - methodProvider.inventory.getAmount(item.getItemID()));
		methodProvider.bank.withdraw(item.getItemID(), amount);
		Timing.waitCondition(() -> methodProvider.inventory.getAmount(item.getItemID()) == item.getAmount(), 600, 3000);		
	}

}
