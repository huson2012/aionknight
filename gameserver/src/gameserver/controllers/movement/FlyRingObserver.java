/**   
 * �������� �������� ������� Aion 2.7 �� ������� ������������� 'Aion-Knight Dev. Team' �������� 
 * ��������� ����������� ������������; �� ������ �������������� �/��� �������� ��� �������� �������� 
 * ����������� ������������ �������� GNU (GNU GPL), �������������� ������ ���������� ������������ 
 * ����������� (FSF), ���� �������� ������ 3, ���� (�� ���� ����������) ����� ����� ������� 
 * ������.
 * 
 * ��������� ���������������� � �������, ��� ��� ����� ��������, �� ��� ����� �� �� �� ���� 
 * ����������� ������������; ���� ��� ���������  �����������  ������������, ��������� � 
 * ���������������� ���������� � ������������ ��� ������������ �����. ��� ������������ �������� 
 * ����������� ������������ �������� GNU.
 * 
 * �� ������ ���� �������� ����� ����������� ������������ �������� GNU ������ � ���� ����������. 
 * ���� ��� �� ���, �������� � ���� ���������� �� (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * ���-c��� ������������� : http://aion-knight.ru
 * ��������� ������� ���� : Aion 2.7 - '����� ������' (������) 
 * ������ ��������� ����� : Aion-Knight 2.7 (Beta version)
 */

package gameserver.controllers.movement;

import gameserver.configs.administration.AdminConfig;
import gameserver.dataholders.DataManager;
import gameserver.model.flyring.FlyRing;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.utils3d.Point3D;
import gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import gameserver.quest.HandlerResult;
import gameserver.quest.QuestEngine;
import gameserver.quest.model.QuestCookie;
import gameserver.skill.effect.EffectTemplate;
import gameserver.skill.effect.Effects;
import gameserver.skill.effect.FpHealEffect;
import gameserver.skill.model.Effect;
import gameserver.skill.model.SkillTemplate;
import gameserver.utils.MathUtil;
import gameserver.utils.PacketSendUtility;
import java.util.Random;

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