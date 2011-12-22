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