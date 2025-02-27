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

package gameserver.model.alliance;

public enum PlayerAllianceEvent
{
	LEAVE(0), LEAVE_TIMEOUT(0), BANNED(0), MOVEMENT(1), DISCONNECTED(3),
	UNK(9), RECONNECT(13), ENTER(13), UPDATE(13), MEMBER_GROUP_CHANGE(13),
	APPOINT_VICE_CAPTAIN(13), DEMOTE_VICE_CAPTAIN(13), APPOINT_CAPTAIN(13);
	
	private int id;
	private PlayerAllianceEvent(int id)
	{
		this.id = id;
	}
	
	public int getId()
	{
		return this.id;
	}
}
