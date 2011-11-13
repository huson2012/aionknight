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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gameserver.model.gameobjects.stats.modifiers.StatModifier;


/**
 * @author blakawk
 *
 */
public class StatModifiers
{
	private Map<StatModifierPriority,List<StatModifier>> modifiers;
	
	public StatModifiers()
	{
		modifiers = new HashMap<StatModifierPriority,List<StatModifier>>();
	}
	
	public boolean add(StatModifier modifier)
	{
		if (!modifiers.containsKey(modifier.getPriority()))
		{
			modifiers.put(modifier.getPriority(), new ArrayList<StatModifier>());
		}
		return modifiers.get(modifier.getPriority()).add(modifier);
	}
	
	public List<StatModifier> getModifiers(StatModifierPriority priority)
	{
		if (!modifiers.containsKey(priority))
		{
			modifiers.put(priority, new ArrayList<StatModifier>());
		}
		
		return modifiers.get(priority);
	}
}
