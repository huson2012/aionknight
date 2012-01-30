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

import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.PlayerCommonData;
import gameserver.model.gameobjects.stats.PlayerLifeStats;
import gameserver.model.group.GroupEvent;
import gameserver.model.group.PlayerGroup;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.skill.model.Effect;
import gameserver.world.WorldPosition;
import java.nio.ByteBuffer;
import java.util.List;

public class SM_GROUP_MEMBER_INFO extends AionServerPacket
{
	private PlayerGroup group;
	private Player player;
	private GroupEvent event;
	
	public SM_GROUP_MEMBER_INFO(PlayerGroup group, Player player, GroupEvent event)
	{
		this.group = group;
		this.player = player;
		this.event = event;
	}
	
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{		
		PlayerLifeStats pls = player.getLifeStats();
		PlayerCommonData pcd = player.getCommonData();
		WorldPosition wp = pcd.getPosition();
		
		writeD(buf, group.getGroupId());
		writeD(buf, player.getObjectId());
		writeD(buf, pls.getMaxHp());
		writeD(buf, pls.getCurrentHp());
		writeD(buf, pls.getMaxMp());
		writeD(buf, pls.getCurrentMp());
		writeD(buf, pls.getMaxFp()); //maxflighttime
		writeD(buf, pls.getCurrentFp()); //currentflighttime
		writeD(buf, wp.getMapId());
		writeD(buf, wp.getMapId());
		writeF(buf, wp.getX());
		writeF(buf, wp.getY());
		writeF(buf, wp.getZ());
		writeC(buf, pcd.getPlayerClass().getClassId()); //class id
		writeC(buf, pcd.getGender().getGenderId()); //gender id
		writeC(buf, pcd.getLevel()); //level
		writeC(buf, this.event.getId()); //something events
		writeH(buf, 0x01); //channel
        writeC(buf, 0);
	  	if (this.event == GroupEvent.MOVEMENT)
		{
			return;
		}
		writeS(buf, pcd.getName()); //name
		writeH(buf, 0x00); //unk
		writeH(buf, 0x00); //unk
		List<Effect> abnormalEffects = player.getEffectController().getAbnormalEffects();
		writeH(buf, abnormalEffects.size()); //Abnormal effects
		for(Effect effect : abnormalEffects)
		{
			writeD(buf, effect.getEffectorId()); //casterid
			writeH(buf, effect.getSkillId()); //spellid
			writeC(buf, effect.getSkillLevel()); //spell level
			writeC(buf, effect.getTargetSlot()); //unk ?
			writeD(buf, effect.getElapsedTime()); //estimatedtime
		}
        writeD(buf, 0);
		writeD(buf, 0x25F7); //unk 9719
	}
}