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

package gameserver.skill.effect.modifier;

import gameserver.model.Race;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.skill.model.Effect;
import gameserver.skill.model.SkillTargetRace;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TargetRaceDamageModifier")
public class TargetRaceDamageModifier extends ActionModifier
{
	@XmlAttribute(name = "race")
	private SkillTargetRace skillTargetRace;
	
	@Override
	public int analyze(Effect effect)
	{
		Creature effected = effect.getEffected();
		
		if(effected instanceof Player)
		{
			int newValue = (value + effect.getSkillLevel() * delta);
			Player player = (Player) effected;
			switch(skillTargetRace)
			{
				case ASMODIANS:
					if(player.getCommonData().getRace() == Race.ASMODIANS)
						return newValue;
					break;
				case ELYOS:
					if(player.getCommonData().getRace() == Race.ELYOS)
						return newValue;
			}
		}
		else if(effected instanceof Npc)
		{
			int newValue = (value + effect.getSkillLevel() * delta);
			Npc npc = (Npc) effected;
			if (String.valueOf(npc.getObjectTemplate().getRace()) == String.valueOf(skillTargetRace))
				return newValue;
			else
				return 0;
		}

		return 0;	
	}

	@Override
	public boolean check(Effect effect)
	{
		Creature effected = effect.getEffected();
		if(effected instanceof Player)
		{
			
			Player player = (Player) effected;
			Race race =  player.getCommonData().getRace();
			return (race == Race.ASMODIANS && skillTargetRace == SkillTargetRace.ASMODIANS)
				|| (race == Race.ELYOS && skillTargetRace == SkillTargetRace.ELYOS);
		}
		else if(effected instanceof Npc)
		{
			Npc npc = (Npc) effected;
			Race race = npc.getObjectTemplate().getRace();
			
			if (race == null)
				return false;
			
			switch(race)
			{
			case LYCAN:
				return (race == Race.LYCAN && skillTargetRace == SkillTargetRace.LYCAN);
			case CONSTRUCT:
				return (race == Race.CONSTRUCT && skillTargetRace == SkillTargetRace.CONSTRUCT);
			case CARRIER:
				return (race == Race.CARRIER && skillTargetRace == SkillTargetRace.CARRIER);
			case DRAKAN:
				return (race == Race.DRAKAN && skillTargetRace == SkillTargetRace.DRAKAN);
			case LIZARDMAN:
				return (race == Race.LIZARDMAN && skillTargetRace == SkillTargetRace.LIZARDMAN);
			case TELEPORTER:
				return (race == Race.TELEPORTER && skillTargetRace == SkillTargetRace.TELEPORTER);
			case NAGA:
				return (race == Race.NAGA && skillTargetRace == SkillTargetRace.NAGA);
			case BROWNIE:
				return (race == Race.BROWNIE && skillTargetRace == SkillTargetRace.BROWNIE);
			case KRALL:
				return (race == Race.KRALL && skillTargetRace == SkillTargetRace.KRALL);
			case SHULACK:
				return (race == Race.SHULACK && skillTargetRace == SkillTargetRace.SHULACK);
			case BARRIER:
				return (race == Race.BARRIER && skillTargetRace == SkillTargetRace.BARRIER);
			case PC_LIGHT_CASTLE_DOOR:	
				return (race == Race.PC_LIGHT_CASTLE_DOOR && skillTargetRace == SkillTargetRace.PC_LIGHT_CASTLE_DOOR);
			case PC_DARK_CASTLE_DOOR:
				return (race == Race.PC_DARK_CASTLE_DOOR && skillTargetRace == SkillTargetRace.PC_DARK_CASTLE_DOOR);
			case DRAGON_CASTLE_DOOR:
				return (race == Race.DRAGON_CASTLE_DOOR && skillTargetRace == SkillTargetRace.DRAGON_CASTLE_DOOR);
			case GCHIEF_LIGHT:
				return (race == Race.GCHIEF_LIGHT && skillTargetRace == SkillTargetRace.GCHIEF_LIGHT);
			case GCHIEF_DARK:
				return (race == Race.GCHIEF_DARK && skillTargetRace == SkillTargetRace.GCHIEF_DARK);
			case DRAGON:
				return (race == Race.DRAGON && skillTargetRace == SkillTargetRace.DRAGON);
			case OUTSIDER:
				return (race == Race.OUTSIDER && skillTargetRace == SkillTargetRace.OUTSIDER);
			case RATMAN:
				return (race == Race.RATMAN && skillTargetRace == SkillTargetRace.RATMAN);
			case DEMIHUMANOID:	
				return (race == Race.ASMODIANS && skillTargetRace == SkillTargetRace.ASMODIANS);
			case UNDEAD:
				return (race == Race.UNDEAD && skillTargetRace == SkillTargetRace.UNDEAD);
			case BEAST:
				return (race == Race.BEAST && skillTargetRace == SkillTargetRace.BEAST);
			case MAGICALMONSTER:
				return (race == Race.MAGICALMONSTER && skillTargetRace == SkillTargetRace.MAGICALMONSTER);
			case ELEMENTAL:
				return (race == Race.ELEMENTAL && skillTargetRace == SkillTargetRace.ELEMENTAL);
			case PC_ALL:
				return (race == Race.PC_ALL && skillTargetRace == SkillTargetRace.PC_ALL);
			default:
				return false;
			}
			
		}
		return false;
	}

}
