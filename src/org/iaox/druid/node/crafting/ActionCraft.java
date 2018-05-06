package org.iaox.druid.node.crafting;


import java.util.Arrays;

import org.iaox.druid.Simple;
import org.iaox.druid.Timing;
import org.iaox.druid.assignment.AssignmentType;
import org.iaox.druid.data.Areas;
import org.iaox.druid.node.Node;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.Skill;

public class ActionCraft extends Node{

	@Override
	public boolean active() {
		return craftingProvider.shouldPeformSkillingAction() && skillingProvider.inSkillingArea() ;
	}

	@Override
	public void run() {
		switch (skillingProvider.getAssignment().getCraftingAssignment()) {
		case MOLTEN_GLASS:
			craftingProvider.craftMoltenGlass();
			break;
		default:
			break;

		}
	}
	
	@Override
	public AssignmentType getAssignmentType() {
		return AssignmentType.CRAFTING;
	}

}
