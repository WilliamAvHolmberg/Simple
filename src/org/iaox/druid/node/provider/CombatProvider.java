package org.iaox.druid.node.provider;

import java.util.Arrays;

import org.iaox.druid.Simple;
import org.iaox.druid.Timing;
import org.iaox.druid.data.Areas;
import org.iaox.druid.data.IaoxItem;
import org.iaox.druid.data.Loot;
import org.iaox.druid.node.assignment.CombatAssignment;
import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;

public class CombatProvider {

	public MethodProvider methodProvider;
	private Skill skill = Skill.STRENGTH;
	private GroundItem item;
	private Item food;
	private int foodAmount;
	public CombatProvider(MethodProvider methodProvider) {
		this.methodProvider = methodProvider;
	}

	/**
	 * @return the current combat assignment that is initialized onStart
	 */
	public CombatAssignment getAssignment(){
		return Simple.CURRENT_ASSIGNMENT;
	}
	/**
	 * @return if player is in the fight area
	 */
	public boolean inFightArea() {
		return getAssignment().getNpcArea().contains(methodProvider.myPlayer());
	}

	/**
	 * @return if player is in the bank
	 */
	public boolean inBankArea() {
		return getAssignment().getBankArea().contains(methodProvider.myPlayer());
	}
	
	/**
	 * @return if player has to eat
	 */
	public boolean shouldEat() {
		return methodProvider.myPlayer().getHealthPercent() < 40;
	}
	
	public boolean inventoryContainFood(){
		return methodProvider.inventory.contains(getAssignment().getFood().getID());
	}

	/**
	 * Shall return true if player is in bank and does not have food
	 * Shall return true if player is in fight area and does not have food and has to eat
	 * Shall return false if player is in fight area and does not have but does not have to eat either
	 * Shall return false if player has food
	 * @return if player need to withdraw food
	 */
	public boolean needFood() {
		if(inFightArea() && !inventoryContainFood() && !shouldEat()){
			return true;
		}
		return !inventoryContainFood();
	}
	
	/**
	 * Shall return true if inventory is full and does not contain food
	 * @return
	 */
	public boolean needDepositItems() {
		return methodProvider.inventory.isFull() && !inventoryContainFood();
	}

	/**
	 * Player should fight if 
	 * does not need food
	 * is in player fight area 
	 * does not need to deposit items
	 * @return if player is ready to fight
	 */
	public boolean shouldFight() {
		return !needFood() && !needDepositItems() && getAssignment().getRequiredInventory().hasValidInventory();
	}


	/**
	 * @return if current attack style is equal to the preferred attack style
	 */
	public boolean rightStyle() {
		if (skill.equals(Skill.STRENGTH) && (attackStyle() != 1)) {
			return false;
		} else if (skill.equals(Skill.ATTACK) && (attackStyle() != 0)) {
			return false;
		} else if (skill.equals(Skill.DEFENCE) && (attackStyle() != 3)) {
			return false;
		} else if (skill.equals(Skill.RANGED) && (attackStyle() != 1)) {
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
			if (skill.equals(Skill.STRENGTH) && (attackStyle() != 1)) {
				methodProvider.mouse.click(689, 270, false);// click "train
															// strength"
			} else if (skill.equals(Skill.ATTACK) && (attackStyle() != 0)) {
				methodProvider.mouse.click(601, 269, false); // click "train
																// attack"
			} else if (skill.equals(Skill.DEFENCE) && (attackStyle() != 3)) {
				methodProvider.mouse.click(701, 335, false); // click "train
																// def"
			} else if (skill.equals(Skill.RANGED) && (attackStyle() != 1)) {
				methodProvider.mouse.click(689, 270, false);// click "train
															// range"
			}

		} else {
			methodProvider.mouse.click(545, 190, false);
		}

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
		return methodProvider.getNpcs()
				.closest(npc -> npc.isInteracting(methodProvider.myPlayer()));
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
						&& getAssignment().getNpcArea().contains(npc) && npc.hasAction("Attack"));
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
	 * Shall conditional sleep if player is
	 * Moving
	 * Attacking
	 * NPC is not null
	 * Does not have to eat
	 */
	public void combatSleep(){
		Timing.waitCondition(
				() -> (!methodProvider.myPlayer().isMoving() && !playerIsAttacking()) && getInteractingNPC() != null  || shouldEat(), 1000,
				3000);
	}
	
	/**
	 * This method finds the closest NPC and then attacks it Script is put to
	 * sleep as long as player is still moving, still attacking or target has
	 * died
	 */
	public void attackNewTarget() {
		methodProvider.log("lets attack a new target");
		Simple.ENTITY = getClosestFreeNPC(getAssignment().getNpcName());
		if (Simple.ENTITY != null && Simple.ENTITY.interact("Attack")) {
			methodProvider.log("lets sleep");
			//Sleep a second after click to not spam click
			Timing.sleep(1000);
			combatSleep();
		}
	}

	/**
	 * This method finds the current target that is attacking our NPC and then
	 * attacks it Script is put to sleep as long as player is still moving,
	 * still attacking or target has died
	 */
	public void attackExistingTarget() {
		methodProvider.log("lets attack the current target");
		Simple.ENTITY = getInteractingNPC();
		if (Simple.ENTITY != null && Simple.ENTITY.interact("Attack")) {
			methodProvider.log("lets sleep");
			//Sleep a second after click to not spam click
			Timing.sleep(1000);
			combatSleep();
		}
	}
	
	/**
	 * @return closest available loot that is within the fight area
	 */
	@SuppressWarnings("unchecked")
	private GroundItem getClosestLoot() {
		return methodProvider.groundItems.closest(i -> IaoxItem.getItemIDS(getAssignment().getLoot()).contains(i.getId())
				&& getAssignment().getNpcArea().contains(i) && i.getPosition().distance(methodProvider.myPlayer()) < 7);
	}

	/**
	 * Shall find and loot closest available loot
	 */
	public void loot(GroundItem item) {
		if (item != null && item.interact("Take")) {
			int amountBeforeLoot = (int) methodProvider.getInventory().getAmount(item.getId());
			Timing.waitCondition(() -> methodProvider.inventory.getAmount(item.getId()) > amountBeforeLoot, 300, 4000);
		}
	}

	/**
	 * Shall loot if there if inventory is
	 * not full
	 * or already contains stackable item
	 * Shall eat if not enough inventory space
	 */
	public void handleLoot() {
		item = getClosestLoot();
		if(!methodProvider.inventory.isFull() || (item.getDefinition().getNotedId() == -1 && methodProvider.inventory.contains(item.getId()))) {
			loot(item);	
		}else{
			food = methodProvider.inventory.getItem(getAssignment().getFood().getID());
			foodAmount = (int) methodProvider.inventory.getAmount(food.getId());
			food.interact("Eat");
			Timing.waitCondition(() -> methodProvider.inventory.getAmount(food.getId()) < foodAmount, 600, 3000);	
		}
		
	}

}
