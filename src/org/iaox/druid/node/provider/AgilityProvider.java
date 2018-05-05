package org.iaox.druid.node.provider;

import org.iaox.druid.Simple;
import org.iaox.druid.Timing;
import org.iaox.druid.assignment.Assignment;
import org.iaox.druid.assignment.agility.AgilityAssignment;
import org.iaox.druid.assignment.agility.AgilityObstacle;
import org.iaox.druid.data.Areas;
import org.iaox.druid.data.WebBank;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.script.Script;

public class AgilityProvider {

	private MethodProvider methodProvider;
	private int lastID;
	private int failCheck;
	private SkillingProvider skillingProvider;
	private RS2Object obs;

	public AgilityProvider(MethodProvider methodProvider, SkillingProvider skillingProvider) {
		this.methodProvider = methodProvider;
		this.skillingProvider = skillingProvider;
	}

	public AgilityAssignment getAssignment() {
		return Simple.TASK_HANDLER.getCurrentTask().getAssignment().getAgilityAssignment();
	}
	
	/**
	 * @return if player is in the bank
	 */
	public boolean inBankArea() {
		return getBankArea().contains(methodProvider.myPlayer());
	}
	
	/**
	 * If player is on left side of white mountain, we have to find closest bank
	 * @return
	 */
	public Area getBankArea(){
		if(getAssignment().getBankArea() != null){
			return getAssignment().getBankArea();
		}
		return WebBank.getNearest(methodProvider).getArea();	
	}

	public boolean playerInAgilityArea() {
		if (getAssignment() != null) {
			for (AgilityObstacle obstacle : getAssignment().getObstacles()) {
				if (skillingProvider.playerInArea(obstacle.getArea())) {
					return true;
				}
			}
		}
		return false;
	}

	public void climbObs(AgilityObstacle currentObstacle, AgilityObstacle nextObstacle) {
		//Sleep a tick before doing action
		//this is to prevent clicking action before last action is finished.
		Timing.sleep(1200);
		obs = methodProvider.objects.closest(currentObstacle.getID());
		if (obs != null) {
			obs.interact(currentObstacle.getAction());
			Timing.waitCondition(() -> nextObstacle.getArea().contains(methodProvider.myPlayer()) , 600,6000);
		}

	}

}