package org.iaox.druid.node.provider;

import java.util.Arrays;

import org.iaox.druid.Timing;
import org.iaox.druid.data.Areas;
import org.iaox.druid.data.WebBank;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.script.MethodProvider;

public class MiningProvider {
	
	
	private RS2Object rock;
	private MethodProvider methodProvider;
	private SkillingProvider skillingProvider;

	public MiningProvider(MethodProvider methodProvider, SkillingProvider skillingProvider) {
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
	public RS2Object getClosestAvailableRock() {
		return methodProvider.objects.closest(object -> Arrays.asList(skillingProvider.getAssignment().getMiningAssignment().getMiningIDs()).contains(object.getId())
				&& skillingProvider.getSkillingArea().contains(object));
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
	 * @return if player is mining
	 * TODO - get mining animations
	 */
	public boolean playerIsMiningOre() {
		//return methodProvider.myPlayer().getAnimation() == 879 || methodProvider.myPlayer().getAnimation() == 871 || methodProvider.myPlayer().getAnimation() == 869;
		return methodProvider.myPlayer().isAnimating();
	}



	/**
	 * Shall break mining sleep if the rock that we are cutting does not exist or when inv is full
	 */
	public void miningSleep() {
		Timing.waitCondition(() -> (!playerIsMiningOre()) || skillingProvider.getLastInteractedObject() == null
				|| methodProvider.inventory.isFull(), 1000, 3000);
	}

	/**
	 * This method finds the closest tree and then tries to cut it it Script is put to
	 * sleep as long as player is still moving, still attacking or target has
	 * died
	 */
	public void mineNewRock() {
		rock = getClosestAvailableRock();
		if (rock != null && rock.interact("Mine")) {
			skillingProvider.lastInteractedObject = rock;
			methodProvider.log("lets sleep");
			// Sleep until player is cutting tree or tree does not exist
			Timing.waitCondition(() -> !skillingProvider.lastInteractedObjectStillExists() || !rock.exists(), 1000, 5000);
		}
	}

}
