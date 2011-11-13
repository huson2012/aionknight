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

/**
 * Response to SM_QUESTION_WINDOW
 * @author Ben
 * @author Sarynth
 */
public class CM_QUESTION_RESPONSE extends AionClientPacket
{
    private int 			questionid;
    private int 			response;
    @SuppressWarnings("unused")
	private int 			senderid;
    
	public CM_QUESTION_RESPONSE(int opcode) 
	{
		super(opcode);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		questionid = readD();
		
		response = readC(); // y/n
		readC(); // unk 0x00 - 0x01 ?
		readH();
		senderid = readD();
		readD();
		readH();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		Player player = getConnection().getActivePlayer();
		player.getResponseRequester().respond(questionid, response);
	}

}
