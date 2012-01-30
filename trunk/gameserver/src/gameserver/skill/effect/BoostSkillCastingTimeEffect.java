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

package gameserver.skill.effect;

import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_STATS_INFO;
import gameserver.skill.model.Effect;
import gameserver.skill.model.SkillSubType;
import gameserver.utils.PacketSendUtility;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BoostSkillCastingTimeEffect")
public class BoostSkillCastingTimeEffect extends BufEffect
{
	@XmlAttribute(required = true)
	protected int percent;
	@XmlAttribute(required = true)
	protected SkillSubType type = SkillSubType.NONE;

    /**
	 * SUMMONTRAP,
	 * SUMMON,
	 * SUMMONHOMING,
	 * HEAL,
	 * ATTACK,
	 * NONE //general, for all skills, example: Boon of Quickness
	 * (non-Javadoc)
	 * @see gameserver.skill.effect.BufEffect#startEffect(gameserver.skill.model.Effect)
	 */
	@Override
	public void startEffect(Effect effect)
	{
		effect.getEffected().getController().addBoostCastingRate(type, percent);

		if (type == SkillSubType.NONE && effect.getEffected() instanceof Player)
			PacketSendUtility.sendPacket((Player)effect.getEffected(), new SM_STATS_INFO((Player)effect.getEffected()));
	}
	
	@Override
	public void endEffect(Effect effect)
	{
		effect.getEffected().getController().removeBoostCastingRate(type, percent);
		
		if (type == SkillSubType.NONE && effect.getEffected() instanceof Player)
			PacketSendUtility.sendPacket((Player)effect.getEffected(), new SM_STATS_INFO((Player)effect.getEffected()));
	}
}