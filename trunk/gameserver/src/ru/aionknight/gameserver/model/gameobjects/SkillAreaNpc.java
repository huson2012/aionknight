package ru.aionknight.gameserver.model.gameobjects;


import ru.aionknight.gameserver.ai.npcai.SkillAreaNpcAi;
import ru.aionknight.gameserver.controllers.NpcWithCreatorController;
import ru.aionknight.gameserver.model.templates.VisibleObjectTemplate;
import ru.aionknight.gameserver.model.templates.spawn.SpawnTemplate;

/**
 * @author ViAl
 *
 */
public class SkillAreaNpc extends NpcWithCreator
{
	/**
	 * @param objId
	 * @param controller
	 * @param spawnTemplate
	 * @param objectTemplate
	 */
	public SkillAreaNpc(int objId, NpcWithCreatorController controller, SpawnTemplate spawnTemplate,
		VisibleObjectTemplate objectTemplate)
	{
		super(objId, controller, spawnTemplate, objectTemplate);
		// TODO Auto-generated constructor stub
	}
	
	public NpcWithCreatorController getController()
	{
		return (NpcWithCreatorController)super.getController();
	}
	public SkillAreaNpc getOwner()
	{
		return (SkillAreaNpc)this;
	}
	@Override
	public void initializeAi()
	{
		this.ai = new SkillAreaNpcAi();
		ai.setOwner(this);
	}
	
	/**
	 * @return NpcObjectType.SKILLAREANPC
	 */
	@Override
	public NpcObjectType getNpcObjectType()
	{
		return NpcObjectType.SKILLAREANPC;
	}
}
