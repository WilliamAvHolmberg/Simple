package org.iaox.druid.node;
import org.iaox.druid.assignment.AssignmentType;
import org.iaox.druid.node.provider.CombatProvider;
import org.iaox.druid.node.provider.WoodcuttingProvider;
import org.osbot.rs07.script.MethodProvider;

public abstract class Node {
	
	public MethodProvider methodProvider;
	public CombatProvider combatProvider;
	public WoodcuttingProvider woodcuttingProvider;
	
	public Node init(MethodProvider methodProvider){
		this.methodProvider = methodProvider;
		this.combatProvider = new CombatProvider(methodProvider);
		this.woodcuttingProvider = new WoodcuttingProvider(methodProvider);
		return this;
	}
	
	public abstract boolean active();
	public abstract void run();
	public abstract AssignmentType getAssignmentType();

}
