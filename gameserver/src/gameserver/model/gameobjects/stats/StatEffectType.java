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

package gameserver.model.gameobjects.stats;

public enum StatEffectType
{
	ITEM_EFFECT(1), ENCHANT_EFFECT(2), STONE_EFFECT(3),
	ITEM_SET_EFFECT(4), TITLE_EFFECT(5), SKILL_EFFECT(6);
	
	private int value;
	
	private StatEffectType(int value)
	{
		this.value = value;
	}
	
	public int getValue()
	{
		return value;
	}
}