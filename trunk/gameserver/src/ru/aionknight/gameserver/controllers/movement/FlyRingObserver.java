/*
 *  This file is part of Zetta-Core Engine <http://www.zetta-core.org>.
 *
 *  Zetta-Core is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License,
 *  or (at your option) any later version.
 *
 *  Zetta-Core is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a  copy  of the GNU General Public License
 *  along with Zetta-Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package ru.aionknight.gameserver.controllers.movement;

import java.util.Random;


import ru.aionknight.gameserver.configs.administration.AdminConfig;
import ru.aionknight.gameserver.dataholders.DataManager;
import ru.aionknight.gameserver.model.flyring.FlyRing;
import ru.aionknight.gameserver.model.gameobjects.player.Player;
import ru.aionknight.gameserver.model.utils3d.Point3D;
import ru.aionknight.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import ru.aionknight.gameserver.quest.HandlerResult;
import ru.aionknight.gameserver.quest.QuestEngine;
import ru.aionknight.gameserver.quest.model.QuestCookie;
import ru.aionknight.gameserver.skill.effect.EffectTemplate;
import ru.aionknight.gameserver.skill.effect.Effects;
import ru.aionknight.gameserver.skill.effect.FpHealEffect;
import ru.aionknight.gameserver.skill.model.Effect;
import ru.aionknight.gameserver.skill.model.SkillTemplate;
import ru.aionknight.gameserver.utils.MathUtil;
import ru.aionknight.gameserver.utils.PacketSendUtility;


/**
 * @author blakawk
 * 
 */
public class FlyRingObserver extends ActionObserver
{
	private Player	player;
	private FlyRing	ring;
	private Point3D	oldPosition;
	private Random	random;

	public FlyRingObserver()
	{
		super(ObserverType.MOVE);
		this.player = null;
		this.ring = null;
		this.oldPosition = null;
		this.random = null;
	}

	public FlyRingObserver(FlyRing ring, Player player)
	{
		super(ObserverType.MOVE);
		this.player = player;
		this.ring = ring;
		this.oldPosition = new Point3D(player.getX(), player.getY(), player.getZ());
		this.random = new Random();
	}

	@Override
	public void moved()
	{
		Point3D newPosition = new Point3D(player.getX(), player.getY(), player.getZ());
		boolean passedThrough = false;

		if(ring.getPlane().intersect(oldPosition, newPosition))
		{
			Point3D intersectionPoint = ring.getPlane().intersection(oldPosition, newPosition);
			if(intersectionPoint != null)
			{
				double distance = Math.abs(ring.getPlane().getCenter().distance(intersectionPoint));

				if(distance < ring.getTemplate().getRadius())
				{
					passedThrough = true;
				}
			}
			else
			{
				if(MathUtil.isIn3dRange(ring, player, ring.getTemplate().getRadius()))
					;
				{
					passedThrough = true;
				}
			}
		}

		if(passedThrough)
		{
			if(player.getAccessLevel() >= AdminConfig.COMMAND_RING)
			{
				PacketSendUtility.sendMessage(player, "You passed through fly ring " + ring.getName());
			}

			oldPosition = newPosition;

			HandlerResult result = QuestEngine.getInstance()
				.onFlyThroughRing(new QuestCookie(null, player, 0, 0), ring);
			if(result != HandlerResult.UNKNOWN)
			{
				if(result == HandlerResult.SUCCESS)
				{
					SkillTemplate template = DataManager.SKILL_DATA.getSkillTemplate(1861);
					if(template != null)
					{
						int fpBonus = 0;
						Effects effects = template.getEffects();
						for(EffectTemplate effect : effects.getEffects())
						{
							if(effect instanceof FpHealEffect)
							{
								fpBonus = ((FpHealEffect) effect).getValue();
								break;
							}
						}
						if(player.getLifeStats().getCurrentFp() + fpBonus > player.getLifeStats().getMaxFp())
							fpBonus = player.getLifeStats().getMaxFp() - player.getLifeStats().getCurrentFp();

						if(fpBonus > 0)
							player.getLifeStats().increaseFp(TYPE.FP_RINGS, fpBonus);
						
						Effect e = new Effect(player, player, template, template.getLvl(), template
							.getEffectsDuration());
						e.initialize();
						e.applyEffect();
					}
				}
				return;
			}

			int fpBonus = 7 + random.nextInt(7);
			if(player.getLifeStats().getCurrentFp() + fpBonus > player.getLifeStats().getMaxFp())
			{
				fpBonus = player.getLifeStats().getMaxFp() - player.getLifeStats().getCurrentFp();
			}
			if(fpBonus > 0)
			{
				player.getLifeStats().increaseFp(TYPE.FP_RINGS, fpBonus);
			}
		}
	}
}
