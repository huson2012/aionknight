/**
 * This file is part of the requirements for the Illusion Gate Skill.
 * Code References from ATracer's Trap.java of Aion-Knight
 */
package gameserver.model.gameobjects;


import gameserver.controllers.GroupGateController;
import gameserver.controllers.NpcController;
import gameserver.model.templates.VisibleObjectTemplate;
import gameserver.model.templates.spawn.SpawnTemplate;

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
