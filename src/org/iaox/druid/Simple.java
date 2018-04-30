package org.iaox.druid;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.iaox.druid.data.Areas;
import org.iaox.druid.data.IaoxItem;
import org.iaox.druid.data.LootItems;
import org.iaox.druid.data.Paths;
import org.iaox.druid.exchange.RSExchange;
import org.iaox.druid.gear.IaoxEquipment;
import org.iaox.druid.gear.RequiredEquipment;
import org.iaox.druid.inventory.IaoxInventory;
import org.iaox.druid.inventory.RequiredItem;
import org.iaox.druid.loot.LootHandler;
import org.iaox.druid.node.Node;
import org.iaox.druid.node.assignment.CombatAssignment;
import org.iaox.druid.node.assignment.FightAssignment;
import org.iaox.druid.node.combat.ActionBank;
import org.iaox.druid.node.combat.ActionFight;
import org.iaox.druid.node.combat.WalkToBank;
import org.iaox.druid.node.combat.WalkToFight;
import org.iaox.druid.travel.TravelException;
import org.iaox.druid.travel.TravelType;
import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.ui.EquipmentSlot;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.api.util.GraphicUtilities;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import com.thoughtworks.xstream.io.path.Path;

@ScriptManifest(name = "Druid", author = "Suxen", version = 1.0, info = "", logo = "")
public class Simple extends Script {

	public static CombatAssignment CURRENT_ASSIGNMENT;
	public List<Node> nodeHandler;
	public static LootHandler LOOT_HANDLER;
	public static List<RequiredEquipment> EQUIP_LIST;
	public static List<RequiredItem> WITHDRAW_LIST;

	@Override
	public void onStart() throws InterruptedException {
		// initialize item prices
		log("Initializing item prices");
		final RSExchange rsExchange = new RSExchange();
		int price = 0;
		// Loop through every single item and set item price according to sell average
		// from the GE
		for (IaoxItem item : IaoxItem.values()) {
			price = rsExchange.getExchangeItem(item.getName()).get().getSellAverage();
			item.setItemPrice(price);
		}
		log("Finished with initialzing item prices");

		// initialize lootHandler
		LOOT_HANDLER = new LootHandler();
		// initialize equipment and withdraw list
		EQUIP_LIST = new ArrayList<RequiredEquipment>();
		WITHDRAW_LIST = new ArrayList<RequiredItem>();

		// exception: if player is in fight place and does not have to eat, then food is
		// not required
		RequiredItem food = new RequiredItem(5, IaoxItem.TROUT, true,
				() -> Areas.TAVERLEY_DRUIDS.contains(myPlayer()) && myPlayer().getHealthPercent() > 40);
		// initialize the required inventory
		IaoxInventory druidInventory = new IaoxInventory(new RequiredItem[] {food});
		// intialize all required equipments
		RequiredEquipment runeScimitar = new RequiredEquipment(EquipmentSlot.WEAPON, IaoxItem.RUNE_SCIMITAR);
		// initialize the required equipment
		IaoxEquipment druidEquipment = new IaoxEquipment(new RequiredEquipment[] { runeScimitar });

		
		CURRENT_ASSIGNMENT = new CombatAssignment(FightAssignment.CHAOS_DRUIDS_TAVERLEY, druidInventory, druidEquipment, IaoxItem.TROUT);

		nodeHandler = new ArrayList<Node>();
		nodeHandler.add(new ActionFight().init(this));
		nodeHandler.add(new ActionBank().init(this));
		nodeHandler.add(new WalkToBank().init(this));
		nodeHandler.add(new WalkToFight().init(this));

		// initialize experience tracker for strength

		experienceTracker.getExperienceTracker().start(Skill.STRENGTH);

	}

	@Override
	public int onLoop() throws InterruptedException {
			log("node handler");
			for (Node node : nodeHandler) {
				if (node.active()) {
					node.run();
				}
			}
		return 200;
	}


	@Override
	public void onPaint(Graphics2D g) {
		// Print information about experience
		g.drawString("XP Gained: " + experienceTracker.getGainedXP(Skill.STRENGTH), 50, 50);
		g.drawString("XP Per Hour: " + experienceTracker.getGainedXPPerHour(Skill.STRENGTH), 50, 75);

		// Print information about loot
		g.drawString("Total loot: " + LOOT_HANDLER.getValueOfLoot(), 50, 100);

	}

}
