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

import gameserver.ai.AI;
import gameserver.controllers.CreatureController;
import gameserver.controllers.MoveController;
import gameserver.controllers.ObserveController;
import gameserver.controllers.attack.AggroList;
import gameserver.controllers.effect.EffectController;
import gameserver.model.TaskId;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.state.CreatureSeeState;
import gameserver.model.gameobjects.state.CreatureState;
import gameserver.model.gameobjects.state.CreatureVisualState;
import gameserver.model.gameobjects.stats.CreatureGameStats;
import gameserver.model.gameobjects.stats.CreatureLifeStats;
import gameserver.model.templates.VisibleObjectTemplate;
import gameserver.model.templates.item.EAttackType;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.skill.effect.EffectId;
import gameserver.skill.model.Skill;
import gameserver.task.impl.PacketBroadcaster;
import gameserver.task.impl.PacketBroadcaster.BroadcastMode;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.WorldPosition;
import javolution.util.FastMap;
import org.apache.commons.lang.StringUtils;
import java.util.Map;
import java.util.concurrent.Future;

public abstract class Creature extends VisibleObject
{
	/**
	 * Reference to AI
	 */
	protected AI<? extends Creature> ai;
	private CreatureLifeStats<? extends Creature> lifeStats;
	private CreatureGameStats<? extends Creature> gameStats;
	private EffectController effectController;
	private MoveController moveController = null;
	private int state = CreatureState.ACTIVE.getId();
	private int visualState = CreatureVisualState.VISIBLE.getId();
	private int seeState = CreatureSeeState.NORMAL.getId();
	private boolean isInCombat = false;
	private Skill castingSkill;
	private Map<Integer, Long> skillCoolDowns;
	private int transformedModelId;
	private ObserveController 	observeController;
	protected EAttackType attackType = EAttackType.PHYSICAL;
	private AggroList aggroList;

	/**
	 * 
	 * @param objId
	 * @param controller
	 * @param spawnTemplate
	 * @param objectTemplate
	 * @param position
	 */
	public Creature(int objId, CreatureController<? extends Creature> controller,
		SpawnTemplate spawnTemplate, VisibleObjectTemplate objectTemplate, WorldPosition position)
	{
		super(objId, controller, spawnTemplate, objectTemplate, position);
		initializeAi();

		this.observeController = new ObserveController();
		
		this.aggroList = new AggroList(this);
	}

	/**
	 * Return CreatureController of this Creature object.
	 * 
	 * @return CreatureController.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public CreatureController getController()
	{
		return (CreatureController) super.getController();
	}

	/**
	 * @return the lifeStats
	 */
	public CreatureLifeStats<? extends Creature> getLifeStats()
	{
		return  lifeStats;
	}

	/**
	 * @param lifeStats the lifeStats to set
	 */
	public void setLifeStats(CreatureLifeStats<? extends Creature> lifeStats)
	{
		this.lifeStats = lifeStats;
	}

	/**
	 * @return the gameStats
	 */
	public CreatureGameStats<? extends Creature> getGameStats()
	{
		return gameStats;
	}

	/**
	 * @param gameStats the gameStats to set
	 */
	public void setGameStats(CreatureGameStats<? extends Creature> gameStats)
	{
		this.gameStats = gameStats;
	}

	public abstract byte getLevel();

	public abstract void initializeAi();

	/**
	 * @return the effectController
	 */
	public EffectController getEffectController()
	{
		return effectController;
	}

	/**
	 * @param effectController the effectController to set
	 */
	public void setEffectController(EffectController effectController)
	{
		this.effectController = effectController;
	}

	/**
	 * @return the npcAi
	 */
	public AI<? extends Creature> getAi()
	{
		return ai != null ? ai : AI.dummyAi();
	}

	/**
	 * @param ai the ai to set
	 */
	public void setAi(AI<? extends Creature> ai)
	{
		this.ai = ai;
	}
	
	/**
	 * Is creature casting some skill
	 * 
	 * @return
	 */
	public boolean isCasting()
	{
		return castingSkill != null;
	}
	
	/**
	 * Set current casting skill or null when skill ends
	 * 
	 * @param castingSkill
	 */
	public void setCasting(Skill castingSkill)
	{
		this.castingSkill = castingSkill;
	}
	
	/**
	 * Current casting skill id
	 * 
	 * @return
	 */
	public int getCastingSkillId()
	{
		return castingSkill != null ? castingSkill.getSkillTemplate().getSkillId() : 0;
	}
	
	/**
	 * Current casting skill
	 * 
	 * @return
	 */
	public Skill getCastingSkill()
	{
		return castingSkill;
	}

	/**
	 * Set or unset Creature in combat state
	 * 
	 * @param isInCombat
	 */
	private void setInCombat(boolean isInCombat)
	{
		if(isInCombat)
		{
			this.getController().cancelTask(TaskId.CREATURE_COMBAT);
			this.isInCombat = isInCombat;
		}
		else
			this.isInCombat = false;
	}

 	/**
	 * Is the creature in combat state
	 * 
	 * @return isInCombat
	 */
	public boolean isInCombat()
	{
		return isInCombat;
	}
	
	/**
	 * Sets combat state to true for time in seconds
	 * @param creature
	 * @param time
	 */
	@SuppressWarnings({ "unchecked" })
	public void setCombatState(int time)
	{
		setInCombat(true);
		Future<?> task = ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				if(isInCombat())
					setInCombat(false);
			}
		}, time * 1000);

		this.getController().addTask(TaskId.CREATURE_COMBAT, task);
	}

	/**
	 * All abnormal effects are checked that disable movements
	 * 
	 * @return
	 */
	public boolean canPerformMove()
	{
		return !(getEffectController().isAbnormalState(EffectId.CANT_MOVE_STATE) || !isSpawned());
	}
	
	/**
	 * All abnormal effects are checked that disable attack
	 * @return
	 */
	public boolean canAttack()
	{
		return !(getEffectController().isAbnormalState(EffectId.CANT_ATTACK_STATE) || isCasting() || isInState(CreatureState.RESTING) || isInState(CreatureState.PRIVATE_SHOP));
	}

	/**
	 * @return state
	 */
	public int getState()
	{
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(CreatureState state)
	{
		int oldState = this.state;
		this.state |= state.getId();
		if (oldState != this.state)
			observeController.notifyStateChangeObservers(state, true);
	}
	
	/** 
	 * @param state taken usually from templates
	 */
	public void setState(int state)
	{
		this.state = state;
	}

	public void unsetState(CreatureState state)
	{
		int oldState = this.state;
		this.state &= ~state.getId();
		if (oldState != this.state)
			observeController.notifyStateChangeObservers(state, false);
	}

	public boolean isInState(CreatureState state)
	{
		int isState = this.state & state.getId();

        return isState == state.getId();

    }

	/**
	 * @return visualState
	 */
	public int getVisualState()
	{
		return visualState;
	}

	/**
	 * @param visualState the visualState to set
	 */
	public void setVisualState(CreatureVisualState visualState)
	{
		this.visualState |= visualState.getId();
	}

	public void unsetVisualState(CreatureVisualState visualState)
	{
		this.visualState &= ~visualState.getId();
	}

	public boolean isInVisualState(CreatureVisualState visualState)
	{
		int isVisualState = this.visualState & visualState.getId();

        return isVisualState == visualState.getId();

    }

	/**
	 * @return seeState
	 */
	public int getSeeState()
	{
		return seeState;
	}

	/**
	 * @param seeState the seeState to set
	 */
	public void setSeeState(CreatureSeeState seeState)
	{
		this.seeState |= seeState.getId();
	}

	public void unsetSeeState(CreatureSeeState seeState)
	{
		this.seeState &= ~seeState.getId();
	}

	public boolean isInSeeState(CreatureSeeState seeState)
	{
		int isSeeState = this.seeState & seeState.getId();

        return isSeeState == seeState.getId();

    }

	/**
	 * @return the transformedModelId
	 */
	public int getTransformedModelId()
	{
		return transformedModelId;
	}

	/**
	 * @param transformedModelId the transformedModelId to set
	 */
	public void setTransformedModelId(int transformedModelId)
	{
		this.transformedModelId = transformedModelId;
	}

	/**
	 * @return the moveController
	 */
	public MoveController getMoveController()
	{
		if(moveController == null)
			moveController = new MoveController(this);
		return moveController;
	}
	
	public void setMoveController(MoveController mc)
	{
		moveController = mc;
	}
	/**
	 * @return the aggroList
	 */
	public AggroList getAggroList()
	{
		return aggroList;
	}
	
	/**
	 * PacketBroadcasterMask
	 */
	private volatile byte packetBroadcastMask;

	/**
	 * This is adding broadcast to player.
	 */
	public final void addPacketBroadcastMask(BroadcastMode mode)
	{
		packetBroadcastMask |= mode.mask();

		PacketBroadcaster.getInstance().add(this);
	}

	/**
	 * This is removing broadcast from player.
	 */
	public final void removePacketBroadcastMask(BroadcastMode mode)
	{
		packetBroadcastMask &= ~mode.mask();
	}

	/**
	 * Broadcast getter.
	 */
	public final byte getPacketBroadcastMask()
	{
		return packetBroadcastMask;
	}

	/**
	 * @return the observeController
	 */
	public ObserveController getObserveController()
	{
		return observeController;
	}
	
	public void setObserveController(ObserveController oc)
	{
		observeController = oc;
	}
	
	/**
	 * 
	 * @param visibleObject
	 * @return
	 */
	public boolean isEnemy(VisibleObject visibleObject)
	{
		if(visibleObject instanceof Npc)
			return isEnemyNpc((Npc) visibleObject);
		else if(visibleObject instanceof Player)
			return isEnemyPlayer((Player) visibleObject);
		else if(visibleObject instanceof Summon)
			return isEnemySummon((Summon) visibleObject);
		
		return false;
	}

	/**
	 * @param summon
	 * @return
	 */
	protected boolean isEnemySummon(Summon summon)
	{
		return false;
	}

	/**
	 * @param player
	 * @return
	 */
	protected boolean isEnemyPlayer(Player player)
	{
		return false;
	}

	/**
	 * @param npc
	 * @return
	 */
	protected boolean isEnemyNpc(Npc npc)
	{
		return false;
	}
	
	public String getTribe()
	{
		return StringUtils.EMPTY;
	}
	
	/**
	 * 
	 * @param creature
	 * @return
	 */
	public boolean isAggressiveTo(Creature creature)
	{
		return false;
	}
	
	/**
	 * 
	 * @param npc
	 * @return
	 */
	public boolean isAggroFrom(Npc npc)
	{
		return false;
	}
	
	/**
	 * 
	 * @param npc
	 * @return
	 */
	public boolean isHostileFrom(Npc npc)
	{
		return false;
	}
	
	
	public boolean isSupportFrom(Npc npc)
	{
		return false;
	}
	/**
	 * 
	 * @param player
	 * @return
	 */
	public boolean isAggroFrom(Player player)
	{
		return false;
	}
	
	/**
	 * 
	 * @param summon
	 * @return
	 */
	public boolean isAggroFrom(Summon summon)
	{
		return isAggroFrom(summon.getMaster());
	}
	/**
	 * 
	 * @param visibleObject
	 * @return
	 */
	public boolean canSee(VisibleObject visibleObject)
	{
		if(visibleObject instanceof Npc)
			return canSeeNpc((Npc) visibleObject);
		else if(visibleObject instanceof Player)
			return canSeePlayer((Player) visibleObject);
		
		return true;
	}
	
	/**
	 * @param visibleObject
	 * @return
	 */
	protected boolean canSeePlayer(Player visibleObject)
	{
		return true;
	}

	/**
	 * @param visibleObject
	 * @return
	 */
	protected boolean canSeeNpc(Npc visibleObject)
	{
		return true;
	}
	
	/**
	 * @return NpcObjectType.NORMAL
	 */
	public NpcObjectType getNpcObjectType()
	{
		return NpcObjectType.NORMAL;
	}
	
	/**
	 * For summons and different kind of servants<br>
	 * it will return currently acting player.<br>
	 * 
	 * This method is used for duel and enemy relations,<br>
	 * rewards<br>
	 * 
	 * @return Master of this creature or self
	 */
	public Creature getMaster()
	{
		return this;
	}
	
	/**
	 * For summons it will return summon object and for <br>
	 * servants - player object.<br>
	 * 
	 * Used to find attackable target for npcs.<br>
	 * 
	 * @return acting master - player in case of servants
	 */
	public Creature getActingCreature()
	{
		return this;
	}
	
	/**
	 * 
	 * @param skillId
	 * @return
	 */
	public boolean isSkillDisabled(int delayId)
	{
		if(skillCoolDowns == null)
			return false;
		
		Long coolDown = skillCoolDowns.get(delayId);
		if(coolDown == null)
			return false;
		
		
		if (coolDown < System.currentTimeMillis())
		{
			skillCoolDowns.remove(delayId);
			return false;
		}
		
		return true;
	}
	
	/**
	 * 
	 * @param skillId
	 * @return
	 */
	public long getSkillCoolDown(int delayId)
	{
		if(skillCoolDowns == null || !skillCoolDowns.containsKey(delayId))
			return 0;
		
		return skillCoolDowns.get(delayId);
	}
	
	/**
	 * 
	 * @param skillId
	 * @param time
	 */
	public void setSkillCoolDown(int delayId, long time)
	{
		if(skillCoolDowns == null)
			skillCoolDowns = new FastMap<Integer, Long>().shared();
		
		skillCoolDowns.put(delayId, time);
	}

	/**
	 * @return the skillCoolDowns
	 */
	public Map<Integer, Long> getSkillCoolDowns()
	{
		return skillCoolDowns;
	}
	
	/**
	 * 
	 * @param skillId
	 */
	public void removeSkillCoolDown(int delayId)
	{
		if(skillCoolDowns == null)
			return;
		skillCoolDowns.remove(delayId);
	}

	public EAttackType getAttackType() 
	{
		return attackType;
	}
	public void setAttackType(EAttackType attackType) 
	{
		this.attackType = attackType;
	}
	public void unsetAttackType() 
	{
		this.attackType = EAttackType.PHYSICAL;
	}
}
