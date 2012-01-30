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

import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.SkillListEntry;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

/**
 * In this packet Server is sending Skill Info.
 */
public class SM_SKILL_LIST extends AionServerPacket
{

	private SkillListEntry[] skillList;
	private int messageId;
	private int skillNameId;
	private String skillLvl;
	public static final int YOU_LEARNED_SKILL = 1300050;

	/**
	 * This constructor is used on player entering the world
	 * 
 	 * Constructs new <tt>SM_SKILL_LIST </tt> packet
 	 */

	public SM_SKILL_LIST(Player player)
 	{
		this.skillList = player.getSkillList().getAllSkills();
		this.messageId = 0;
 	}
	
	public SM_SKILL_LIST(SkillListEntry skillListEntry, int messageId)
 	{
		this.skillList = new SkillListEntry[]{skillListEntry};
		this.messageId = messageId;
		this.skillNameId = DataManager.SKILL_DATA.getSkillTemplate(skillListEntry.getSkillId()).getNameId();
		this.skillLvl = String.valueOf(skillListEntry.getSkillLevel());
 	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		final int size = skillList.length;
		writeH(buf, size); //skills list size
		
		if (size > 0)
		{
			for (SkillListEntry entry : skillList)
			{
				writeH(buf, entry.getSkillId());//id
				writeH(buf, entry.getSkillLevel());//lvl
				writeC(buf, 0x00);
				writeC(buf, entry.getExtraLvl());
				writeD(buf, 0); //use time? [s]
				writeC(buf, entry.isStigma() ? 1 : 0); // stigma flag
			}
		}
		writeD(buf, messageId);
		if (messageId != 0);
		{
			writeH(buf, 0x24); //unk
			writeD(buf, skillNameId);
			writeH(buf, 0x00);
			writeS(buf, skillLvl);
		}
	}
}