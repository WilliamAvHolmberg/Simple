package org.iaox.druid.node.combat;

import java.util.Arrays;
import java.util.List;

import org.iaox.druid.data.Areas;
import org.iaox.druid.node.Node;
import org.iaox.druid.node.assignment.AssignmentType;
import org.iaox.druid.travel.TravelException;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.event.WebWalkEvent;
import org.osbot.rs07.event.webwalk.PathPreferenceProfile;

public class WalkToFight extends Node {

	private List<TravelException> travelExceptions;
	private boolean exception;
	private WebWalkEvent webEvent;
	private PathPreferenceProfile ppp;

	@Override
	public boolean active() {
		return combatProvider.shouldFight() && !combatProvider.inFightArea();
	}

	@Override
	public void run() {
		// initialize a boolean called exception
		// boolean will set to true if any TravelException is found
		// if boolean is false after checking every travelException that exist
		// webwalking shall be used
		exception = false;
		//check if there is any travelExceptions for the current assignment
				travelExceptions = combatProvider.getAssignment().getTravelExceptionsToFight();
		// first of all, check if player should eat.
		if (combatProvider.shouldEat()) {
			combatProvider.eat();
		} else if (travelExceptions != null) {
			methodProvider.log("exception");
			// loop through every single travelException that exist for the
			// current assignment
			for (TravelException travelException : travelExceptions) {
				// if the area of the travelexception contains player - precede
				// if the area does not contain our player, go to next
				// travelexception
				if (travelException.getArea().contains(methodProvider.myPlayer())) {
					switch (travelException.getType()) {
					case PATH:
						methodProvider.getWalking().walkPath(travelException.getPath());
						exception = true;
						break;
					case TELEPORT:
						// check if inventory contains the required teleport
						// shall use the teleport if inventory contains
						// shall set exception to false if inventory does not
						// contain teleport
						// in order to use webwalk instead
						Item teleport = methodProvider.inventory.getItem(travelException.getTeleportName());
						if (teleport != null) {
							teleport.interact("Break");
							exception = true;
						} else {
							exception = false;
						}
						break;
					case WEBWALK:
						methodProvider.walking.webWalk(travelException.getWebWalkArea());
						break;
					default:
						break;

					}
				}
			}
			// Use webwalk if either no exception were found or no exception
			// were current
			if (!exception) {
				methodProvider.walking.webWalk(combatProvider.getAssignment().getNpcArea());

			}
		}

	}

	@Override
	public AssignmentType getAssignmentType() {
		return AssignmentType.COMBAT;
	}

}
