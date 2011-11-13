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