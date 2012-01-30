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
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.state.CreatureState;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.utils.PacketSendUtility;
import org.apache.log4j.Logger;

public class CM_EMOTION extends AionClientPacket
{

	/**
	 * Logger
	 */
	private static final Logger	log	= Logger.getLogger(CM_EMOTION.class);

	/**
	 * Emotion number
	 */
	EmotionType							emotionType;

	/**
	 * Emotion number
	 */
	int							emotion;

	/**
	 *	Coordinates of player
	 */
	float						x;
	float						y;
	float						z;
	byte						heading;

	/**
	 * Constructs new client packet instance.
	 * 
	 * @param opcode
	 */
	public CM_EMOTION(int opcode)
	{
		super(opcode);
	}

	/**
	 * Read data
	 */
	@Override
	protected void readImpl()
	{
		int et;
		et = readC();
		emotionType = EmotionType.getEmotionTypeById(et);
				
		switch(emotionType)
		{
			case SELECT_TARGET:// select target
			case JUMP: // jump
			case SIT: // resting
			case STAND: // end resting
			case LAND_FLYTELEPORT: // fly teleport land
			case FLY: // fly up
			case LAND: // land
			case DIE: // die
			case ATTACKMODE: // get equip weapon
			case NEUTRALMODE: // remove equip weapon
			case END_DUEL: // duel end
			case WALK: // walk on
			case RUN: // walk off
			case SWITCH_DOOR: // static doors
			case POWERSHARD_ON: // powershard on
			case POWERSHARD_OFF: // powershard off
			case ATTACKMODE2: // get equip weapon
			case NEUTRALMODE2: // remove equip weapon
				break;
			case EMOTE:
				emotion = readH();
				break;
			case CHAIR_SIT: // sit on chair
			case CHAIR_UP: // stand on chair
				x = readF();
				y = readF();
				z = readF();
				heading = (byte)readC();
				break;				
			default:
				log.error("Unknown emotion type? 0x" + Integer.toHexString(et/**!!!!!*/).toUpperCase());
				break;
		}
	}

	/**
	 * Send emotion packet
	 */
	@Override
	protected void runImpl()
	{
		Player player = getConnection().getActivePlayer();
		
		if(player == null)
			return;
		
		switch(emotionType)
		{
			case SELECT_TARGET:
				return;
			case SIT:
				player.setState(CreatureState.RESTING);
				break;
			case STAND:
				player.unsetState(CreatureState.RESTING);
				break;
			case CHAIR_SIT:
				player.unsetState(CreatureState.WEAPON_EQUIPPED);
				PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE, emotion, x, y, z, heading,
					player.getTarget() == null ? 0 : player.getTarget().getObjectId()), true);
				player.unsetState(CreatureState.ACTIVE);
				player.setState(CreatureState.CHAIR);
				break;
			case CHAIR_UP:
				player.unsetState(CreatureState.CHAIR);
				player.setState(CreatureState.ACTIVE);
				break;
			case LAND_FLYTELEPORT:
				player.getController().onFlyTeleportEnd();
				break;
			case FLY:
				boolean success = player.getFlyController().startFly();
				if (!success) return;
				break;
			case LAND:
				player.getFlyController().endFly();
				break;
			case ATTACKMODE2:
			case ATTACKMODE:
				player.setState(CreatureState.WEAPON_EQUIPPED);
				break;
			case NEUTRALMODE2:
			case NEUTRALMODE:
				player.unsetState(CreatureState.WEAPON_EQUIPPED);
				break;
			case WALK:
				// cannot toggle walk when you flying or gliding
				if(player.getFlyState() > 0)
					return;
				player.setState(CreatureState.WALKING);
				break;
			case RUN:
				player.unsetState(CreatureState.WALKING);
				break;
			case SWITCH_DOOR:
				// TODO: Store door id in GroupService or InstanceService and broadcast to all members
				break;				
			case POWERSHARD_ON:
				if(!player.getEquipment().isPowerShardEquipped())
				{
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.NO_POWER_SHARD_EQUIPPED());
					return;
				}
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.ACTIVATE_THE_POWER_SHARD());
				player.setState(CreatureState.POWERSHARD);
				break;
			case POWERSHARD_OFF:
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.DEACTIVATE_THE_POWER_SHARD());
				player.unsetState(CreatureState.POWERSHARD);
				break;
			case JUMP:
				player.getController().onJump();
				break;
		}
		// Ghetto code for pet snuggle.
		if(emotion == 0x72 && player.getToyPet() != null){
			PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, emotionType, emotion, x, y, z, heading,
				player.getToyPet().getUid()), true);
		}
		else
		{
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, emotionType, emotion, x, y, z, heading,
			player.getTarget() == null ? 0 : player.getTarget().getObjectId()), true);
		}
	}
}