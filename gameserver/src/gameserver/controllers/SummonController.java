/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */
package gameserver.controllers;



import org.apache.log4j.Logger;

import gameserver.configs.main.CustomConfig;
import gameserver.controllers.attack.AttackStatus;
import gameserver.dataholders.DataManager;
import gameserver.model.EmotionType;
import gameserver.model.TaskId;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Summon;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.Summon.SummonMode;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_SUMMON_OWNER_REMOVE;
import gameserver.network.aion.serverpackets.SM_SUMMON_PANEL_REMOVE;
import gameserver.network.aion.serverpackets.SM_SUMMON_UPDATE;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import gameserver.services.LifeStatsRestoreService;
import gameserver.skill.SkillEngine;
import gameserver.skill.model.Skill;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;


/**

 * @author RotO (Attack-speed hack protection)
 */
public class SummonController extends CreatureController<Summon>
{
	private int orderSkillId = 0 ;
	private int orderSkillCooldown = 0;

	private static Logger	log	= Logger.getLogger(SummonController.class);


	@Override
	public void notSee(VisibleObject object, boolean isOutOfRange)
	{
		super.notSee(object, isOutOfRange);
		if(getOwner().getMaster() == null)
			return;
		
		if(object.getObjectId() == getOwner().getMaster().getObjectId())
		{
			release(UnsummonType.DISTANCE);
		}
	}

	@Override
	public Summon getOwner()
	{
		return (Summon) super.getOwner();
	}

	/**
	 * Release summon
	 */
	public void release(final UnsummonType unsummonType)
	{
		final Summon owner = getOwner();

		if(owner.getMode() == SummonMode.RELEASE)
			return;
		owner.setMode(SummonMode.RELEASE);

		final Player master = owner.getMaster();
		final int summonObjId = owner.getObjectId();

		switch(unsummonType)
		{
			case COMMAND:
				PacketSendUtility.sendPacket(master, SM_SYSTEM_MESSAGE.SUMMON_UNSUMMON(getOwner().getNameId()));
				PacketSendUtility.sendPacket(master, new SM_SUMMON_UPDATE(getOwner()));
				Creature target = (Creature)owner.getTarget();
				if (target!=null)
					target.getAggroList().addHate(master, target.getAggroList().getAggroInfo(owner).getHate());
				break;
			case DISTANCE:
				PacketSendUtility.sendPacket(getOwner().getMaster(), SM_SYSTEM_MESSAGE
					.SUMMON_UNSUMMON_BY_TOO_DISTANCE());
				PacketSendUtility.sendPacket(master, new SM_SUMMON_UPDATE(getOwner()));
				break;
			case LOGOUT:
			case UNSPECIFIED:
				break;
		}

		ThreadPoolManager.getInstance().schedule(new Runnable(){

			@Override
			public void run()
			{
				Player temp = master;
				owner.setMaster(null);
				owner.setTarget(null);
				if(temp != null)
					temp.setSummon(null);
				temp = null;
				owner.getController().delete();

				switch(unsummonType)
				{
					case COMMAND:
					case DISTANCE:
					case UNSPECIFIED:
						PacketSendUtility
							.sendPacket(master, SM_SYSTEM_MESSAGE.SUMMON_DISMISSED(getOwner().getNameId()));
						PacketSendUtility.sendPacket(master, new SM_SUMMON_OWNER_REMOVE(summonObjId));

						// TODO temp till found on retail
						PacketSendUtility.sendPacket(master, new SM_SUMMON_PANEL_REMOVE());
						break;
					case LOGOUT:				
						break;
				}
			}
		}, 5000);
	}

	/**
	 * Change to rest mode
	 */
	public void restMode()
	{
		getOwner().setMode(SummonMode.REST);
		Player master = getOwner().getMaster();
		PacketSendUtility.sendPacket(master, SM_SYSTEM_MESSAGE.SUMMON_RESTMODE(getOwner().getNameId()));
		PacketSendUtility.sendPacket(master, new SM_SUMMON_UPDATE(getOwner()));		
		checkCurrentHp();
	}

	private void checkCurrentHp()
	{
		if(!getOwner().getLifeStats().isFullyRestoredHp() && !this.hasTask(TaskId.RESTORE))
			getOwner().getController().addNewTask(TaskId.RESTORE,
				LifeStatsRestoreService.getInstance().scheduleHpRestoreTask(getOwner().getLifeStats()));
	}

	/**
	 * Change to guard mode
	 */
	public void guardMode()
	{
		getOwner().setMode(SummonMode.GUARD);
		Player master = getOwner().getMaster();
		PacketSendUtility.sendPacket(master, SM_SYSTEM_MESSAGE.SUMMON_GUARDMODE(getOwner().getNameId()));
		PacketSendUtility.sendPacket(master, new SM_SUMMON_UPDATE(getOwner()));
		checkCurrentHp();
	}

	/**
	 * Change to attackMode
	 */
	public void attackMode()
	{
		getOwner().setMode(SummonMode.ATTACK);
		Player master = getOwner().getMaster();
		getOwner().setTarget(master.getTarget());
		PacketSendUtility.sendPacket(master, SM_SYSTEM_MESSAGE.SUMMON_ATTACKMODE(getOwner().getNameId()));
		PacketSendUtility.sendPacket(master, new SM_SUMMON_UPDATE(getOwner()));
		getOwner().getController().cancelTask(TaskId.RESTORE, true);
	}

	@Override
	public void attackTarget(Creature target)
	{
		super.attackTarget(target);
	}

	@Override
	public void onAttack(Creature creature, int skillId, TYPE type, int damage, int logId, AttackStatus status, boolean notifyAttackedObservers, boolean sendPacket)
	{
		if(getOwner().getLifeStats().isAlreadyDead())
			return;
		
		//temp 
		if(getOwner().getMode() == SummonMode.RELEASE)
			return;
		
		super.onAttack(creature, skillId, type, damage, logId, status, notifyAttackedObservers, sendPacket);
		PacketSendUtility.sendPacket(getOwner().getMaster(), new SM_SUMMON_UPDATE(getOwner()));
	}

	@Override
	public void onDie(Creature lastAttacker)
	{
		super.onDie(lastAttacker);
		release(UnsummonType.UNSPECIFIED);
		Summon owner = getOwner();
		PacketSendUtility.broadcastPacket(owner, new SM_EMOTION(owner, EmotionType.DIE, 0, lastAttacker == null ? 0 : lastAttacker
			.getObjectId()));
	}
	
	public void useSkill(int skillId, Creature target)
	{
		Creature creature = getOwner();

		Skill skill = SkillEngine.getInstance().getSkill(creature, skillId, 1, target);
		if(skill != null)
		{
			skill.useSkill();
		}
	}
	
	public boolean checkSkillPacket(int skillId,Creature target)
	{
		Skill skill = SkillEngine.getInstance().getSkill(getOwner(), skillId, 1, target);
		long skillCooldownTime = getOwner().getSkillCoolDown(skill.getSkillTemplate().getDelayId());

		if(CustomConfig.LOG_CASTSPELL_COOLDOWNHACK && System.currentTimeMillis() < skillCooldownTime)
		{
			log.info("[CHEAT] " + getOwner().getMaster().getName() + " CM_SUMMON_CASTSPELL packet hack. Packet force send. COOLDOWN AVOID." + " IP: " + getOwner().getMaster().getClientConnection().getIP());
			return false;
		}
		
		int rightSkillId = DataManager.PET_SKILL_DATA.getPetOrderSkill(orderSkillId, getOwner().getNpcId());
		if ( skillId != rightSkillId)
		{
			log.info("[CHEAT] " + getOwner().getMaster().getName() + " CM_SUMMON_CASTSPELL skillId hack" + " IP: " + getOwner().getMaster().getClientConnection().getIP());
			return false;
		}
		return true;
	}
	
	public static enum UnsummonType
	{
		LOGOUT,
		DISTANCE,
		COMMAND,
		UNSPECIFIED
	}

	/**
	 * @param orderSkillId the orderSkillId to set
	 */
	public void setOrderSkillId(int orderSkillId)
	{
		this.orderSkillId = orderSkillId;
	}
	
	/**
	 * @return the orderSkillCooldown
	 */
	public int getOrderSkillCooldown()
	{
		return orderSkillCooldown;
	}

	/**
	 * @param orderSkillCooldown the orderSkillCooldown to set
	 */
	public void setOrderSkillCooldown(int orderSkillCooldown)
	{
		this.orderSkillCooldown = orderSkillCooldown;
	}
}
