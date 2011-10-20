package ru.aionknight.gameserver.model.gameobjects;


import ru.aionknight.gameserver.ai.npcai.TotemAi;
import ru.aionknight.gameserver.controllers.NpcController;
import ru.aionknight.gameserver.controllers.NpcWithCreatorController;
import ru.aionknight.gameserver.model.templates.VisibleObjectTemplate;
import ru.aionknight.gameserver.model.templates.spawn.SpawnTemplate;

/**
 * @author kecimis
 *
 */
public class Totem extends NpcWithCreator
{
	/**
	 * 
	 * @param objId
	 * @param controller
	 * @param spawnTemplate
	 * @param objectTemplate
	 */
	public Totem(int objId, NpcController controller, SpawnTemplate spawnTemplate, VisibleObjectTemplate objectTemplate)
	{
		super(objId, controller, spawnTemplate, objectTemplate);
	}
	
	@Override
	public NpcWithCreatorController getController()
	{
		return (NpcWithCreatorController) super.getController();
	}
	public Totem getOwner()
	{
		return (Totem)this;
	}
	@Override
	public void initializeAi()
	{
		this.ai = new TotemAi();
		ai.setOwner(this);
	}
	
	/**
	 * @return NpcObjectType.TOTEM
	 */
	@Override
	public NpcObjectType getNpcObjectType()
	{
		return NpcObjectType.TOTEM;
	}
}