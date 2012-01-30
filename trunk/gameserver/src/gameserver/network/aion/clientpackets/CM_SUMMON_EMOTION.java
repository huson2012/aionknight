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

package gameserver.network.aion.clientpackets;

import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Summon;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.state.CreatureState;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.utils.PacketSendUtility;
import org.apache.log4j.Logger;

public class CM_SUMMON_EMOTION extends AionClientPacket
{
	private static final Logger	log	= Logger.getLogger(CM_SUMMON_EMOTION.class);
	
	@SuppressWarnings("unused")
	private int objId;
	
	private int emotionTypeId;
	
	public CM_SUMMON_EMOTION(int opcode)
	{
		super(opcode);
	}

	@Override
	protected void readImpl()
	{
		objId = readD();
		emotionTypeId = readC();
	}

	@Override
	protected void runImpl()
	{
		EmotionType emotionType = EmotionType.getEmotionTypeById(emotionTypeId);
		
		// Unknown Summon Emotion Type
		if(emotionType == EmotionType.UNK)
			log.error("Unknown emotion type? 0x" + Integer.toHexString(emotionTypeId).toUpperCase());
		
		Player activePlayer = getConnection().getActivePlayer();
		if(activePlayer == null) return;
		
		Summon summon = activePlayer.getSummon();
		if (summon == null) return;
			
		switch(emotionType)
		{
			case FLY:
			case LAND:
				PacketSendUtility.broadcastPacket(summon, new SM_EMOTION(summon, emotionType));
				break;
			case ATTACKMODE: //start attacking
				summon.setState(CreatureState.WEAPON_EQUIPPED);
				PacketSendUtility.broadcastPacket(summon, new SM_EMOTION(summon, emotionType));
				break;
			case NEUTRALMODE: //stop attacking
				summon.unsetState(CreatureState.WEAPON_EQUIPPED);
				PacketSendUtility.broadcastPacket(summon, new SM_EMOTION(summon, emotionType));
				break;
		}	
	}
}
