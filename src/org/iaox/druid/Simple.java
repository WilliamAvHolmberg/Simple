package org.iaox.druid;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import org.iaox.druid.assignment.Assignment;
import org.iaox.druid.data.IaoxItem;
import org.iaox.druid.equipment.RequiredEquipment;
import org.iaox.druid.exchange.RSExchange;
import org.iaox.druid.intelligence.IaoxIntelligence;
import org.iaox.druid.inventory.RequiredItem;
import org.iaox.druid.loot.LootHandler;
import org.iaox.druid.node.Node;
import org.iaox.druid.node.agility.AgilityAction;
import org.iaox.druid.node.agility.AgilityBank;
import org.iaox.druid.node.agility.WalkToAgilityBank;
import org.iaox.druid.node.agility.WalkToAgilityCourse;
import org.iaox.druid.node.combat.ActionFight;
import org.iaox.druid.node.combat.FightBank;
import org.iaox.druid.node.combat.WalkToFight;
import org.iaox.druid.node.combat.WalkToFightBank;
import org.iaox.druid.node.woodcutting.ActionChop;
import org.iaox.druid.node.woodcutting.WCBank;
import org.iaox.druid.node.woodcutting.WalkToTree;
import org.iaox.druid.node.woodcutting.WalkToWCBank;
import org.iaox.druid.task.TaskHandler;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

@ScriptManifest(name = "BestFarmerEUWest", author = "Suxen", version = 1.0, info = "", logo = "")
public class Simple extends Script {

	public static Assignment CURRENT_ASSIGNMENT;
	public static List<Node> ALL_NODES;
	public static LootHandler LOOT_HANDLER;
	public static List<RequiredEquipment> EQUIP_LIST;
	public static List<RequiredItem> WITHDRAW_LIST;
	public static TaskHandler TASK_HANDLER;
	public static boolean TRAIN_DEFENCE = true;
	private IaoxIntelligence iaoxIntelligence;

	@Override
	public void onStart() throws InterruptedException {
		
		// initialize item prices
		log("Initializing item prices");
		final RSExchange rsExchange = new RSExchange();
		int price = 0;
		// Loop through every single item and set item price according to sell average
		// from the GE
		for (IaoxItem item : IaoxItem.values()) {
			//price = rsExchange.getExchangeItem(item.getName()).get().getSellAverage();
			//item.setItemPrice(price);
		}
		log("Finished with initialzing item prices");

		// initialize lootHandler
		LOOT_HANDLER = new LootHandler();
		// initialize equipment and withdraw list
		EQUIP_LIST = new ArrayList<RequiredEquipment>();
		WITHDRAW_LIST = new ArrayList<RequiredItem>();

		ALL_NODES = new ArrayList<Node>();
		
		//initialize combat nodes
		ALL_NODES.add(new ActionFight().init(this));
		ALL_NODES.add(new FightBank().init(this));
		ALL_NODES.add(new WalkToFightBank().init(this));
		ALL_NODES.add(new WalkToFight().init(this));
		
		//initialize wc nodes
		ALL_NODES.add(new ActionChop().init(this));
		ALL_NODES.add(new WalkToWCBank().init(this));
		ALL_NODES.add(new WalkToTree().init(this));
		ALL_NODES.add(new WCBank().init(this));
		
		//initialize agility nodes
		ALL_NODES.add(new AgilityBank().init(this));
		ALL_NODES.add(new WalkToAgilityCourse().init(this));
		ALL_NODES.add(new WalkToAgilityBank().init(this));
		ALL_NODES.add(new AgilityAction().init(this));



		// initialize task handler
		TASK_HANDLER = new TaskHandler(this);
		
		//initialize IaoxIntelligence
		iaoxIntelligence = new IaoxIntelligence(this);
		new Thread(iaoxIntelligence).start();

	}

	@Override
	public int onLoop() throws InterruptedException {
		if (!TASK_HANDLER.hasTask() || TASK_HANDLER.taskIsCompleted()) {
			log("lets generate a new task");
			TASK_HANDLER.setNewTask(iaoxIntelligence.generateNewTask());
		} else if(TASK_HANDLER.getNodes() != null && !TASK_HANDLER.getNodes().isEmpty()) {
			for (Node node : TASK_HANDLER.getNodes()) {
				if (node.active()) {
					node.run();
				}
			}
		}else{
			log("no nodes");
		}
		return 200;
	}
	
	public long getHealth(){
		return getSkills().getDynamic(Skill.HITPOINTS);
	}


	@Override
	public void onPaint(Graphics2D g) {

		// Print information about current task
		if (TASK_HANDLER.getCurrentTask() != null) {
			g.drawString("Current Task: " + TASK_HANDLER.getCurrentTask(), 50, 25);
			g.drawString("RequiredInv:" + TASK_HANDLER.getCurrentTask().getAssignment().getRequiredInventory(), 50, 125);
			g.drawString("RequiredEquipment:" + TASK_HANDLER.getCurrentTask().getAssignment().getRequiredEquipment(), 50, 150);

		}
		// Print information about experience
		g.drawString("XP Gained: " + experienceTracker.getGainedXP(TASK_HANDLER.getCurrentTask().getSkill()), 50, 50);
		g.drawString("XP Per Hour: " + experienceTracker.getGainedXPPerHour(TASK_HANDLER.getCurrentTask().getSkill()), 50, 75);

		// Print information about loot
		g.drawString("Total loot: " + LOOT_HANDLER.getValueOfLoot(), 50, 100);

	}

}
