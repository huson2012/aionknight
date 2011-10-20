package ru.aionknight.gameserver.ai.state.handler;


import ru.aionknight.gameserver.ai.AI;
import ru.aionknight.gameserver.ai.desires.impl.AttackDesire;
import ru.aionknight.gameserver.ai.desires.impl.MoveToTargetDesire;
import ru.aionknight.gameserver.ai.desires.impl.SkillUseDesire;
import ru.aionknight.gameserver.ai.state.AIState;
import ru.aionknight.gameserver.model.EmotionType;
import ru.aionknight.gameserver.model.gameobjects.Creature;
import ru.aionknight.gameserver.model.gameobjects.Npc;
import ru.aionknight.gameserver.model.gameobjects.state.CreatureState;
import ru.aionknight.gameserver.model.gameobjects.stats.StatEnum;
import ru.aionknight.gameserver.network.aion.serverpackets.SM_EMOTION;
import ru.aionknight.gameserver.network.aion.serverpackets.SM_LOOKATOBJECT;
import ru.aionknight.gameserver.utils.PacketSendUtility;

/**
 * @author ATracer
 *
 */
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
