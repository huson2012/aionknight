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

package gameserver.model;

public class Announcement
{
	private	int id;
	private String faction;
	private String announce;
	private String chatType;
	private	int	delay;

	public Announcement(String announce, String faction, String chatType, int delay)
	{
		this.announce	= announce;
		
		if (!faction.equalsIgnoreCase("ELYOS") && !faction.equalsIgnoreCase("ASMODIANS"))
			faction = "ALL";
			
		this.faction = faction;
		this.chatType = chatType;
		this.delay = delay;
	}
	
	public Announcement(int id, String announce, String faction, String chatType, int delay)
	{
		this.id			= id;
		this.announce	= announce;
		
		if (!faction.equalsIgnoreCase("ELYOS") && !faction.equalsIgnoreCase("ASMODIANS"))
			faction = "ALL";
		
		this.faction	= faction;
		this.chatType	= chatType;
		this.delay		= delay;
	}
	
	public int getId()
	{
		if (id != 0)
			return id;
		else
			return -1;
	}

	public String getAnnounce()
	{
		return announce;
	}

	public String getFaction()
	{
		return faction;
	}

	public Race getFactionEnum()
	{
		if (faction.equalsIgnoreCase("ELYOS"))
			return Race.ELYOS;
		else if (faction.equalsIgnoreCase("ASMODIANS"))
			return Race.ASMODIANS;
		
		return null;
	}

	public String getType()
	{
		return chatType;
	}

	public ChatType getChatType()
	{
		if (chatType.equalsIgnoreCase("NORMAL"))
			return ChatType.PERIOD_NOTICE;
		else if (chatType.equalsIgnoreCase("YELLOW"))
			return ChatType.ANNOUNCEMENTS;
		else if (chatType.equalsIgnoreCase("SHOUT"))
			return ChatType.SHOUT;
		else if (chatType.equalsIgnoreCase("ORANGE"))
			return ChatType.GROUP_LEADER;
		else
			return ChatType.SYSTEM_NOTICE;
	}

	public int getDelay()
	{
		return delay;
	}
}