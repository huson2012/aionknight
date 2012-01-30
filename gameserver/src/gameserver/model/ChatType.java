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