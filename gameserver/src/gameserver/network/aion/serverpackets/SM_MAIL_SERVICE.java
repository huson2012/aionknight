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

import gameserver.model.gameobjects.Letter;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.mail.MailMessage;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.MailServicePacket;
import java.nio.ByteBuffer;
import java.util.Collection;

public class SM_MAIL_SERVICE extends MailServicePacket
{
	private int serviceId;
	private Player player;
	private Collection<Letter> letters;
	
	private int mailCount;
	private int unreadCount;
	private boolean hasExpress;
	
	private int mailMessage;
	
	private Letter letter;
	private long time;
	
	private int letterId;
	private int attachmentType;
	
	public SM_MAIL_SERVICE(int mailCount, int unreadCount, boolean hasExpress)
	{
		this.serviceId = 0;
		
		this.mailCount = mailCount;
		this.unreadCount = unreadCount;
		this.hasExpress = hasExpress;
	}
	
	/**
	 * Send mailMessage(ex. Send OK, Mailbox full etc.)
	 * @param mailMessage
	 */
	public SM_MAIL_SERVICE(MailMessage mailMessage)
	{
		this.serviceId = 1;
		this.mailMessage = mailMessage.getId();
	}
	
	/**
	 * Send mailbox info
	 * @param player
	 * @param letters
	 */
	public SM_MAIL_SERVICE(Player player, Collection<Letter> letters)
	{
		this.serviceId = 2;
		this.player = player;
		this.letters = letters;
	}
	
	/**
	 * used when reading letter
	 * @param player
	 * @param letter
	 * @param time
	 */
	public SM_MAIL_SERVICE(Player player, Letter letter, long time)
	{
		this.serviceId = 3;
		this.player = player;
		this.letter = letter;
		this.time = time;
	}
	
	/**
	 * used when getting attached items
	 * @param letterId
	 * @param attachmentType
	 */
	public SM_MAIL_SERVICE(int letterId, int attachmentType)
	{
		this.serviceId = 5;
		this.letterId = letterId;
		this.attachmentType = attachmentType;
	}
	
	/**
	 * used when deleting letter
	 * @param letterId
	 */
	public SM_MAIL_SERVICE(Player player, int letterId)
	{
		this.serviceId = 6;
		this.player = player;
		this.letterId = letterId;
	}
	
	@Override
	public void writeImpl (AionConnection con, ByteBuffer buf)
	{
		switch(serviceId)
		{
			case 0:
				writeMailboxState(buf, mailCount, unreadCount, hasExpress);
				break;
				
			case 1:
				writeMailMessage(buf, mailMessage);
				break;
				
			case 2:
				if(!letters.isEmpty())
					writeLettersList(buf, letters, player);
				else
					writeEmptyLettersList(buf, player);
				break;	
				
			case 3:
				writeLetterRead(buf, player, letter, time);
				break;
				
			case 5:
				writeLetterState(buf, letterId, attachmentType);
				break;
				
			case 6:
				writeLetterDelete(buf, player, letterId);
				break;
		}
	}
}