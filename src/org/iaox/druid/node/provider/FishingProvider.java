package org.iaox.druid.node.provider;

import java.util.Arrays;

import org.iaox.druid.Timing;
import org.iaox.druid.data.Areas;
import org.iaox.druid.data.WebBank;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.script.MethodProvider;

public class FishingProvider {
	
	
	private NPC fishingSpot;
	private MethodProvider methodProvider;
	private SkillingProvider skillingProvider;
	private NPC lastInteractedObject;

	public FishingProvider(MethodProvider methodProvider, SkillingProvider skillingProvider) {
		this.methodProvider = methodProvider;
		this.skillingProvider = skillingProvider;
	}
	
	/**
	 * Method should return the closest available NPC that is Not under attack
	 * Not dead Exists In specified Area
	 * 
	 * @param name
	 *            of the NPC that we want to find
	 * @return the closest available NPC
	 */
	@SuppressWarnings("unchecked")
	public NPC getClosestAvailableFishingSpot() {
		return methodProvider.npcs.closest(npc -> Arrays.asList(skillingProvider.getAssignment().getFishingAssignment().getFishingSpotIDs()).contains(npc.getId())
				);
	}
	
	/**
	 * @return if player is in the bank
	 */
	public boolean inBankArea() {
		return getBankArea().contains(methodProvider.myPlayer());
	}
	
	/**
	 * If player is on right side of white mountain, we have to find closest bank
	 * @return
	 */
	public Area getBankArea(){
		if( Areas.RIGHT_SIDE_OF_WHITE_MOUNTAIN.contains(methodProvider.myPlayer())){
			return skillingProvider.getAssignment().getBankArea();
		}
		return WebBank.getNearest(methodProvider).getArea();	
	}
	
	/**
	 * 
	 * @return if player is fishing
	 * TODO - get mining animations
	 */
	public boolean playerIsFishing() {
		//return methodProvider.myPlayer().getAnimation() == 879 || methodProvider.myPlayer().getAnimation() == 871 || methodProvider.myPlayer().getAnimation() == 869;
		return methodProvider.myPlayer().isAnimating();
	}
	
	/**
	 * Checks if the tree that we last tried to chop still exists
	 */
	public boolean lastInteractedObjectStillExists() {
		if(lastInteractedObject != null && lastInteractedObject.exists()){
			return true;
		}
		return false;
	}



	/**
	 * Shall break mining sleep if the rock that we are cutting does not exist or when inv is full
	 */
	public void miningSleep() {
		Timing.waitCondition(() -> (!playerIsFishing()) || skillingProvider.getLastInteractedObject() == null
				|| methodProvider.inventory.isFull(), 1000, 3000);
	}

	/**
	 * This method finds the closest tree and then tries to cut it it Script is put to
	 * sleep as long as player is still moving, still attacking or target has
	 * died
	 */
	public void startFishing() {
		fishingSpot = getClosestAvailableFishingSpot();
		if(fishingSpot != null){
			methodProvider.log(fishingSpot.getActions());
		}else{
			methodProvider.log("not null");
		}
		if (fishingSpot != null && fishingSpot.interact(skillingProvider.getAssignment().getFishingAssignment().getAction())) {
			lastInteractedObject = fishingSpot;
			methodProvider.log("lets sleep");
			// Sleep until player is cutting tree or tree does not exist
			Timing.waitCondition(() -> !lastInteractedObjectStillExists() || !fishingSpot.exists(), 1000, 5000);
		}
	}

}
