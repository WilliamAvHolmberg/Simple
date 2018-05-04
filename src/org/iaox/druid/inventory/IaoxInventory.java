package org.iaox.druid.inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BooleanSupplier;

import org.iaox.druid.data.IaoxItem;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.script.MethodProvider;

public class IaoxInventory {

	private List<RequiredItem> requiredItems;
	private String string;
	private boolean shallAdd;

	/**
	 * IaoxInventory is an object that contains information about which items
	 * that are required For instance, it can be used in a fightAssignment when
	 * either one or more items are required for the assignment
	 * 
	 * @param Arrays.asList(requiredItems)
	 */
	public IaoxInventory(RequiredItem[] requiredItems) {
		this.requiredItems = new ArrayList<RequiredItem>();
		Arrays.asList(requiredItems).forEach(item -> {
			addItem(item);
		});
	}

	public List<RequiredItem> getRequiredItems() {
		return requiredItems;
	}

	/**
	 * @return a complete list of all item ids that are required
	 */
	private List<Integer> getRequiredItemIDs() {
		List<Integer> requiredItemIDs = new ArrayList<Integer>();
		requiredItems.forEach(item -> {
			requiredItemIDs.add(item.getItemID());
		});
		return requiredItemIDs;
	}

	/**
	 * If user wants to add an item to requiredInventory, we use this method For
	 * instance, druidAssignment comes with one fixed requiredItem and that is
	 * falador teleport When a new combatAssignment is created, maybe the user
	 * wants to add food to the requiredInventory. That can be done by using
	 * this method.
	 */
	public void addItem(int amount, IaoxItem item, boolean isFood, BooleanSupplier exception) {
		shallAdd = true;
		// FailSafe - check so inventory does not already contain the item
		requiredItems.forEach(reqItem -> {
			if (reqItem.getItemID() == item.getID()) {
				shallAdd = false;
			}
		});
		if (shallAdd) {
			requiredItems.add(new RequiredItem(amount, item, isFood, exception));
		}
	}

	public void addItem(RequiredItem item) {
		shallAdd = true;
		// FailSafe - check so inventory does not already contain the item
		requiredItems.forEach(reqItem -> {
			if (reqItem.getItemID() == item.getItemID()) {
				shallAdd = false;
			}
		});
		if (shallAdd) {
			requiredItems.add(item);
		}
	}

	public String toString() {
		string = null;
		requiredItems.forEach(reqItem -> {
			string += reqItem.getIaoxItem().getName() + ":" + reqItem.getAmount();
		});
		return string;
	}

}
