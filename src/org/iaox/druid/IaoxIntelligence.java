package org.iaox.druid;

import org.iaox.druid.data.IaoxItem;
import org.iaox.druid.equipment.IaoxEquipment;
import org.iaox.druid.equipment.RequiredEquipment;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.ui.EquipmentSlot;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;

public class IaoxIntelligence implements Runnable {

	private MethodProvider methodProvider;
	private int level;
	private IaoxEquipment equipment;
	private RequiredEquipment bestEquipment;

	/**
	 * Supposed to work as the "brain" of this script Make sure that the right
	 * task is done Make sure that the rigth gear is choosen
	 */

	/**
	 * Main Thread - shall be ran every 5 sec check best equipment TODO - Check
	 * how many players in area TODO - Check if player should deposit gold to
	 * mule
	 */

	public IaoxIntelligence(MethodProvider methodProvider) {
		this.methodProvider = methodProvider;
	}

	public void run() {

		while (methodProvider.getClient().isLoggedIn()) {
			methodProvider.log("running: thread");
			if (Simple.TASK_HANDLER.getCurrentTask() != null && !bestEquipmentIsSelected()) {
				setNewEquipment();
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// sleep 5000ms
	}

	/**
	 * Shall return the most appropriate equipment Depending on: Level Current
	 * Task
	 * 
	 * @return
	 */
	public RequiredEquipment getBestEquipment(EquipmentSlot slot) {
		switch (slot) {
		case AMULET:
			return new RequiredEquipment(slot, IaoxItem.AMULET_OF_STRENGTH);
		case ARROWS:
			break;
		case CAPE:
			level = getLevel(Skill.DEFENCE);
			if (level < 20) {
				return new RequiredEquipment(slot, IaoxItem.BLACK_CAPE);
			}
			break;
		case CHEST:
			// case (chest
			// depends on defence level
			// if < 40
			// return monks robe
			// if < 70 && range >= 50
			// return blue d hide body
			break;
		case FEET:
			break;
		case HANDS:
			break;
		case HAT:
			break;
		case LEGS:
			break;
		case RING:
			break;
		case SHIELD:
			break;
		case WEAPON:
			level = getLevel(Skill.ATTACK);
			if (level < 20) {
				return new RequiredEquipment(slot, IaoxItem.IRON_SCIMITAR);
			}
			if (level < 30) {
				return new RequiredEquipment(slot, IaoxItem.MITHRIL_SCIMITAR);
			}
			if (level < 40) {
				return new RequiredEquipment(slot, IaoxItem.ADAMANT_SCIMITAR);
			}
			if (level < 70) {
				return new RequiredEquipment(slot, IaoxItem.RUNE_SCIMITAR);
			}
			break;
		}
		return null;
	}

	/**
	 * Shall check if task requires gear Shall loop through the Required
	 * Equipment and check if each item is the most appropriate
	 * 
	 * @return
	 */
	public boolean bestEquipmentIsSelected() {
		equipment = Simple.TASK_HANDLER.getCurrentTask().getCombatAssignment().getRequiredEquipment();
		for (RequiredEquipment currentEquipment : equipment.getRequiredEquipment()) {
			bestEquipment = getBestEquipment(currentEquipment.getSlot());
			if ((currentEquipment.getIaoxItem() != null && currentEquipment.getItemID() == bestEquipment.getItemID()) || (currentEquipment.getIaoxItem() == null && bestEquipment != null)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Shall set equipment to new best equipment
	 */
	public void setNewEquipment() {
		equipment = Simple.TASK_HANDLER.getCurrentTask().getCombatAssignment().getRequiredEquipment();
		for (RequiredEquipment currentEquipment : equipment.getRequiredEquipment()) {
			bestEquipment = getBestEquipment(currentEquipment.getSlot());
			if (bestEquipment != null && (currentEquipment.getIaoxItem() == null || currentEquipment.getItemID() != bestEquipment.getItemID())) {
				currentEquipment.replaceItem(bestEquipment.getIaoxItem());
			}
		}
	}

	public int getLevel(Skill skill) {
		return methodProvider.skills.getStatic(skill);
	}

	public int getExperience(Skill skill) {
		return methodProvider.skills.getExperience(skill);
	}

}
