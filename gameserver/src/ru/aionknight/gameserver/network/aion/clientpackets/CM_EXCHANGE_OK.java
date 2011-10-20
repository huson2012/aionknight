/*
 * This file is part of aion-unique <www.aion-unique.org>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package ru.aionknight.gameserver.network.aion.clientpackets;


import ru.aionknight.gameserver.model.gameobjects.player.Player;
import ru.aionknight.gameserver.network.aion.AionClientPacket;
import ru.aionknight.gameserver.services.ExchangeService;

/**
 * @author -Avol-
 * 
 */
public class CM_EXCHANGE_OK extends AionClientPacket
{

	public CM_EXCHANGE_OK(int opcode)
	{
		super(opcode);
	}

	@Override
	protected void readImpl()
	{

	}

	@Override
	protected void runImpl()
	{	
		final Player activePlayer = getConnection().getActivePlayer();
		ExchangeService.getInstance().confirmExchange(activePlayer);
	}
}