/**   
 * Эмулятор игрового сервера Aion 2.7 от команды разработчиков 'Aion-Knight Dev. Team' является 
 * свободным программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного программного 
 * обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой более поздней 
 * версии.
 * 
 * Программа распространяется в надежде, что она будет полезной, но БЕЗ КАКИХ БЫ ТО НИ БЫЛО 
 * ГАРАНТИЙНЫХ ОБЯЗАТЕЛЬСТВ; даже без косвенных  гарантийных  обязательств, связанных с 
 * ПОТРЕБИТЕЛЬСКИМИ СВОЙСТВАМИ и ПРИГОДНОСТЬЮ ДЛЯ ОПРЕДЕЛЕННЫХ ЦЕЛЕЙ. Для подробностей смотрите 
 * Стандартную Общественную Лицензию GNU.
 * 
 * Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе с этой программой. 
 * Если это не так, напишите в Фонд Свободного ПО (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * Веб-cайт разработчиков : http://aion-knight.ru
 * Поддержка клиента игры : Aion 2.7 - 'Арена Смерти' (Иннова) 
 * Версия серверной части : Aion-Knight 2.7 (Beta version)
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