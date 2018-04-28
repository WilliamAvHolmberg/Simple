package org.iaox.druid.loot;

import org.iaox.druid.data.IaoxItem;

public class Loot {

	private IaoxItem item;
	private int amount;
	
	public Loot(IaoxItem item, int amount) {
		this.item = item;
		this.amount = amount;
	}
	
	public IaoxItem getItem() {
		return item;
	}
	
	public int getAmount() {
		return amount;
	}

}
