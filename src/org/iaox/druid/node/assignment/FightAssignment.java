package org.iaox.druid.node.assignment;

import java.util.Arrays;
import java.util.List;

import org.iaox.druid.data.Areas;
import org.iaox.druid.data.IaoxItem;
import org.iaox.druid.data.LootItems;
import org.iaox.druid.equipment.IaoxEquipment;
import org.iaox.druid.inventory.IaoxInventory;
import org.iaox.druid.travel.TravelException;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.constants.Banks;

public enum FightAssignment {
	CHAOS_DRUIDS_TAVERLEY("Chaos druid", Areas.TAVERLEY_DRUIDS.getArea(),
							Banks.FALADOR_WEST,LootItems.CHAOS_DRUID_LOOT,
							TravelExceptions.druidTravelExceptionsToFight, 
							TravelExceptions.druidTravelExceptionsToBank,
							RequiredInventories.druidInventory, 
							RequiredEquipments.NONE),
	SEAGULLS_PORT_SARIM("Seagull", Areas.SEAGULL_PORT_SARIM_NORTH.getArea(),
							Banks.DRAYNOR_VILLAGE, LootItems.NO_LOOT,
							TravelExceptions.NONE, TravelExceptions.NONE,
							RequiredInventories.NONE, RequiredEquipments.NONE);
	
	private String npcName;
	private Area npcArea;
	private Area bankArea;
	private List<IaoxItem> loot;
	private List<TravelException> travelExceptionsToFight;
	private List<TravelException> travelExceptionsToBank;
	private IaoxInventory requiredInventory;
	private IaoxEquipment requiredEquipment;
	
	FightAssignment(String npcName, Area npcArea, Area bankArea, IaoxItem[] loot, TravelException[] travelExceptionsToFight, TravelException[] travelExceptionsToBank,  IaoxInventory requiredInventory, IaoxEquipment requiredEquipment){
		this.npcName = npcName;
		this.npcArea = npcArea;
		this.loot = Arrays.asList(loot);
		this.bankArea = bankArea;
		this.travelExceptionsToFight = Arrays.asList(travelExceptionsToFight);
		this.travelExceptionsToBank = Arrays.asList(travelExceptionsToBank);
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
	
	public List<TravelException> getTravelExceptionsToFight() {
		return travelExceptionsToFight;
	}
	
	public List<TravelException> getTravelExceptionsToBank() {
		return travelExceptionsToBank;
	}
	
	public IaoxEquipment getRequiredEquipment() {
		return requiredEquipment;
	}
	
	public IaoxInventory getRequiredInventory(){
		return requiredInventory;
	}

}
