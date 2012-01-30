/*
 * Emulator game server Aion 2.7 from the command of developers 'Aion-Knight Dev. Team' is
 * free software; you can redistribute it and/or modify it under the terms of
 * GNU affero general Public License (GNU GPL)as published by the free software
 * security (FSF), or to License version 3 or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranties related to
 * CONSUMER PROPERTIES and SUITABILITY FOR CERTAIN PURPOSES. For details, see
 * General Public License is the GNU.
 *
 * You should have received a copy of the GNU affero general Public License along with this program.
 * If it is not, write to the Free Software Foundation, Inc., 675 Mass Ave,
 * Cambridge, MA 02139, USA
 *
 * Web developers : http://aion-knight.ru
 * Support of the game client : Aion 2.7- 'Arena of Death' (Innova)
 * The version of the server : Aion-Knight 2.7 (Beta version)
 */

package gameserver.network.aion.serverpackets;

import gameserver.configs.main.CustomConfig;
import gameserver.model.gameobjects.player.Emotion;
import gameserver.model.gameobjects.player.EmotionList;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.services.EmotionService;
import java.nio.ByteBuffer;

public class SM_EMOTION_LIST extends AionServerPacket
{
	private EmotionList	emotionList;

	public SM_EMOTION_LIST(Player player)
	{
		this.emotionList = player.getEmotionList();
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeC(buf, 0x00);

		if(CustomConfig.RETAIL_EMOTIONS)
		{
			Player player = emotionList.getOwner();
			EmotionService.removeExpiredEmotions(player);
			
			writeH(buf, emotionList.size());

			for(Emotion emotion : emotionList.getEmotions())
			{
				writeH(buf, emotion.getEmotionId());
				writeD(buf, (int) emotion.getEmotionTimeLeft());
			}
		}else{
			writeH(buf, 66);
			for (int i = 0; i < 66; i++)
			{
				writeH(buf, 64 + i);
				writeD(buf, 0x00);
			}
		}
	}
}