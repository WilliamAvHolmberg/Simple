package org.iaox.druid.node.woodcutting;


import java.util.Arrays;

import org.iaox.druid.Simple;
import org.iaox.druid.Timing;
import org.iaox.druid.assignment.AssignmentType;
import org.iaox.druid.data.Areas;
import org.iaox.druid.node.Node;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.Skill;

public class ActionChop extends Node{

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
		}else if(skillingProvider.lastInteractedObjectStillExists() && (woodcuttingProvider.playerIsCuttingTree() || methodProvider.myPlayer().isMoving() )){
			methodProvider.log("We are already woodcutting");
			woodcuttingProvider.woodcuttingSleep();
		}else {
			//find new tree and chop it
			methodProvider.log("lets chop");
			woodcuttingProvider.chopNewTree();
		}
	}
	
	@Override
	public AssignmentType getAssignmentType() {
		return AssignmentType.WOODCUTTING;
	}

}
