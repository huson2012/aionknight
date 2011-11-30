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

import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.SkillListEntry;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;

import java.nio.ByteBuffer;


/**
 * In this packet Server is sending Skill Info?
 * 
 * @author -Nemesiss-
 * 
 * modified by ATracer,MrPoke
 * 
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