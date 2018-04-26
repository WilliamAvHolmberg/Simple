package org.iaox.druid.inventory;

import java.util.function.BooleanSupplier;

import org.iaox.druid.data.IaoxItem;

public class RequiredItem {
	
	private int amount;
	private IaoxItem iaoxItem;
	private boolean isFood;
	private BooleanSupplier exception;
	
	/**
	 * A RequiredItem is an object that contains an amount and an IaoxItem
	 * It is used,for instance, in IaoxInventory
	 * IaoxInventory can contain multiple RequiredItems
	 * In order to know how many (amount) and which item (IaoxItem), we use RequiredItem
	 * @param amount
	 * @param iaoxItem
	 * @param isFood
	 * @param exception if there is any scenario when the item is not required
	 * for instance, food is not required when the player is in fight area and does not have to eat.
	 */
	public RequiredItem(int amount, IaoxItem iaoxItem, boolean isFood, BooleanSupplier exception){
		this.amount = amount;
		this.iaoxItem = iaoxItem;
		this.isFood = isFood;
		this.exception = exception;
	}
	
	public int getAmount(){
		return amount;
	}
	
	public IaoxItem getIaoxItem(){
		return iaoxItem;
	}
	
	public int getItemID(){
		return iaoxItem.getID();
	}

	public boolean isFood() {
		return isFood;
	}

	public BooleanSupplier getException() {
		return exception;
	}
	
	

}
