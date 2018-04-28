package org.iaox.druid.loot;

import java.util.ArrayList;
import java.util.List;

public class LootHandler {
	
	private List<Loot> loot;
	private int valueOfLoot;
	
	public LootHandler() {
		this.loot = new ArrayList<Loot>();
		this.valueOfLoot = 0;
	}
	
	/**
	 * Add loot to loot handler
	 * Add price of the loot (itemPrice * amount of item)
	 * @param loot
	 */
	public void addLoot(Loot loot) {
		this.loot.add(loot);
		valueOfLoot += loot.getItem().getItemPrice() * loot.getAmount();
	}
	public int getValueOfLoot() {
		return valueOfLoot;
	}

}
