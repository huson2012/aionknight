/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a  copy  of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */
package gameserver.skill.effect;

import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.RequestResponseHandler;
import gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.services.TeleportService;
import gameserver.skill.model.Effect;
import gameserver.utils.PacketSendUtility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;



/**
 * @author Bio
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RecallInstantEffect")
public class RecallInstantEffect extends EffectTemplate
{

	@Override
	public void applyEffect(Effect effect)
	{
		final Player effector = (Player) effect.getEffector();
		final Player effected = (Player) effect.getEffected();

		if(effected.isInCombat() || 
			effector.getEffectController().isAbnormalState(EffectId.CANT_ATTACK_STATE) || 
			effected.getLifeStats().isAlreadyDead() ||
			effector.getWorldId() != effected.getWorldId())
		{
			PacketSendUtility.sendPacket(effector, SM_SYSTEM_MESSAGE.RECALL_CANNOT_ACCEPT_EFFECT(effected.getName()));
			return;
		}

		RequestResponseHandler rrh = new RequestResponseHandler((Creature)effector){
			@Override
			public void denyRequest(Creature effector, Player effected)
			{
				PacketSendUtility.sendPacket((Player) effector, SM_SYSTEM_MESSAGE.RECALL_REJECTED_EFFECT(effected.getName()));
				PacketSendUtility.sendPacket(effected, SM_SYSTEM_MESSAGE.RECALL_REJECT_EFFECT(effector.getName()));
			}

			@Override
			public void acceptRequest(Creature effector, Player effected)
			{
				if(effected.isInCombat() || effector.getEffectController().isAbnormalState(EffectId.CANT_ATTACK_STATE) || effected.getLifeStats().isAlreadyDead())
					return;

				TeleportService.teleportTo(effected, effector.getWorldId(), effector.getInstanceId(), effector.getX(), effector.getY(), effector.getZ(), effector.getHeading(), 0);
			}
		};

		if(!effected.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_SUMMON_PARTY_DO_YOU_ACCEPT_REQUEST, rrh))
			PacketSendUtility.sendPacket(effector, SM_SYSTEM_MESSAGE.RECALL_DUPLICATE_EFFECT(effected.getName()));//already summoned by someone else
		else
			PacketSendUtility.sendPacket(effected,
				new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_SUMMON_PARTY_DO_YOU_ACCEPT_REQUEST, 0, effector.getName(), "Summon Group Member", 30));

	}
	
	@Override
	public void calculate(Effect effect)
	{
		final Player effector = (Player) effect.getEffector();
		final Player effected = (Player) effect.getEffected();

		if(effected != null && effected instanceof Player && !effector.isEnemyPlayer(effected))
			super.calculate(effect);
	}
}
