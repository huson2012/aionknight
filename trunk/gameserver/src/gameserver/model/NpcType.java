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

public enum NpcType
{
	ATTACKABLE(0), NEUTRAL(2), AGGRESSIVE(8), NON_ATTACKABLE(38), RESURRECT(38), 
	POSTBOX(38), USEITEM(38), PORTAL(38), CHEST(38), ARTIFACT(38), ARTIFACT_PROTECTOR(38);
	
	private int someClientSideId;
	
	private NpcType(int id)
	{
		this.someClientSideId = id;
	}
	
	public int getId()
	{
		return someClientSideId;
	}
}