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

package gameserver.ai.state.handler;

import gameserver.ai.AI;
import gameserver.ai.desires.impl.AttackDesire;
import gameserver.ai.desires.impl.MoveToTargetDesire;
import gameserver.ai.desires.impl.SkillUseDesire;
import gameserver.ai.state.AIState;
import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.state.CreatureState;
import gameserver.model.gameobjects.stats.StatEnum;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_LOOKATOBJECT;
import gameserver.utils.PacketSendUtility;

public class AttackingStateHandler extends StateHandler
{
	@Override
	public AIState getState()
	{
		return AIState.ATTACKING;
	}

	/**
	 * State ATTACKING
	 * AI MonsterAi
	 * AI AggressiveAi
	 */
	@Override
	public void handleState(AIState state, AI<?> ai)
	{
		ai.clearDesires();

		Creature target = ((Npc)ai.getOwner()).getAggroList().getMostHated();
		if(target == null)
			return;

		Npc owner = (Npc) ai.getOwner();
		owner.setTarget(target);
		PacketSendUtility.broadcastPacket(owner, new SM_LOOKATOBJECT(owner));

		owner.setState(CreatureState.WEAPON_EQUIPPED);
		PacketSendUtility.broadcastPacket(owner,
				new SM_EMOTION(owner, EmotionType.ATTACKMODE, 0, target.getObjectId()));

		float runSpeed = owner.getObjectTemplate().getStatsTemplate().getRunSpeedFight() != 0 ?
				owner.getObjectTemplate().getStatsTemplate().getRunSpeedFight():
				owner.getObjectTemplate().getStatsTemplate().getRunSpeed();

		owner.getGameStats().setStat(StatEnum.SPEED, (int) (runSpeed * 1000));
		owner.getMoveController().setSpeed(((float) owner.getGameStats().getCurrentStat(StatEnum.SPEED))/1000f);

		PacketSendUtility.broadcastPacket(owner,
				new SM_EMOTION(owner, EmotionType.START_EMOTE2, 0, target.getObjectId()));

		owner.getMoveController().setDistance(owner.getGameStats().getCurrentStat(StatEnum.ATTACK_RANGE) / 1000f);
		
		if(owner.getNpcSkillList() != null && !owner.getNpcSkillList().getNpcSkills().isEmpty())
			ai.addDesire(new SkillUseDesire(owner, AIState.USESKILL.getPriority()));
		 
		ai.addDesire(new AttackDesire(owner, target, AIState.ATTACKING.getPriority()));
		
		if (owner.getGameStats().getCurrentStat(StatEnum.SPEED) != 0)
			ai.addDesire(new MoveToTargetDesire(owner, target, owner.getGameStats().getCurrentStat(StatEnum.ATTACK_RANGE) / 1000 + 1, AIState.ATTACKING.getPriority()));//added some tolerance to offset

		ai.schedule();
	}
}