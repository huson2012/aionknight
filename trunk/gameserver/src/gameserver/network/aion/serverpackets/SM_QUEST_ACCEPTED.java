/*
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
 *  You should have received a  copy  of the GNU General Public License
 *  along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */
package gameserver.network.aion.serverpackets;

import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.quest.model.QuestStatus;

import java.nio.ByteBuffer;

public class SM_QUEST_ACCEPTED extends AionServerPacket
{
	private int questId;
	private int status;
	private int step;
	private int action;
	private int timer;
	private int sharerId;
	@SuppressWarnings("unused")
	private boolean unk;

 // accept = 1 - get quest 2 - quest steps/hand in 3 - fail/delete 4 - display client timer	
	
	public SM_QUEST_ACCEPTED(int action, int questId, QuestStatus status, int step)
	{
		this.action = action;
		this.questId = questId;
		this.status = status.value();
		this.step = step;
	}
	
	public SM_QUEST_ACCEPTED(int questId)
	{
		this.action = 3;
		this.questId = questId;
		this.status = 0;
		this.step = 0;
	}
	
	public SM_QUEST_ACCEPTED(int action, int questId, int timer)
	{
		this.action = action;
		this.questId = questId;
		this.timer = timer;
		this.step = 0;
	}
	
	public SM_QUEST_ACCEPTED(int questId, int sharerId, boolean unk)
	{
		this.action = 5;
		this.questId = questId;
		this.sharerId = sharerId;
		this.unk = true;
	}
	
	public SM_QUEST_ACCEPTED(int action, int questId)
	{
		this.action = action;
		this.questId = questId;
		this.status = 1;
		this.step = 0;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		switch(action)
		{
			case 1:
				writeC(buf, action);
				writeD(buf, questId);
				writeC(buf, status);
				writeC(buf, 0x0);
				writeD(buf, step);
				writeH(buf, 0);
				break;
			case 2:
				writeC(buf, action);
				writeD(buf, questId);
				writeC(buf, status);
				writeC(buf, 0x0);
				writeD(buf, step);
				writeH(buf, 0x0);
				break;
			case 3:
				writeC(buf, action);
				writeD(buf, questId);
				writeC(buf, status);
				writeC(buf, step);
				break;
			case 4:
				writeC(buf, action);
				writeD(buf, questId);
				writeD(buf, timer);
				writeC(buf, 0x01);
				writeH(buf, 0x0);
				writeC(buf, 0x01);
				break;
			case 5:
				writeC(buf, action);
				writeD(buf, questId);
				writeD(buf, sharerId);
				writeD(buf, 0);
				break;
			case 6:
				writeC(buf, action);
				writeD(buf, questId);
				writeC(buf, status);
				writeC(buf, step);
				writeH(buf, 0x0);
		}
	}
}
