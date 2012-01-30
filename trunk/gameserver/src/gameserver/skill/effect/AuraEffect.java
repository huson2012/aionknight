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

import gameserver.dataholders.DataManager;
import gameserver.model.alliance.PlayerAllianceMember;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_MANTRA_EFFECT;
import gameserver.skill.model.Effect;
import gameserver.skill.model.SkillTemplate;
import gameserver.utils.MathUtil;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.util.concurrent.Future;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AuraEffect")
public class AuraEffect extends EffectTemplate
{
	@XmlAttribute
	protected int	distance;
	@XmlAttribute(name = "skill_id")
	protected int	skillId;

	@Override
	public void applyEffect(Effect effect)
	{
		effect.addToEffectedController();
	}

	public void onPeriodicAction(Effect effect, SkillTemplate template)
	{
		Player effector = (Player) effect.getEffector();
		float auraRangeRate = effector.getController().getAuraRangeRate();
		
		if (effector.isInAlliance())
		{
			for (PlayerAllianceMember allianceMember : effector.getPlayerAlliance().getMembersForGroup(effector.getObjectId()))
			{
				if (allianceMember.isOnline() && MathUtil.isIn3dRange(effector, allianceMember.getPlayer(), distance * auraRangeRate))
				{
					applyAuraTo(allianceMember.getPlayer(), template);
				}
			}
		}
		else if(effector.isInGroup())
		{
			for(Player member : effector.getPlayerGroup().getMembers())
			{
				if(MathUtil.isIn3dRange(effector, member, distance * auraRangeRate))
				{
					applyAuraTo(member, template);
				}
			}
		}
		else
		{
			applyAuraTo(effector, template);
		}
		
		PacketSendUtility.broadcastPacket(effector, new SM_MANTRA_EFFECT(effector, skillId));
	}
	
	/**
	 * 
	 * @param effector
	 */
	private void applyAuraTo(Player effector, SkillTemplate template)
	{
		Effect e = new Effect(effector, effector, template, template.getLvl(), template.getEffectsDuration());
		e.initialize();
		e.applyEffect();
		e = null;
	}

	@Override
	public void startEffect(final Effect effect)
	{
		Future<?> task = ThreadPoolManager.getInstance().scheduleEffectAtFixedRate(new Runnable(){

			@Override
			public void run()
			{
				onPeriodicAction(effect, DataManager.SKILL_DATA.getSkillTemplate(skillId));
			}
		}, 0, 6500);
		effect.setPeriodicTask(task, position);
	}

	@Override
	public void endEffect(Effect effect)
	{
		// nothing todo
	}
}