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

import commons.utils.Rnd;
import gameserver.ai.npcai.AggressiveAi;
import gameserver.ai.npcai.NpcAi;
import gameserver.configs.main.CustomConfig;
import gameserver.configs.main.DropConfig;
import gameserver.configs.main.NpcMovementConfig;
import gameserver.controllers.NpcController;
import gameserver.dataholders.DataManager;
import gameserver.model.NpcType;
import gameserver.model.Race;
import gameserver.model.ShoutEventType;
import gameserver.model.drop.DropTemplate;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.state.CreatureState;
import gameserver.model.gameobjects.stats.NpcGameStats;
import gameserver.model.gameobjects.stats.NpcLifeStats;
import gameserver.model.templates.NpcTemplate;
import gameserver.model.templates.VisibleObjectTemplate;
import gameserver.model.templates.bonus.InventoryBonusType;
import gameserver.model.templates.item.ItemRace;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.model.templates.npcskill.NpcSkillList;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.model.templates.stats.NpcRank;
import gameserver.services.NpcShoutsService;
import gameserver.utils.MathUtil;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.KnownList;
import gameserver.world.NpcKnownList;
import gameserver.world.WorldPosition;
import gameserver.world.WorldType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

public class Npc extends Creature
{
	
	private NpcSkillList npcSkillList;
	public double lastShoutedSeconds;
	
	private ScheduledFuture<?> shoutThread;
	
	/**
	 * Constructor creating instance of Npc.
	 * 
	 * @param spawn
	 *           SpawnTemplate which is used to spawn this npc
	 * @param objId
	 *           unique objId
	 */
	public Npc(int objId, NpcController controller, SpawnTemplate spawnTemplate, VisibleObjectTemplate objectTemplate)
	{
		super(objId, controller, spawnTemplate, objectTemplate, new WorldPosition());
		controller.setOwner(this);
		
		super.setGameStats(new NpcGameStats(this));
		super.setLifeStats(new NpcLifeStats(this));
		lastShoutedSeconds = System.currentTimeMillis() / 1000;
		
		final Npc npc = this;
		
		if(NpcShoutsService.getInstance().hasShouts(npc.getNpcId(), ShoutEventType.IDLE))
		{
			shoutThread = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable(){
				
				@Override
				public void run()
				{
					NpcShoutsService.getInstance().handleEvent(npc, npc, ShoutEventType.IDLE);
				}
			}, Rnd.get(0, 180000), Rnd.get(175000, 185000));
		}
		
	}

	@Override
	public NpcTemplate getObjectTemplate()
	{
		return (NpcTemplate) objectTemplate;
	}
	@Override
	public String getName()
	{
		return getObjectTemplate().getName();
	}

	public int getNpcId()
	{
		return getObjectTemplate().getTemplateId();
	}

	@Override
	public byte getLevel()
	{
		return getObjectTemplate().getLevel();
	}

	/**
	 * @return the lifeStats
	 */
	@Override
	public NpcLifeStats getLifeStats()
	{
		return (NpcLifeStats) super.getLifeStats();
	}

	/**
	 * @return the gameStats
	 */
	@Override
	public NpcGameStats getGameStats()
	{
		return (NpcGameStats) super.getGameStats();
	}
		
	@Override
	public NpcController getController()
	{
		return (NpcController) super.getController();
	}

	public boolean hasWalkRoutes()
	{
		return getSpawn().getWalkerId() > 0 || (getSpawn().hasRandomWalk() && NpcMovementConfig.ACTIVE_NPC_MOVEMENT);
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isAggressive()
	{
		String currentTribe = getObjectTemplate().getTribe();
		return DataManager.TRIBE_RELATIONS_DATA.hasAggressiveRelations(currentTribe) || isGuard() || isHostile();
	}
	
	public boolean isHostile()
	{
		String currentTribe = getObjectTemplate().getTribe();
		return DataManager.TRIBE_RELATIONS_DATA.hasHostileRelations(currentTribe);
	}
	
	@Override
	public boolean isAggressiveTo(Creature creature)
	{
		if(creature instanceof Player || creature instanceof Summon)
		{
			if(this.getWorldId() != 300080000 && this.getWorldId() != 300090000 && this.getWorldId() != 300060000
				&& !this.isGuard() && this.getLevel() + 10 <= creature.getLevel())
				return false;
			
			Player player = (Player) (creature instanceof Player ? creature : creature.getMaster());
			if(player.getAdminNeutral())
				return false;
		}

		if(DataManager.TRIBE_RELATIONS_DATA.isAggressiveRelation(getTribe(), creature.getTribe())
			|| DataManager.TRIBE_RELATIONS_DATA.isHostileRelation(getTribe(), creature.getTribe()))
			return true;

        return creature instanceof Npc && guardAgainst((Npc) creature);

    }
	
	@Override
	public boolean isAggroFrom(Npc npc)
	{
		return DataManager.TRIBE_RELATIONS_DATA.isAggressiveRelation(npc.getTribe(), getTribe());
	}
	
	@Override
	public boolean isHostileFrom(Npc npc)
	{
		return DataManager.TRIBE_RELATIONS_DATA.isHostileRelation(npc.getTribe(), getTribe());
	}

	@Override
	public boolean isSupportFrom(Npc npc)
	{
		return DataManager.TRIBE_RELATIONS_DATA.isSupportRelation(npc.getTribe(), getTribe());
	}

	/** 
	 * 
	 * @return
	 */
	public boolean isGuard()
	{
		String currentTribe = getTribe();
		return DataManager.TRIBE_RELATIONS_DATA.isGuardDark(currentTribe)
			|| DataManager.TRIBE_RELATIONS_DATA.isGuardLight(currentTribe)
			|| DataManager.TRIBE_RELATIONS_DATA.isGuardDrakan(currentTribe);
	}
	
	@Override
	public String getTribe()
	{
		return this.getObjectTemplate().getTribe();
	}
	
	public int getAggroRange()
	{
		return getObjectTemplate().getAggroRange();
	}
	
	@Override
	public void initializeAi()
	{
		if(isAggressive() && !CustomConfig.DISABLE_MOB_AGGRO)
			this.ai = new AggressiveAi();
		else
			this.ai = new NpcAi();
		ai.setOwner(this);
	}

	/**
	 * Check whether npc located at initial spawn location
	 * 
	 * @return true or false
	 */
	public boolean isAtSpawnLocation()
	{
		return MathUtil.getDistance(getSpawn().getX(), getSpawn().getY(), getSpawn().getZ(),
			getX(), getY(), getZ()) < 3 ;
	}

	/**
	 * @return the npcSkillList
	 */
	public NpcSkillList getNpcSkillList()
	{
		return npcSkillList;
	}

	/**
	 * @param npcSkillList the npcSkillList to set
	 */
	public void setNpcSkillList(NpcSkillList npcSkillList)
	{
		this.npcSkillList = npcSkillList;
	}
	
	@Override
	protected boolean isEnemyNpc(Npc visibleObject)
	{
		if(this.getObjectTemplate().getNpcType() == NpcType.NEUTRAL || this.getObjectTemplate().getNpcType() == NpcType.ARTIFACT)
			return false;
		
		String ownerTribe = getTribe();
		
		if(ownerTribe.equals(visibleObject.getTribe()))
			return false;

		if((DataManager.TRIBE_RELATIONS_DATA.isAggressiveRelation(ownerTribe, visibleObject.getTribe())
		|| !DataManager.TRIBE_RELATIONS_DATA.isFriendlyRelation(ownerTribe, visibleObject.getTribe())))
			return true;

		guardAgainst(visibleObject);

		return false;
	}
	
	/**
	 * Represents the action of a guard defending its position
	 * @param npc
	 * @return true if this npc is a guard and the given npc is aggro to their PC race
	 */
	protected boolean guardAgainst(Npc npc)
	{
		if(DataManager.TRIBE_RELATIONS_DATA.isGuardLight(getTribe())
				&& DataManager.TRIBE_RELATIONS_DATA.isAggressiveRelation(npc.getTribe(), "PC"))
			return true;
		else if(DataManager.TRIBE_RELATIONS_DATA.isGuardDark(getTribe())
				&& DataManager.TRIBE_RELATIONS_DATA.isAggressiveRelation(npc.getTribe(), "PC_DARK"))
			return true;

		return false;
	}
	@Override
	protected boolean isEnemyPlayer(Player visibleObject)
	{
		Player player = (Player)visibleObject;
        return getObjectTemplate().getRace() != player.getCommonData().getRace();

    }
	
	@Override
	protected boolean isEnemySummon(Summon visibleObject)
	{
		return true;//TODO
	}
	
	@Override
	protected boolean canSeeNpc(Npc npc)
	{
		return true; //TODO
	}

	@Override
	protected boolean canSeePlayer(Player player)
	{
		if(!player.isInState(CreatureState.ACTIVE))
			return false;
		
		if (player.getVisualState() == 1 && getObjectTemplate().getRank() == NpcRank.NORMAL)
		   return false;
		
		if (player.getVisualState() == 2 && (getObjectTemplate().getRank() == NpcRank.ELITE || getObjectTemplate().getRank() == NpcRank.NORMAL))
		   return false;

        return player.getVisualState() < 3;

    }
	
	@Override
	public void setKnownlist(KnownList knownList)
	{
		if(knownList != null && !(knownList instanceof NpcKnownList))
		{
			throw new RuntimeException("Invalid knownlist "+knownList.getClass().getSimpleName()+" for "+getClass().getSimpleName());
		}
		super.setKnownlist(knownList);
	}
	
	@Override
	public NpcKnownList getKnownList()
	{
		return (NpcKnownList)super.getKnownList();
	}
	
	public Set<DropTemplate> getWorldDrops(Player player)
	{
		NpcTemplate template = this.getObjectTemplate();
		InventoryBonusType dropType = InventoryBonusType.NONE;
		
		// Just simulating item drops by their race
		if (template.getRace() == Race.ASMODIANS)
			dropType = InventoryBonusType.WORLD_DROP_A;
		else if (template.getRace() == Race.ELYOS)
			dropType = InventoryBonusType.WORLD_DROP_E;
		else if (template.getRace() == Race.BEAST ||
				 template.getRace() == Race.DEMIHUMANOID ||
				 template.getRace() == Race.DRAKAN ||
				 template.getRace() == Race.BROWNIE ||
				 template.getRace() == Race.LIZARDMAN ||
				 template.getRace() == Race.MAGICALMONSTER ||
				 template.getRace() == Race.NAGA ||
				 template.getRace() == Race.UNDEAD ||
				 template.getRace() == Race.LYCAN)
		{
			if (this.getWorldType() == WorldType.BALAUREA || this.getWorldType() == WorldType.ABYSS)
				dropType = InventoryBonusType.WORLD_DROP_B;
			else if (this.getWorldType() == WorldType.ASMODAE)
				dropType = InventoryBonusType.WORLD_DROP_A;
			else if (this.getWorldType() == WorldType.ELYSEA)
				dropType = InventoryBonusType.WORLD_DROP_E;
			else if (template.getLevel() >= 50)
				dropType = InventoryBonusType.WORLD_DROP_B;
			else
				return null; // nothing to drop
		}
		else
			return null; // nothing to drop
		
		int startLevel = template.getLevel() / 10 * 10;
		List<Integer> itemIds = DataManager.ITEM_DATA.getBonusItems(dropType, startLevel, startLevel + 5);

		if (itemIds.isEmpty())
			return null;
		
		Set<DropTemplate> dropTemplates = new HashSet<DropTemplate>();
		int itemId = itemIds.get(Rnd.get(itemIds.size()));
		
		ItemTemplate itemTemplate = DataManager.ITEM_DATA.getItemTemplate(itemId);
		
		// check just in case the item race (world drops are for both races)
		if (itemTemplate.getRace() != ItemRace.ALL)
		{
			if (!player.getCommonData().getRace().toString().equals(itemTemplate.getRace().toString()))
				return null;
		}

		float chance = 0;
		switch (itemTemplate.getItemQuality())
		{
			case COMMON:
				chance = DropConfig.WORLD_DROP_CHANCE_COMMON;
				break;
			case RARE:
				chance = DropConfig.WORLD_DROP_CHANCE_RARE;
				break;
			case LEGEND:
				chance = DropConfig.WORLD_DROP_CHANCE_LEGENDARY;
				break;
			case UNIQUE:
				chance = DropConfig.WORLD_DROP_CHANCE_UNIQUE;
		}
		
		dropTemplates.add(new DropTemplate(this.getNpcId(), itemId, 1, 1, chance));
		
		return dropTemplates;
	}
	
	public boolean mayShout()
	{
		return ((System.currentTimeMillis() / 1000) - lastShoutedSeconds) > 16;
	}
	
	public void shout()
	{
		lastShoutedSeconds = System.currentTimeMillis() / 1000;
	}
	
	public void stopShoutThread()
	{
		if(shoutThread != null)
			shoutThread.cancel(false);
	}	
}
