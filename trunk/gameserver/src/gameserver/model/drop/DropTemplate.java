/**
 * This file is part of Aion-Knight [http://aion-knight.ru]
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

package gameserver.model.drop;

public class DropTemplate
{
	private int mobId;
	private int itemId;
	private int min;
	private int max;
	private float chance;

	public DropTemplate(int mobId, int itemId, int min, int max, float chance) 
	{
		this.mobId = mobId;
		this.itemId = itemId;
		this.min = min;
		this.max = max;
		this.chance = chance;
	}

	public int getMobId()
	{
		return mobId;
	}

	public int getItemId()
	{
		return itemId;
	}

	public int getMin()
	{
		return min;
	}

	public int getMax()
	{
		return max;
	}

	public float getChance()
	{
		return chance;
	}
}