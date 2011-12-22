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

import gameserver.model.templates.item.ItemTemplate;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_CRAFT_UPDATE extends AionServerPacket
{
	private int skillId;
	private int itemId;
	private int action;
	private int success;
	private int failure;
	private int nameId;
	private int delay;

	/**
	 * @param skillId
	 * @param item
	 * @param success
	 * @param failure
	 * @param action
	 */
	public SM_CRAFT_UPDATE(int skillId, ItemTemplate item, int success, int failure, int action)
	{
		this.action = action;
		this.skillId = skillId;
		this.itemId = item.getTemplateId();
		this.success = success;
		this.failure = failure;
		this.nameId = item.getNameId();
		if(skillId == 40009)
			this.delay = 1500;
		else
			this.delay = 700;
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeH(buf, skillId);
		writeC(buf, action);
		writeD(buf, itemId);

		switch(action)
		{
			case 0: // Инициализация
				writeD(buf, success);
				writeD(buf, failure);
				writeD(buf, 0);
				writeD(buf, 1200);		// Задержки, после чего бар начнет двигаться (мс)
				writeD(buf, 1330048);	// Сообщение о начале крафта
				writeH(buf, 0x24);
				writeD(buf, nameId);	// item nameId to display it's name in system message above
				writeH(buf, 0);
				break;
			case 1: // Регулярные обновления
			case 2: // Скорость обновление
				writeD(buf, success);
				writeD(buf, failure);
				writeD(buf, delay);	// time of moving execution (ms)
				writeD(buf, 1200);	// delay after which bar will start moving (ms)
				writeD(buf, 0);
				writeH(buf, 0);
				break;
			case 3: //crit
				writeD(buf, success);
				writeD(buf, failure);
				writeD(buf, 0);
				writeD(buf, 0);
				writeD(buf, 0);
				writeH(buf, 0);
				break;
			case 4:	// Отмена крафта
				writeD(buf, success);
				writeD(buf, failure);
				writeD(buf, 0);
				writeD(buf, 0);
				writeD(buf, 1330051);	// Сообщение об отмене крафта
				writeH(buf, 0);
				break;
			case 5: // Успешный крафт
				writeD(buf, success);
				writeD(buf, failure);
				writeD(buf, 0);
				writeD(buf, 0);	
				writeD(buf, 1300788);	// Сообщение об успешном крафте
				writeH(buf, 0x24);
				writeD(buf, nameId);	//item nameId to display it's name in system message above
				writeH(buf, 0);
				break;
			case 6: // Неудача при крафте
				writeD(buf, success);
				writeD(buf, failure);
				writeD(buf, 0);
				writeD(buf, 0);	
				writeD(buf, 1330050);	// Сообщение о неудачном крафте
				writeH(buf, 0x24);
				writeD(buf, nameId);	//item nameId to display it's name in system message above
				writeH(buf, 0);
				break;
		}
	}
}