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
package gameserver.model.templates.stats;

import java.util.TreeSet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import gameserver.model.gameobjects.stats.modifiers.AddModifier;
import gameserver.model.gameobjects.stats.modifiers.MeanModifier;
import gameserver.model.gameobjects.stats.modifiers.RateModifier;
import gameserver.model.gameobjects.stats.modifiers.SetModifier;
import gameserver.model.gameobjects.stats.modifiers.StatModifier;
import gameserver.model.gameobjects.stats.modifiers.SubModifier;


/**
 * @author blakawk
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "modifiers")
public class ModifiersTemplate 
{
	@XmlElements ({
			@XmlElement(name = "sub", type=SubModifier.class),
			@XmlElement(name = "add", type=AddModifier.class),
			@XmlElement(name = "rate", type=RateModifier.class),
			@XmlElement(name = "set", type=SetModifier.class),
			@XmlElement(name = "mean", type=MeanModifier.class)
	})
	private TreeSet<StatModifier> modifiers;
	
	public TreeSet<StatModifier> getModifiers()
	{
		return modifiers;
	}
}
