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

package gameserver.model.templates.mail;

public enum MailMessage
{
	MAIL_SEND_SECCESS(0),
	NO_SUCH_CHARACTER_NAME(1),
	RECIPIENT_MAILBOX_FULL(2),
	MAIL_IS_ONE_RACE_ONLY(3),
	YOU_ARE_IN_RECIPIENT_IGNORE_LIST(4),
	RECIPIENT_IGNORING_MAIL_FROM_PLAYERS_LOWER_206_LVL(5), //WTF??
	MAILSPAM_WAIT_FOR_SOME_TIME(6);
	
	private int id;
	
	private MailMessage(int id)
	{
		this.id = id;
	}
	
	public int getId()
	{
		return id;
	}
}
