package org.iaox.druid.node.agility;

import java.util.Arrays;
import java.util.List;

import org.iaox.druid.Simple;
import org.iaox.druid.Timing;
import org.iaox.druid.assignment.AssignmentType;
import org.iaox.druid.assignment.agility.AgilityObstacle;
import org.iaox.druid.data.Areas;
import org.iaox.druid.node.Node;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.Skill;

public class ActionAgility extends Node {

	private boolean climbed;
	private List<AgilityObstacle> obstacles;
	private AgilityObstacle nextObstacle;
	private int currentInt;

	@Override
	public boolean active() {
		return skillingProvider.shouldPeformSkillingAction() && skillingProvider.inAgilityArea();
	}

	@Override
	public void run() {
		/**
		 * Loop through each obstacle
		 * If player is in area for the specific obstacle
		 * Climb obstacle
		 */
		climbed = false;
		obstacles = skillingProvider.getAgilityAssignment().getObstacles();
		for (AgilityObstacle obstacle : obstacles) {
			if (skillingProvider.playerInArea(obstacle.getArea())) {
				nextObstacle = getNextObstacle(obstacles, obstacle);
				agilityProvider.climbObs(obstacle, nextObstacle);
				climbed = true;
			}
		}
		
		//TODO - returns a false position when for instance, cross a tightrope
		//has to fix this in the future
		//
		if(!climbed && methodProvider.myPosition().getZ() == 0){
			methodProvider.walking.webWalk(skillingProvider.getAgilityAssignment().getObstacles().get(0).getArea());
		}
	}
	
	public AgilityObstacle getNextObstacle(List<AgilityObstacle> obstacles, AgilityObstacle currentObstacle){
		currentInt = obstacles.indexOf(currentObstacle);
		methodProvider.log("current int: " + currentInt + "---Size: " +obstacles.size());
		if(obstacles.size() == currentInt + 1){
			return obstacles.get(0);
		}else{
			nextObstacle = obstacles.get(obstacles.indexOf(currentObstacle) + 1);
			if(nextObstacle != null){
				return nextObstacle;
			}
		}
		return obstacles.get(0);
	}

	@Override
	public AssignmentType getAssignmentType() {
		return AssignmentType.AGILITY;
	}

}
