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
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.skill.model.SkillTemplate;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SM_SKILL_COOLDOWN extends AionServerPacket
{
	private Map<Integer, Long> cooldowns;
	
	public SM_SKILL_COOLDOWN(Map<Integer, Long> cooldowns)
	{
		this.cooldowns = cooldowns;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		long currentTime = System.currentTimeMillis();
		Map<Integer, Integer> cooldowns = new HashMap<Integer, Integer>();
		for(Map.Entry<Integer, Long> entry : this.cooldowns.entrySet())
		{
			List<SkillTemplate> sts = DataManager.SKILL_DATA.getSkillTemplatesForDelayId(entry.getKey());
			int left = Math.round((entry.getValue() - currentTime) / 1000);
			for (SkillTemplate st : sts)
			{
				cooldowns.put(st.getSkillId(), left > 0 ? left : 0);
			}
		}
		
		writeH(buf, cooldowns.size());
		for(Map.Entry<Integer, Integer> entry : cooldowns.entrySet())
		{
			writeH(buf, entry.getKey());
			writeD(buf, entry.getValue());
		}
	}
}