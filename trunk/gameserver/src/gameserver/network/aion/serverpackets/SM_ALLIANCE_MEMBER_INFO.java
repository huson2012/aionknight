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