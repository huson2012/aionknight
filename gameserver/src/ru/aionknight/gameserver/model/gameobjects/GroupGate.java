/*
 * This file is part of the requirements for the Illusion Gate Skill.
 * Code References from ATracer's Trap.java of Aion-Unique
 */
package ru.aionknight.gameserver.model.gameobjects;


import ru.aionknight.gameserver.controllers.GroupGateController;
import ru.aionknight.gameserver.controllers.NpcController;
import ru.aionknight.gameserver.model.templates.VisibleObjectTemplate;
import ru.aionknight.gameserver.model.templates.spawn.SpawnTemplate;

/**
 * @author LokiReborn
 *
 */
public class GroupGate extends NpcWithCreator
{
	/**
	 * 
	 * @param objId
	 * @param controller
	 * @param spawnTemplate
	 * @param objectTemplate
	 */
	public GroupGate(int objId, NpcController controller, SpawnTemplate spawnTemplate, VisibleObjectTemplate objectTemplate)
	{
		super(objId, controller, spawnTemplate, objectTemplate);
	}

	@Override
	public GroupGateController getController()
	{
		return (GroupGateController) super.getController();
	}
	@Override
	public byte getLevel()
	{
		return (1);
	}
	
	/**
	 * @return NpcObjectType.GROUPGATE
	 */
	@Override
	public NpcObjectType getNpcObjectType()
	{
		return NpcObjectType.GROUPGATE;
	}
}
