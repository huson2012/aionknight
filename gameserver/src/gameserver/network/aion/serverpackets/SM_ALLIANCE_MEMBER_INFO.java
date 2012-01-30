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

import gameserver.model.alliance.PlayerAllianceEvent;
import gameserver.model.alliance.PlayerAllianceMember;
import gameserver.model.gameobjects.player.PlayerCommonData;
import gameserver.model.gameobjects.stats.PlayerLifeStats;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.skill.model.Effect;
import gameserver.world.WorldPosition;
import java.nio.ByteBuffer;
import java.util.List;

public class SM_ALLIANCE_MEMBER_INFO extends AionServerPacket
{
	private PlayerAllianceMember member;
	private PlayerAllianceEvent event;
	
	public SM_ALLIANCE_MEMBER_INFO(PlayerAllianceMember member, PlayerAllianceEvent event)
	{
		this.member = member;
		this.event = event;
	}
	
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		PlayerCommonData pcd = member.getCommonData();
		WorldPosition wp = pcd.getPosition();

		if (!member.isOnline())
			event = PlayerAllianceEvent.DISCONNECTED;
		
		writeD(buf, member.getAllianceId());
		writeD(buf, member.getObjectId());
		if (member.isOnline())
		{
			PlayerLifeStats pls = member.getPlayer().getLifeStats();
			writeD(buf, pls.getMaxHp());
			writeD(buf, pls.getCurrentHp());
			writeD(buf, pls.getMaxMp());
			writeD(buf, pls.getCurrentMp());
			writeD(buf, pls.getMaxFp());
			writeD(buf, pls.getCurrentFp());
		}
		else
		{
			writeD(buf, 0);
			writeD(buf, 0);
			writeD(buf, 0);
			writeD(buf, 0);
			writeD(buf, 0);
			writeD(buf, 0);
		}
		writeD(buf, wp.getMapId());
		writeD(buf, wp.getMapId());
		writeF(buf, wp.getX());
		writeF(buf, wp.getY());
		writeF(buf, wp.getZ());
		writeC(buf, pcd.getPlayerClass().getClassId());
		writeC(buf, pcd.getGender().getGenderId());
		writeC(buf, pcd.getLevel());
		writeC(buf, this.event.getId());
		writeH(buf, 0x01);
		writeC(buf, 0x00);
		switch(this.event)
		{
			case LEAVE:
			case LEAVE_TIMEOUT:
			case BANNED:
			case MOVEMENT:
			case DISCONNECTED:
				break;				
			case ENTER:
			case UPDATE:
			case RECONNECT:
			case MEMBER_GROUP_CHANGE:				
			case APPOINT_VICE_CAPTAIN:
			case DEMOTE_VICE_CAPTAIN:
			case APPOINT_CAPTAIN:
				writeS(buf, pcd.getName());
				writeD(buf, 0x00);
                writeD(buf, 0x00);
				
				if (member.isOnline())
				{
					List<Effect> abnormalEffects = member.getPlayer().getEffectController().getAbnormalEffects();
					writeH(buf, abnormalEffects.size());
					for(Effect effect : abnormalEffects)
					{
						writeD(buf, effect.getEffectorId());
						writeH(buf, effect.getSkillId());
						writeC(buf, effect.getSkillLevel());
						writeC(buf, effect.getTargetSlot());
						writeD(buf, effect.getElapsedTime());
					}
				}
				else
				{
					writeH(buf, 0);
				}
				break;
			default:
				break;
		}
	}	
}