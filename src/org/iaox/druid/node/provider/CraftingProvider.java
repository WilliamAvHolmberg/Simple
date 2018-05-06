package org.iaox.druid.node.provider;

import java.util.Arrays;

import org.iaox.druid.Timing;
import org.iaox.druid.data.Areas;
import org.iaox.druid.data.IaoxItem;
import org.iaox.druid.data.WebBank;
import org.iaox.druid.inventory.RequiredItem;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.script.MethodProvider;

public class CraftingProvider {

	private MethodProvider methodProvider;
	private SkillingProvider skillingProvider;
	private RS2Object furnace;
	private String selectedItem;

	public CraftingProvider(MethodProvider methodProvider, SkillingProvider skillingProvider) {
		this.methodProvider = methodProvider;
		this.skillingProvider = skillingProvider;
	}

	/**
	 * @return if player is in the bank
	 */
	public boolean inBankArea() {
		return getBankArea().contains(methodProvider.myPlayer());
	}

	/**
	 * If player is on right side of white mountain, we have to find closest
	 * bank
	 * 
	 * @return
	 */
	public Area getBankArea() {
		if (Areas.RIGHT_SIDE_OF_WHITE_MOUNTAIN.contains(methodProvider.myPlayer())) {
			return skillingProvider.getAssignment().getBankArea();
		}
		return WebBank.getNearest(methodProvider).getArea();
	}




	public void craftMoltenGlass() {
		if (methodProvider.myPlayer().isAnimating()) {
			Timing.waitCondition(() -> methodProvider.myPlayer().isAnimating(), 300, 6000);
		} else if (widgetIsVisible(270, 14)) {
			makeAll();
		} else if (itemIsSelected(IaoxItem.BUCKET_OF_SAND)) {
			interactFurnace();
		} else {
			selectItem(IaoxItem.BUCKET_OF_SAND);
		}
	}

	public boolean itemIsSelected(IaoxItem item) {
		selectedItem = methodProvider.inventory.getSelectedItemName();
		return selectedItem != null && selectedItem.equals(item.getName());
	}
	
	/** 
	 * Select item in inventory
	 */
	public void selectItem(IaoxItem item){
		methodProvider.log("Lets select item: " + item.getName());
		selectedItem = methodProvider.inventory.getSelectedItemName();
		//if we are not already selecting an item
		if(selectedItem == null){
		methodProvider.inventory.interact("Use", item.getID());
		}
		//if the selected item is not the supposed one
		else if(!selectedItem.equals(item.getName())){
			methodProvider.inventory.deselectItem();
		}
		Timing.waitCondition(() -> itemIsSelected(item), 300, 6000);
	}

	/**
	 * Interact with the furnance and sleep until widget "make all" is visible
	 */
	public void interactFurnace() {
		furnace = methodProvider.objects.closest(16469);
		furnace.interact("Use");
		Timing.waitCondition(() -> methodProvider.widgets.isVisible(270, 14), 300, 6000);
	}

	public boolean widgetIsVisible(int root, int child) {
		return methodProvider.widgets.isVisible(root, child);
	}

	/**
	 * Interact with furnace and sleep until player does not have raw material
	 */
	public void makeAll() {
		methodProvider.log("lets make all");
		methodProvider.widgets.interact(270, 14, "Make");
		// when inv does not contain raw material anymore, break
		Timing.waitCondition(() -> !inventoryContainsRawMaterial(), 300, 6000);
	}

	public boolean inventoryContainsRawMaterial() {
		for (RequiredItem item : skillingProvider.getAssignment().getRequiredInventory().getRequiredItems()) {
			if (!item.getIaoxItem().getName().contains("teleport") && !methodProvider.inventory.contains(item.getItemID())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Player should perform skilling if it has the required inventory
	 * 
	 * @return if player is ready to craft
	 */
	public boolean shouldPeformSkillingAction() {
		return skillingProvider.hasValidEquipment() && skillingProvider.hasValidInventory();
	}
}
