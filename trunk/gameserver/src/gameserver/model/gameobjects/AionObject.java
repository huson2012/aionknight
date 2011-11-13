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

package gameserver.model.gameobjects;

public abstract class AionObject
{
	/**
	 * Unique id, for all game objects such as: items, players, monsters.
	 */
	private Integer	objectId;

	public AionObject(Integer objId)
	{
		this.objectId = objId;
	}

	/**
	 * Returns unique ObjectId of AionObject
	 * 
	 * @return Int ObjectId
	 */
	public Integer getObjectId()
	{
		return objectId;
	}
	
	/**
	 * @param objectId
	 */
	public void setObjectId(Integer objectId)
	{
		this.objectId = objectId;
	}

	/**
	 * Returns name of the object.<br>
	 * Unique for players, common for NPCs, items, etc
	 * 
	 * @return name of the object
	 */
	public abstract String getName();
}