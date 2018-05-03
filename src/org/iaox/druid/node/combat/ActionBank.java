package org.iaox.druid.node.combat;

import org.iaox.druid.Timing;
import org.iaox.druid.equipment.RequiredEquipment;
import org.iaox.druid.inventory.RequiredItem;
import org.iaox.druid.node.Node;

public class ActionBank extends Node {

	private long amountOfItemInInventory;

	@Override
	public boolean active() {
		return !combatProvider.shouldFight() && combatProvider.inBankArea();
	}

	/**
	 * Shall open bank if bank is not open Shall deposit all items that are not
	 * required Shall withdraw and equip all required equipment Shall withdraw
	 * inventory items that are required required items For the future - Perhaps add
	 * support so that the player eats up to full hp
	 */
	@Override
	public void run() {
		if (!methodProvider.bank.isOpen()) {
			openBank();
		} else if (combatProvider.inventoryContainsUnecessaryItems()) {
			depositUnecessaryItems();
		}else if (!combatProvider.hasValidEquipment()) {
			withdrawAndEquip();
		}else if (!combatProvider.hasValidInventory()) {
			withdrawInventory();
		}
	}

	/**
	 * Shall withdraw inventory items that are required
	 * Gets inventoryItems from currentAssignment
	 */
	private void withdrawInventory() {
		combatProvider.getNeededInventoryItems().forEach(item -> {
			amountOfItemInInventory = methodProvider.inventory.getAmount(item.getItemID());
			if (amountOfItemInInventory < item.getAmount()) {
				withdraw(item);
			} else {
				deposit(item);
			}
		});
	}

	/**
	 * Shall withdraw and equip items that are required from requiredEquipment
	 * Gets requiredEquipment fmor currentAsignment
	 */
	private void withdrawAndEquip() {
		combatProvider.getNeededEquipmentItems().forEach(item -> {
			if (methodProvider.inventory.contains(item.getItemID())) {
				combatProvider.equip(item);
			} else {
				withdraw(item);
			}
		});
	}

	/**
	 * Shall deposit all items that are not required for the trip.
	 * Gets unecessary items from combatProvider
	 * combatProvider gets unecessary items from checking what inventory contains
	 * And then checks if the items are either requiredInventory or requiredEquipment
	 * If the item is not required, deposit
	 */
	private void depositUnecessaryItems() {
		// Deposit all unecessary items
		combatProvider.getUnecessaryItems().forEach(item -> {
			methodProvider.bank.depositAll(item.getId());
		});

	}

	/**
	 * Shall open the bank and sleep until the bank is open
	 */
	private void openBank() {
		try {
			methodProvider.bank.open();
			Timing.waitCondition(() -> methodProvider.bank.isOpen(), 600, 5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Withdraw the required item If inventory already contain the item, but not the
	 * whole required amount Shall withdraw the 'rest'
	 * 
	 * @param item
	 */
	private void withdraw(RequiredItem item) {
		// calculate the quantity that has to be withdrawn
		int amount = (int) (item.getAmount() - methodProvider.inventory.getAmount(item.getItemID()));
		// check if bank contains the item that is going to be withdrawn
		// if bank does not contain the item, stop script.
		if (methodProvider.bank.getAmount(item.getItemID()) >= amount) {
			methodProvider.bank.withdraw(item.getItemID(), amount);
			// sleep until inventory contains the item
			Timing.waitCondition(() -> methodProvider.inventory.getAmount(item.getItemID()) == item.getAmount(), 600,
					3000);
		} else {
			methodProvider
					.log("Stop script due to bank not containing the required item: " + item.getIaoxItem().getName());
			try {
				methodProvider.getBot().getScriptExecutor().stop();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Withdraw the required equipment If inventory already contain the item, but
	 * not the whole required amount Shall withdraw the 'rest'
	 * 
	 * @param item
	 */
	private void withdraw(RequiredEquipment item) {
		// calculate the quantity that has to be withdrawn
		int amount = 1;
		// check if bank contains the item that is going to be withdrawn
		// if bank does not contain the item, stop script.
		// For the future - add item to withdraw list
		if (methodProvider.bank.getAmount(item.getItemID()) >= amount) {
			methodProvider.bank.withdraw(item.getItemID(), amount);
			// sleep until inventory contains the item
			Timing.waitCondition(() -> methodProvider.inventory.getAmount(item.getItemID()) == 1, 600, 3000);
		} else {
			methodProvider
					.log("Stop script due to bank not containing the required item: " + item.getIaoxItem().getName());
			try {
				methodProvider.getBot().getScriptExecutor().stop();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Deposit an item
	 * 
	 * @param item
	 */
	private void deposit(RequiredItem item) {
		// calculate the quantity that has to be withdrawn
		int amount = (int) (methodProvider.inventory.getAmount(item.getItemID()) - item.getAmount());
		// check if bank contains the item that is going to be withdrawn
		// if bank does not contain the item, stop script.

		methodProvider.bank.deposit(item.getItemID(), amount);
		// sleep until inventory contains the item
		Timing.waitCondition(() -> methodProvider.inventory.getAmount(item.getItemID()) == item.getAmount(), 600, 3000);
	}

}
