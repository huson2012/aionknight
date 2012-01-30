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

package gameserver.model.account;

public class CharacterPasskey
{
	private int objectId;
	private int wrongCount = 0;
	private boolean isPass = false;
	private ConnectType	connectType;
	public int getObjectId()
	{
		return objectId;
	}

	public void setObjectId(int objectId)
	{
		this.objectId = objectId;
	}

	public int getWrongCount()
	{
		return wrongCount;
	}

	public void setWrongCount(int count)
	{
		this.wrongCount = count;
	}

	public boolean isPass()
	{
		return isPass;
	}

	public void setIsPass(boolean isPass)
	{
		this.isPass = isPass;
	}

	public ConnectType getConnectType()
	{
		return connectType;
	}

	public void setConnectType(ConnectType connectType)
	{
		this.connectType = connectType;
	}

	public enum ConnectType
	{
		ENTER,
		DELETE
	}
}