package org.iaox.druid.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum IaoxItem {
	
	//teleport tablets and runes
	FALADOR_TELEPORT("Falador teleport", 8009),
	CAMELOT_TELEPORT("Camelot teleport", 8009),
	VARROCK_TELEPORT("Varrock teleport", 8009),
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
	TUNA("Tuna", 361);
	
	
	
	private String itemName;
	private int itemID;
	
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





}