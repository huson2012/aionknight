/**
 * This file is part of Aion-Knight [http://www.aion-knight.ru]
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


import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_PING_RESPONSE;

/**
 * 
 * @author dragoon112
 * 
 */
public class CM_PING_REQUEST extends AionClientPacket
{
	/**
	 * Constructs new instance of <tt>CM_PING_REQUEST </tt> packet
	 * @param opcode
	 */
	public CM_PING_REQUEST(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		// empty
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		sendPacket(new SM_PING_RESPONSE());
	}
}
