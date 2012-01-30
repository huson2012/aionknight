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

package gameserver.skill.properties;

import gameserver.model.gameobjects.Creature;
import gameserver.skill.effect.EffectId;
import gameserver.skill.model.Skill;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.EnumSet;
import java.util.regex.Pattern;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TargetStatusProperty")
public class TargetStatusProperty extends Property
{

    private static final Pattern COMPILE = Pattern.compile(" ");
    @XmlAttribute(name = "value", required = true)
	protected String stateSet;
	
	@XmlTransient
	protected EnumSet<EffectId> value = EnumSet.noneOf(EffectId.class);
	
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		if (stateSet == null || stateSet.isEmpty())
			return;

		String[] states = COMPILE.split(stateSet);
		for (String state : states)
			value.add(EffectId.valueOf(state));

		stateSet = null;
	}
	
	@Override
	public boolean set(Skill skill)
	{
		if (skill.getEffectedList().size() != 1)
			return false;
		
		Creature effected = skill.getEffectedList().first().getCreature();
		boolean result = false;
		
		for (EffectId as : value)
		{
			if (effected.getEffectController().isAbnormalSet(as))
				result = true;
		}
		
		return result;
	}
}
