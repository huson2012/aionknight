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

package gameserver.model;

import gameserver.configs.main.GSConfig;

public enum ChatType
{


	NORMAL(0x00), SHOUT(0x03), WHISPER(0x04), GROUP(0x05), ALLIANCE(0x06), GROUP_LEADER(0x07),
	LEGION(0x08), LEGION_2(0x0A), SHOUT_2(0x0C), ANNOUNCEMENTS(0x19, true),	PERIOD_NOTICE(0x20, true),
	PERIOD_ANNOUNCEMENTS(0x1C, true), SYSTEM_NOTICE(0x21, true), SYSTEM_NOTICE_2(0x22, true);

	private final int intValue;
	private boolean	sysMsg;
	private ChatType(int intValue)
	{
		this(intValue, false);
	}

	public int toInteger()
	{
		return intValue;
	}

	public static ChatType getChatTypeByInt(int integerValue) throws IllegalArgumentException
	{
		for(ChatType ct : ChatType.values())
		{
			if(ct.toInteger() == integerValue)
			{
				return ct;
			}
		}

		throw new IllegalArgumentException("Unsupported chat type: " + integerValue);
	}

	private ChatType(int intValue, boolean sysMsg)
	{
		if(GSConfig.SERVER_VERSION.startsWith("2."))
			if(intValue == 0x08)
				intValue = 0x0A;
			else if(intValue == 0x21)
					intValue = 0x22;

		this.intValue = intValue;
		this.sysMsg = sysMsg;
	}

	public boolean isSysMsg()
	{
		return sysMsg;
	}
}