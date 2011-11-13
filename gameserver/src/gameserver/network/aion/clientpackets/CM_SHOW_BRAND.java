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


import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.services.AllianceService;
import gameserver.services.GroupService;

/**
 * @author Sweetkr
 * @author Simple
 * @author Kamui
 */
public class CM_SHOW_BRAND extends AionClientPacket
{

	private int				modeId;
	private int				brandId;
	private int				targetObjectId;

	/**
	 * @param opcode
	 */
	public CM_SHOW_BRAND(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		modeId = readD();
		brandId = readD();
		targetObjectId = readD();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		Player player = getConnection().getActivePlayer();
		
		if (player == null)
			return;
		
		if (player.isInGroup())
			GroupService.getInstance().showBrand(player.getPlayerGroup(), modeId, brandId, targetObjectId);
		if (player.isInAlliance())
			AllianceService.getInstance().showBrand(player.getPlayerAlliance(), modeId, brandId, targetObjectId);
	}
}
