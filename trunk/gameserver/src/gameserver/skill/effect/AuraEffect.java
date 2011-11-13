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
package gameserver.skill.effect;

import java.util.concurrent.Future;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


import gameserver.dataholders.DataManager;
import gameserver.model.alliance.PlayerAllianceMember;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_MANTRA_EFFECT;
import gameserver.skill.model.Effect;
import gameserver.skill.model.SkillTemplate;
import gameserver.utils.MathUtil;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;


/**

 * 
 */
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
