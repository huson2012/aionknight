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
package gameserver.network.aion.serverpackets;

import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;

import java.nio.ByteBuffer;


/**
 * 
 * @author -orz-, MrPoke
 * 
 */
public class SM_PLAY_MOVIE extends AionServerPacket
{
	private int	type	= 1;	// if 1: CutSceneMovies else CutScenes
	private int	movieId	= 0;

	public SM_PLAY_MOVIE(int type, int movieId)
	{
		this.type = type;
		this.movieId = movieId;
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeC(buf, type);
		writeD(buf, 0x00);
		writeD(buf, 0x00);
		writeH(buf, movieId);
		writeD(buf, 0x00);
	}
}
