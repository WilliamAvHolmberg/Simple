package org.iaox.druid.node.fishing;


import java.util.Arrays;

import org.iaox.druid.Simple;
import org.iaox.druid.Timing;
import org.iaox.druid.assignment.AssignmentType;
import org.iaox.druid.data.Areas;
import org.iaox.druid.node.Node;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.Skill;

public class ActionFish extends Node{

	@Override
	public boolean active() {
		return skillingProvider.shouldPeformSkillingAction() && skillingProvider.inSkillingArea() ;
	}

	@Override
	public void run() {
		//TODO - add pick up nest
		if(!methodProvider.getSettings().isRunning() && methodProvider.getSettings().getRunEnergy() > 5) {
			methodProvider.getSettings().setRunning(true);
			Timing.waitCondition(() -> methodProvider.getSettings().isRunning(), 150, 3000);
		}else if(fishingProvider.lastInteractedObjectStillExists() && (fishingProvider.playerIsFishing() || methodProvider.myPlayer().isMoving() )){
			methodProvider.log("We are already woodcutting");
			fishingProvider.miningSleep();
		}else {
			//find new tree and chop it
			methodProvider.log("lets fish");
			fishingProvider.startFishing();
		}
	}
	
	@Override
	public AssignmentType getAssignmentType() {
		return AssignmentType.FISHING;
	}

}
