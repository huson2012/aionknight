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
package gameserver.ai.state.handler;


import gameserver.ai.AI;
import gameserver.ai.desires.impl.MoveToHomeDesire;
import gameserver.ai.state.AIState;
import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Npc;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.utils.PacketSendUtility;

/**

 *
 */
public class MovingToHomeStateHandler extends StateHandler
{
	@Override
	public AIState getState()
	{
		return AIState.MOVINGTOHOME;
	}
	
	/**
	 * State MOVINGTOHOME
	 * AI MonsterAi
	 * AI GuardAi
	 */
	@Override
	public void handleState(AIState state, AI<?> ai)
	{
		ai.clearDesires();
		Npc npc = (Npc) ai.getOwner();
		npc.setTarget(null);
		npc.getAggroList().clear();
		PacketSendUtility.broadcastPacket(npc, new SM_EMOTION(npc, EmotionType.NEUTRALMODE, 0, 0));
		ai.addDesire(new MoveToHomeDesire(npc, AIState.MOVINGTOHOME.getPriority()));
		
		ai.schedule();
	}
}
