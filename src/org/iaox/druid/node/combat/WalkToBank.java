package org.iaox.druid.node.combat;

import org.iaox.druid.Timing;
import org.iaox.druid.data.Areas;
import org.iaox.druid.node.Node;
import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.event.WebWalkEvent;
import org.osbot.rs07.event.webwalk.PathPreferenceProfile;

public class WalkToBank extends Node{

	@Override
	public boolean active() {
		return !combatProvider.shouldFight() && !combatProvider.inBankArea();
	}

	@Override
	public void run() {
		if(Areas.TAVERLEY_DRUIDS.contains(methodProvider.myPlayer()) && methodProvider.inventory.contains("Falador teleport")){
			methodProvider.log("lets teleport to falador");
			Item teleport = methodProvider.inventory.getItem("Falador teleport");
			teleport.interact("Break");
			Timing.waitCondition(() -> !Areas.TAVERLEY_DRUIDS.contains(methodProvider.myPlayer()), 600,2000);
		}else{
		methodProvider.log("lets walk to bank");
		WebWalkEvent webEvent = new WebWalkEvent(Banks.FALADOR_WEST);
		webEvent.useSimplePath();
		PathPreferenceProfile ppp = new PathPreferenceProfile();
		ppp.setAllowTeleports(true);
		webEvent.setPathPreferenceProfile(ppp);
		methodProvider.execute(webEvent);
		}
	}

}
