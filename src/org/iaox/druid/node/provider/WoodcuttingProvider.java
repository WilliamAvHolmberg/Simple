package org.iaox.druid.node.provider;

import java.util.Arrays;

import org.iaox.druid.Timing;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.script.MethodProvider;

public class WoodcuttingProvider {
	
	
	private RS2Object tree;
	private MethodProvider methodProvider;
	private SkillingProvider skillingProvider;

	public WoodcuttingProvider(MethodProvider methodProvider, SkillingProvider skillingProvider) {
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
	public RS2Object getClosestAvailableTree() {
		return methodProvider.objects.closest(object -> Arrays.asList(skillingProvider.getAssignment().getWoodcuttingAssignment().getTreeIDs()).contains(object.getId())
				&& skillingProvider.getSkillingArea().contains(object));
	}
	
	/**
	 * 
	 * @return if player is attacking
	 */
	public boolean playerIsCuttingTree() {
		return methodProvider.myPlayer().getAnimation() == 879 || methodProvider.myPlayer().getAnimation() == 871 || methodProvider.myPlayer().getAnimation() == 869;
	}



	/**
	 * Shall break wc sleep if the tree that we are cutting does not exist or when inv is full
	 * 0 have to eat
	 */
	public void woodcuttingSleep() {
		Timing.waitCondition(() -> (!playerIsCuttingTree()) || skillingProvider.getLastInteractedObject() == null
				|| methodProvider.inventory.isFull(), 1000, 3000);
	}

	/**
	 * This method finds the closest tree and then tries to cut it it Script is put to
	 * sleep as long as player is still moving, still attacking or target has
	 * died
	 */
	public void chopNewTree() {
		tree = getClosestAvailableTree();
		if (tree != null && tree.interact("Chop down")) {
			skillingProvider.lastInteractedObject = tree;
			methodProvider.log("lets sleep");
			// Sleep until player is cutting tree or tree does not exist
			Timing.waitCondition(() -> !skillingProvider.lastInteractedObjectStillExists() || !tree.exists(), 1000, 5000);
		}
	}

}
