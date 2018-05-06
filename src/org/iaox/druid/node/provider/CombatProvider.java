package org.iaox.druid.node.provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.iaox.druid.Simple;
import org.iaox.druid.Timing;
import org.iaox.druid.assignment.Assignment;
import org.iaox.druid.data.Areas;
import org.iaox.druid.data.IaoxItem;
import org.iaox.druid.data.LootItems;
import org.iaox.druid.data.WebBank;
import org.iaox.druid.equipment.RequiredEquipment;
import org.iaox.druid.inventory.RequiredItem;
import org.iaox.druid.loot.Loot;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.EquipmentSlot;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;

public class CombatProvider {

	public MethodProvider methodProvider;

	private GroundItem item;
	private Item food;
	private int foodAmount;
	private NPC target;
	private int amountBeforeLoot;
	private int amountAfterLoot;
	private int lootAmount;
	private IaoxItem iaoxItem;
	private boolean equiped;
	private List<RequiredEquipment> equipment;
	private boolean valid;
	private ArrayList<Item> unecessaryItems;
	private ArrayList<Integer> requiredItemIDs;
	private Item[] inventoryItems;
	private ArrayList<RequiredEquipment> neededEquipmentItems;
	private ArrayList<RequiredItem> neededInventoryItems;

	public CombatProvider(MethodProvider methodProvider) {
		this.methodProvider = methodProvider;
	}

	/**
	 * @return the current combat assignment that is initialized onStart
	 */
	public Assignment getAssignment() {
		return Simple.TASK_HANDLER.getCurrentTask().getAssignment();
	}

	/**
	 * @return if player is in the fight area
	 */
	public boolean inFightArea() {
		return getAssignment().getActionArea().contains(methodProvider.myPlayer());
	}

	/**
	 * @return if player is in the bank
	 */
	public boolean inBankArea() {
		return getBankArea().contains(methodProvider.myPlayer());
	}

	public Area getBankArea() {
		if(Areas.LEFT_SIDE_OF_WHITE_MOUNTAIN.contains(methodProvider.myPlayer())){
			return WebBank.getNearest(methodProvider).getArea();
		}
		return getAssignment().getBankArea();
	}

	/**
	 * @return if player has to eat
	 */
	public boolean shouldEat() {
		if(methodProvider.getSkills().getStatic(Skill.HITPOINTS) < 30){
			return false;
		}
		return getHealth() < 20;
	}
	

	public boolean inventoryContainFood() {
		//if food is not set for this assignment, it means that we do not need food
		if(getAssignment().getFightAssignment().getFood() == null){
			return true;
		}
		return methodProvider.inventory.contains(getAssignment().getFightAssignment().getFood().getID());
	}

	/**
	 * Shall return true if player is in bank and does not have food Shall
	 * return true if player is in fight area and does not have food and has to
	 * eat Shall return false if player is in fight area and does not have but
	 * does not have to eat either Shall return false if player has food
	 * 
	 * @return if player need to withdraw food
	 */
	public boolean needFood() {
		if (inFightArea() && !shouldEat() && !inventoryContainFood()) {
			return false;
		}
		return !inventoryContainFood();
	}

	/**
	 * Shall return true if inventory is full and does not contain food
	 * 
	 * @return
	 */
	public boolean needDepositItems() {
		return methodProvider.inventory.isFull() && !inventoryContainFood();
	}

	/**
	 * Player should fight if does not need food is in player fight area does
	 * not need to deposit items
	 * 
	 * @return if player is ready to fight
	 */
	public boolean shouldFight() {
		return !needDepositItems() && hasValidEquipment() && hasValidInventory();
	}

	/**
	 * Loop through each required item if player is wearing item - do nothing -
	 * PROBABLY HAVE TO FIX THIS TO MAKE IT CLEANER if player has item in
	 * inventory - equip else means that player does not have item equipped nor
	 * in inventory
	 * 
	 * @return
	 */
	public boolean hasValidEquipment() {
		equipment = getNeededEquipmentItems();
		valid = true;
		for (RequiredEquipment equipment : equipment) {
			if (equipment.getIaoxItem() == null) {
				// do not check this item :)
			}
			if (methodProvider.equipment.isWearingItem(equipment.getSlot(), equipment.getItemName())) {
				// everything is fine :)
			} else if (methodProvider.inventory.contains(equipment.getItemName())) {
				equip(equipment);
				valid = false;
			} else {
				valid = false;
			}
		}
		return valid;
	}

	/**
	 * Shall equip item Close bank if bank is open, sleep until it is closed
	 * Equip again Equip item if bank is not open
	 * 
	 * @param equipment
	 */
	public void equip(RequiredEquipment equipment) {
		if (methodProvider.bank.isOpen()) {
			methodProvider.log("closing bank");
			methodProvider.bank.close();
			Timing.waitCondition(() -> !methodProvider.bank.isOpen(), 300, 5000);
			equip(equipment);
		} else {
			methodProvider.log("Equipping item");
			methodProvider.getEquipment().equip(equipment.getSlot(), equipment.getItemName());
			Timing.waitCondition(
					() -> methodProvider.getEquipment().isWearingItem(equipment.getSlot(), equipment.getItemName()),
					300, 5000);
		}

	}

	/**
	 * @return if current attack style is equal to the preferred attack style
	 */
	public boolean rightStyle() {
		if (getSkill().equals(Skill.STRENGTH) && (attackStyle() != 1)) {
			return false;
		} else if (getSkill().equals(Skill.ATTACK) && (attackStyle() != 0)) {
			return false;
		} else if (getSkill().equals(Skill.DEFENCE) && (attackStyle() != 3)) {
			return false;
		} else if (getSkill().equals(Skill.RANGED) && (attackStyle() != 1)) {
			return false;
		}
		return true;
	}

	/**
	 * Widget(593) is for the attack style/combat widget This method should
	 * change attack style to suite the preferred skill
	 */
	public void changeStyle() {
		if (methodProvider.widgets.isVisible(593)) {
			if (getSkill().equals(Skill.STRENGTH) && (attackStyle() != 1)) {
				methodProvider.mouse.click(689, 270, false);// click "train
															// strength"
			} else if (getSkill().equals(Skill.ATTACK) && (attackStyle() != 0)) {
				methodProvider.mouse.click(601, 269, false); // click "train
																// attack"
			} else if (getSkill().equals(Skill.DEFENCE) && (attackStyle() != 3)) {
				// Check if wearing weapon
				// if not wearing weapon, we press other button in attack styles
				// since there is only three alternatives then
				if (methodProvider.equipment.getItemInSlot(EquipmentSlot.WEAPON.slot) != null) {
					methodProvider.mouse.click(701, 335, false); // click "train
																	// defence"
				} else {
					methodProvider.mouse.click(599, 324, false); // click "train
																	// defence"
				}
				// def"
			} else if (getSkill().equals(Skill.RANGED) && (attackStyle() != 1)) {
				methodProvider.mouse.click(689, 270, false);// click "train
															// range"
			}

		} else {
			methodProvider.mouse.click(545, 190, false);
		}

	}

	private Skill getSkill() {
		return Simple.TASK_HANDLER.getCurrentTask().getSkill();
	}

	/**
	 * @return current attack style. 1 == STRENGTH, 0 == ATTACK, 3 == DEFENCE
	 */
	public int attackStyle() {
		return methodProvider.configs.get(43);
	}

	/**
	 * Method shall return the npc that is attacking our player
	 * 
	 * @return the npc that is attacking our player
	 */
	@SuppressWarnings("unchecked")
	public NPC getInteractingNPC() {
		return methodProvider.getNpcs().closest(npc -> npc.isInteracting(methodProvider.myPlayer()));
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
	public NPC getClosestFreeNPC(String name) {
		return methodProvider.getNpcs()
				.closest(npc -> !npc.isUnderAttack() && npc.getHealthPercent() > 0 && npc.getInteracting() == null
						&& Arrays.asList(name).contains(npc.getName()) && npc.exists()
						&& getAssignment().getActionArea().contains(npc) && npc.hasAction("Attack"));
	}

	/**
	 * @return boolean whether loot is available or not
	 */
	public boolean lootIsAvailable() {
		return getClosestLoot() != null;
	}

	/**
	 * 
	 * @return if player is attacking
	 */
	public boolean playerIsAttacking() {
		return methodProvider.combat.isFighting();
	}

	/**
	 * We need a method to check so that the npc that we are trying to interact
	 * with is not In combat with someone else.
	 * 
	 * @return whether the npc that we are trying to interact with is available
	 *         for combat or not
	 */
	public boolean interactingNpcIsAvailable() {
		target = (NPC) methodProvider.myPlayer().getInteracting();
		// return false if target does not exist.
		// If we are not interacting an npc, then we are not attacking an npc.
		if (target == null) {
			return false;
		}
		// return false if the target that we are trying to attack is
		// interacting with another player
		// We can not attack the npc if it is already in combat with someone
		// else
		else if (target.getInteracting() != methodProvider.myPlayer()) {
			return false;
		}
		return true;
	}

	/**
	 * @return if player is under attack
	 */
	public boolean underAttack() {
		return methodProvider.myPlayer().isUnderAttack();
	}

	/**
	 * 
	 * @return npc that is attacking our player
	 */
	private Entity getAttackingNPC() {
		return methodProvider.myPlayer().getInteracting();
	}

	/**
	 * Shall break combat sleep if player is not attacking npc is null npc hp is
	 * 0 have to eat
	 */
	public void combatSleep() {
		Timing.waitCondition(() -> (!playerIsAttacking()) || getInteractingNPC() == null
				|| getInteractingNPC().getHealthPercent() == 0 || shouldEat(), 150, 3000);
	}

	/**
	 * This method finds the closest NPC and then attacks it Script is put to
	 * sleep as long as player is still moving, still attacking or target has
	 * died
	 */
	public void attackNewTarget() {
		methodProvider.log("lets attack a new target");
		target = getClosestFreeNPC(getAssignment().getFightAssignment().getNpcName());
		if (target != null && target.interact("Attack")) {
			methodProvider.log("lets sleep");
			// combatSleep();
			// Sleep until player is interacting npc or npc is interacting
			// someone else than our player
			// perhaps change so target.isUnderAttack?
			Timing.waitCondition(() -> target.isUnderAttack(), 150, 5000);
		}
	}

	/**
	 * This method finds the current target that is attacking our NPC and then
	 * attacks it Script is put to sleep as long as player is still moving,
	 * still attacking or target has died
	 */
	public void attackExistingTarget() {
		methodProvider.log("lets attack the current target");
		target = getInteractingNPC();
		if (target != null && target.interact("Attack")) {
			methodProvider.log("lets sleep");
			// Sleep a second after click to not spam click
			Timing.sleep(1000);
			combatSleep();
		}
	}

	/**
	 * @return closest available loot that is within the fight area
	 */
	@SuppressWarnings("unchecked")
	private GroundItem getClosestLoot() {
		return methodProvider.groundItems
				.closest(i -> IaoxItem.getItemIDS(getAssignment().getFightAssignment().getLoot()).contains(i.getId())
						&& getAssignment().getActionArea().contains(i)
						&& i.getPosition().distance(methodProvider.myPlayer()) < 7);
	}

	/**
	 * Shall find and loot closest available loot
	 */
	public void loot(GroundItem item) {
		if (item != null && item.interact("Take")) {
			amountBeforeLoot = (int) methodProvider.getInventory().getAmount(item.getId());
			Timing.waitCondition(() -> methodProvider.inventory.getAmount(item.getId()) > amountBeforeLoot, 300, 4000);
			amountAfterLoot = (int) methodProvider.getInventory().getAmount(item.getId());

			// if successfull loot
			if (amountAfterLoot > amountBeforeLoot) {
				methodProvider.log("Successfull loot");

				// calculate the quantity
				lootAmount = amountAfterLoot - amountBeforeLoot;
				// get iaoxItem
				iaoxItem = IaoxItem.getByName(item.getName());
				// nullcheck if we found the item
				if (iaoxItem != null) {
					Simple.LOOT_HANDLER.addLoot(new Loot(iaoxItem, lootAmount));
				} else {
					methodProvider.log("could not find item: " + item.getName());
				}
			}
		}
	}

	/**
	 * Get food Check amount of food so we can create an conditionsleep relying
	 * on that When amount of food is below what it was before we tried to eat
	 * We shall break, since we are done eating
	 */
	public void eat() {
		if (!methodProvider.bank.isOpen()) {
			food = methodProvider.inventory.getItem(getAssignment().getFightAssignment().getFood().getID());
			if (food != null) {
				food.interact("Eat");
				Timing.waitCondition(() -> methodProvider.inventory.getAmount(food.getId()) < foodAmount, 600, 3000);
				Timing.sleep(3500);
			}
		} else {
			methodProvider.bank.close();
			Timing.waitCondition(() -> !methodProvider.bank.isOpen(), 300, 5000);
			eat();
		}
	}

	/**
	 * Shall loot if there if inventory is not full or already contains
	 * stackable item Shall eat if not enough inventory space
	 */
	public void handleLoot() {
		item = getClosestLoot();
		if (!methodProvider.inventory.isFull()
				|| (item.getDefinition().getNotedId() == -1 && methodProvider.inventory.contains(item.getId()))) {
			loot(item);
		} else {
			eat();
		}
	}

	/**
	 * @return a complete list of all item ids that are required
	 */
	private List<Integer> getRequiredItemIDs() {
		requiredItemIDs = new ArrayList<Integer>();
		getAssignment().getRequiredInventory().getRequiredItems().forEach(item -> {
			requiredItemIDs.add(item.getItemID());
		});
		getAssignment().getRequiredEquipment().getRequiredEquipment().forEach(item -> {
			if (item.getIaoxItem() != null) {
				requiredItemIDs.add(item.getItemID());
			}
		});
		return requiredItemIDs;
	}

	/**
	 * Shall return a list of items that shall be deposited from the players
	 * inventory
	 * 
	 * @return a complete list of items that inventory contains which is not
	 *         required
	 */
	public List<Item> getUnecessaryItems() {
		unecessaryItems = new ArrayList<Item>();
		// get all items from inventory
		inventoryItems = methodProvider.inventory.getItems();
		// check if inventory contains any items
		if (inventoryItems != null && inventoryItems.length > 0 && getRequiredItemIDs() != null
				&& !getRequiredItemIDs().isEmpty()) {
			// loop through each item and check if it is a required item
			for (Item item : inventoryItems) {
				// if it is not a required item ,it shall be added to the list
				// of unecessary items
				if (item != null && !getRequiredItemIDs().contains(item.getId())) {
					methodProvider.log("unecessary item" + item.getName());
					unecessaryItems.add(item);
				}
			}
		}
		return unecessaryItems;
	}

	/**
	 * Shall return if inventory contains items that are not necessary for the
	 * specific trip.
	 * 
	 * @return
	 */
	public boolean inventoryContainsUnecessaryItems() {
		unecessaryItems = (ArrayList<Item>) getUnecessaryItems();
		return unecessaryItems != null && !unecessaryItems.isEmpty();
	}

	/**
	 * Valid inventory means that the player has the exact amount of required
	 * items for the certain task.
	 * 
	 * @return
	 */
	public boolean hasValidInventory() {
		for (RequiredItem item : getAssignment().getRequiredInventory().getRequiredItems()) {
			if (!item.getException().getAsBoolean()
					&& methodProvider.inventory.getAmount(item.getItemID()) != item.getAmount()) {
				methodProvider.log("false" + item.getAmount());
				return false;
			}
		}
		return true;
	}

	/**
	 * @return a list of items that are required but that player does not have
	 *         in his inventory
	 */
	public List<RequiredEquipment> getNeededEquipmentItems() {
		neededEquipmentItems = new ArrayList<RequiredEquipment>();
		for (RequiredEquipment item : getAssignment().getRequiredEquipment().getRequiredEquipment()) {
			if (item.getIaoxItem() != null
					&& !methodProvider.equipment.isWearingItem(item.getSlot(), item.getItemName())) {
				neededEquipmentItems.add(item);
			}
		}
		return neededEquipmentItems;
	}

	/**
	 * @return a list of items that are required but that player does not have
	 *         in his inventory
	 */
	public List<RequiredItem> getNeededInventoryItems() {
		neededInventoryItems = new ArrayList<RequiredItem>();
		for (RequiredItem item : getAssignment().getRequiredInventory().getRequiredItems()) {
			if (methodProvider.inventory.getAmount(item.getItemID()) != item.getAmount()) {
				neededInventoryItems.add(item);
			}
		}
		return neededInventoryItems;
	}
	
	public int getHealth(){
		return methodProvider.getSkills().getDynamic(Skill.HITPOINTS);
	}


}
