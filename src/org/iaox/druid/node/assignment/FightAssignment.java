package org.iaox.druid.node.assignment;

import java.util.Arrays;
import java.util.List;

import org.iaox.druid.data.Areas;
import org.iaox.druid.data.IaoxItem;
import org.iaox.druid.data.LootItems;
import org.iaox.druid.gear.IaoxEquipment;
import org.iaox.druid.inventory.IaoxInventory;
import org.iaox.druid.travel.TravelException;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.constants.Banks;

public enum FightAssignment {
	CHAOS_DRUIDS_TAVERLEY("Chaos druid", Areas.TAVERLEY_DRUIDS.getArea(),
							Banks.FALADOR_WEST,LootItems.CHAOS_DRUID_LOOT,
							DataDruid.travelExceptions, DataDruid.druidInventory, 
							DataDruid.druidEquipment);
	
	private String npcName;
	private Area npcArea;
	private Area bankArea;
	private List<IaoxItem> loot;
	private List<TravelException> travelExceptions;
	private IaoxInventory requiredInventory;
	private IaoxEquipment requiredEquipment;
	
	FightAssignment(String npcName, Area npcArea, Area bankArea, IaoxItem[] loot, TravelException[] travelExceptions,  IaoxInventory requiredInventory, IaoxEquipment requiredEquipment){
		this.npcName = npcName;
		this.npcArea = npcArea;
		this.loot = Arrays.asList(loot);
		this.bankArea = bankArea;
		this.travelExceptions = Arrays.asList(travelExceptions);
		this.requiredInventory = requiredInventory;
		this.requiredEquipment = requiredEquipment;
	}
	
	public String getNpcName(){
		return npcName;
	}
	
	public Area getNpcArea(){
		return npcArea;
	}
	
	public Area getBankArea(){
		return bankArea;
	}
	
	public List<IaoxItem> getLoot(){
		return loot;
	}
	
	public List<TravelException> getTravelExceptions() {
		return travelExceptions;
	}
	
	public IaoxEquipment getRequiredEquipment() {
		return requiredEquipment;
	}
	
	public IaoxInventory getRequiredInventory(){
		return requiredInventory;
	}

}
