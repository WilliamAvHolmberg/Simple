package org.iaox.druid.node.combat;


import java.util.Arrays;

import org.iaox.druid.Simple;
import org.iaox.druid.Timing;
import org.iaox.druid.data.Areas;
import org.iaox.druid.node.Node;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.Skill;

public class ActionFight extends Node{

	@Override
	public boolean active() {
		return combatProvider.shouldFight() && combatProvider.inFightArea() ;
	}

	@Override
	public void run() {
		if(combatProvider.shouldEat()){
			combatProvider.eat();
		}else if(!combatProvider.rightStyle()){
			combatProvider.changeStyle();
		}else if(!methodProvider.getSettings().isRunning() && methodProvider.getSettings().getRunEnergy() > 5) {
			methodProvider.getSettings().setRunning(true);
			Timing.waitCondition(() -> methodProvider.getSettings().isRunning(), 150, 3000);
		}else if(combatProvider.playerIsAttacking() && combatProvider.interactingNpcIsAvailable()){
			//check for new druid
			methodProvider.log("We are already attacking");
			combatProvider.combatSleep();
		}else if(combatProvider.underAttack() || combatProvider.getInteractingNPC() != null && !combatProvider.playerIsAttacking() && combatProvider.getInteractingNPC().hasAction("Attack")){
			//attack entity that is attacking us
			methodProvider.log("We are under attack and not fighting back");
			combatProvider.attackExistingTarget();
		}else if(combatProvider.lootIsAvailable()){
			methodProvider.log("Loot is available");
			combatProvider.handleLoot();
		}else {
			//find new target and attack
			combatProvider.attackNewTarget();
		}
	}

}
