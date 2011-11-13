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
package gameserver.network.aion.clientpackets;


import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.AionObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_STANCE_STATE;
import gameserver.utils.PacketSendUtility;

/**

 *
 */
public class CM_SKILL_DEACTIVATE extends AionClientPacket
{
	private int skillId;
	
	public CM_SKILL_DEACTIVATE(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		skillId = readD();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		Player player = getConnection().getActivePlayer();
		if (player != null && player.getEffectController() != null)
			player.getEffectController().removeNoshowEffect(skillId);
		
		if (DataManager.SKILL_DATA.getSkillTemplate(skillId).isStance())
			PacketSendUtility.broadcastPacketAndReceive(player, new SM_STANCE_STATE(((AionObject)player).getObjectId(), 0));
	}
}
