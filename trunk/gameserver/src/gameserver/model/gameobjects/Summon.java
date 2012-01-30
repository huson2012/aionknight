/*
 * Emulator game server Aion 2.7 from the command of developers 'Aion-Knight Dev. Team' is
 * free software; you can redistribute it and/or modify it under the terms of
 * GNU affero general Public License (GNU GPL)as published by the free software
 * security (FSF), or to License version 3 or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranties related to
 * CONSUMER PROPERTIES and SUITABILITY FOR CERTAIN PURPOSES. For details, see
 * General Public License is the GNU.
 *
 * You should have received a copy of the GNU affero general Public License along with this program.
 * If it is not, write to the Free Software Foundation, Inc., 675 Mass Ave,
 * Cambridge, MA 02139, USA
 *
 * Web developers : http://aion-knight.ru
 * Support of the game client : Aion 2.7- 'Arena of Death' (Innova)
 * The version of the server : Aion-Knight 2.7 (Beta version)
 */

package gameserver.model.gameobjects;

import gameserver.controllers.CreatureController;
import gameserver.controllers.SummonController;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.SummonGameStats;
import gameserver.model.gameobjects.stats.SummonLifeStats;
import gameserver.model.templates.NpcTemplate;
import gameserver.model.templates.VisibleObjectTemplate;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.model.templates.stats.SummonStatsTemplate;
import gameserver.world.WorldPosition;

public class Summon extends Creature
{

	private Player	master;
	private SummonMode mode;
	private byte level;

	public static enum SummonMode
	{
		ATTACK(0),
		GUARD(1),
		REST(2),
		RELEASE(3);
		
		private int id;
		
		private SummonMode(int id)
		{
			this.id = id;
		}

		/**
		 * @return the id
		 */
		public int getId()
		{
			return id;
		}	
	}
	/**
	 * 
	 * @param objId
	 * @param controller
	 * @param spawnTemplate
	 * @param objectTemplate
	 * @param statsTemplate 
	 * @param position
	 */
	public Summon(int objId, CreatureController<? extends Creature> controller, SpawnTemplate spawnTemplate,
		VisibleObjectTemplate objectTemplate, SummonStatsTemplate statsTemplate, byte level)
	{
		super(objId, controller, spawnTemplate, objectTemplate, new WorldPosition());

		controller.setOwner(this);
		this.level = level;
		super.setGameStats(new SummonGameStats(this, statsTemplate));
		super.setLifeStats(new SummonLifeStats(this));
		
		this.mode = SummonMode.GUARD;
	}

	/**
	 * @return the owner
	 */
	@Override
	public Player getMaster()
	{
		return master;
	}

	/**
	 * @param master
	 *           the master to set
	 */
	public void setMaster(Player master)
	{
		this.master = master;
	}
	
	@Override
	public Creature getActingCreature()
	{
		return this.master;
	}
	
	@Override
	public String getName()
	{
		return objectTemplate.getName();
	}

	/**
	 * @return the level
	 */
	@Override
	public byte getLevel()
	{
		return level;
	}

	@Override
	public void initializeAi()
	{
		// TODO Auto-generated method stub
	}

	@Override
	public NpcTemplate getObjectTemplate()
	{
		return (NpcTemplate) super.getObjectTemplate();
	}

	public int getNpcId()
	{
		return getObjectTemplate().getTemplateId();
	}
	
	public int getNameId()
	{
		return getObjectTemplate().getNameId();
	}

	/**
	 * @return NpcObjectType.SUMMON
	 */
	@Override
	public NpcObjectType getNpcObjectType()
	{
		return NpcObjectType.SUMMON;
	}

	@Override
	public SummonController getController()
	{
		return (SummonController) super.getController();
	}

	/**
	 * @return the mode
	 */
	public SummonMode getMode()
	{
		return mode;
	}

	/**
	 * @param mode the mode to set
	 */
	public void setMode(SummonMode mode)
	{
		this.mode = mode;
	}

	@Override
	protected boolean isEnemyNpc(Npc visibleObject)
	{
		if(master == null)
			return false;
		return master.isEnemyNpc(visibleObject);
	}

	@Override
	protected boolean isEnemyPlayer(Player visibleObject)
	{
		if(master == null)
			return false;
		return master.isEnemyPlayer(visibleObject);
	}

	@Override
	protected boolean isEnemySummon(Summon summon)
	{
		if(master == null)
			return false;
		return master.isEnemySummon(summon);
	}

	@Override
	public String getTribe()
	{
		if(master == null)
			return "";
		return master.getTribe();
	}

	@Override
	public boolean isAggressiveTo(Creature creature)
	{
		return creature.isAggroFrom(this);
	}
	
	@Override
	public boolean isAggroFrom(Npc npc)
	{
		if(master == null)
			return false;
		return master.isAggroFrom(npc);
	}
}
