package org.iaox.druid.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum IaoxItem {
	
	//axes
	BRONZE_AXE("Bronze axe",1351), MITHRIL_AXE("Mithril axe", 1355),
	ADAMANT_AXE("Adamant axe", 1357), RUNE_AXE("Rune axe", 1359),
		
	//teleport tablets and runes
	FALADOR_TELEPORT("Falador teleport", 8009),
	CAMELOT_TELEPORT("Camelot teleport", 8010),
	VARROCK_TELEPORT("Varrock teleport", 8007),
	LAW_RUNE("Law rune", 563),
	NATURE_RUNE("Nature rune", 561),
	
	//range arrows
	MITHRIL_BOLTS("Mithril bolts", 9142),
	
	//herbs
	GRIMY_IRIT("Grimy irit leaf", 209),
	GRIMY_HARRALANDER("Grimy harralander", 205),
	GRIMY_AVANTOE("Grimy avantoe", 211),
	GRIMY_KWUARM("Grimy kwuarm", 213),
	GRIMY_DWARF_WEED("Grimy dwarf weed", 217),
	GRIMY_LANTADYME("Grimy lantadyme", 2485),
	GRIMY_CADANTINE("Grimy cadantine", 215),
	GRIMY_RANARR_WEED("Grimy ranarr weed", 207), 
	
	//food
	TROUT("Trout", 333),
	TUNA("Tuna", 361),

	//weapons
	IRON_SCIMITAR("Iron Scimitar", 1323),
	MITHRIL_SCIMITAR("Mithril Scimitar", 1329),
	ADAMANT_SCIMITAR("Adamant Scimitar", 1331),
	RUNE_SCIMITAR("Rune scimitar", 1333),
	
	//amulets
	AMULET_OF_STRENGTH("Amulet of strength", 1725),
	
	//capes
	BLACK_CAPE("Black cape", 1019),
	//hats
	RED_HAT("Red hat", 2910),
	
	//boots
	DESERT_BOOTS("Desert boots", 1837),
	
	//robes
	ZAMORAK_ROBE_BOTTOM("Zamorak robe", 1033),
	ZAMORAK_ROBE_TOP("Zamorak robe", 1035);
	
	
	
	private String itemName;
	private int itemID;
	private int itemPrice = 0;
	
	IaoxItem(String itemName, int itemID){
		this.itemName = itemName;
		this.itemID = itemID;
	}
	

	/**
	 * @return itemName
	 */
	public String getName(){
		return itemName;
	}
	
	/**
	 * @return itemID
	 */
	public int getID() {
		return itemID;
	}
	/**
	 * @param price to set a price of the item
	 */
	public void setItemPrice(int price) {
		this.itemPrice = price;
	}
	
	/**
	 * @return price of item, 0 as default
	 */
	public int getItemPrice() {
		return this.itemPrice;
	}
	
	/**
	 * Shall 'transform' list of IaoxItems to a list of the itemIDs
	 * @param items - IaoxItems as the datatype List
	 * @return list of itemIDs
	 */
	public static List<Integer> getItemIDS(List<IaoxItem> items){
		List<Integer> list = new ArrayList<Integer>();
		items.forEach(item ->{
			list.add(item.getID());
		});
		return list;
	}
	
	/**
	 * Shall 'transform' list of IaoxItems to a list of the itemIDs
	 * @param items - IaoxItems as the datatype IaoxItem[]
	 * @return list of itemIDs
	 */
	public static List<Integer> getItemIDS(IaoxItem[] items){
		List<Integer> list = new ArrayList<Integer>();
		Arrays.asList(items).forEach(item ->{
			list.add(item.getID());
		});
		return list;
	}


	public static IaoxItem getByName(String name) {
		for(IaoxItem item: values()) {
			if(item.getName().equals(name)) {
				return item;
			}
		}
		return null;
	}





}