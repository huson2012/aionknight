/**
 * This file is part of Aion-Knight <aionunique.smfnew.com>.
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


import gameserver.model.gameobjects.AionObject;
import gameserver.model.gameobjects.player.DeniedStatus;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.services.DuelService;
import gameserver.world.World;

/**
 * 
 * @author blakawk
 * 
 */
public class CM_DUEL_REQUEST extends AionClientPacket
{
	/**
	 * Target object id that client wants to start duel with
	 */
	private int			objectId;

	/**
	 * Constructs new instance of <tt>CM_DUEL_REQUEST</tt> packet
	 * 
	 * @param opcode
	 */
	public CM_DUEL_REQUEST(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		objectId = readD();
	}

	@Override
	protected void runImpl()
	{
		Player activePlayer = getConnection().getActivePlayer();
		AionObject target = World.getInstance().findAionObject(objectId);

		if(activePlayer == null || target == null)
			return;

		if(target instanceof Player)
		{
			Player targetPlayer = (Player) target;

			if(targetPlayer.getPlayerSettings().isInDeniedStatus(DeniedStatus.DUEL))
			{
				sendPacket(SM_SYSTEM_MESSAGE.STR_MSG_REJECTED_DUEL(targetPlayer.getName()));
				return;
			}
			DuelService duelService = DuelService.getInstance();
			duelService.onDuelRequest(activePlayer, targetPlayer);
			duelService.confirmDuelWith(activePlayer, targetPlayer);
		}
		else
		{
			sendPacket(SM_SYSTEM_MESSAGE.DUEL_PARTNER_INVALID(target.getName()));
		}
	}
}
