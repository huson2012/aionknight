/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a  copy  of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */
package gameserver.skill.effect;

import gameserver.model.DescriptionId;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.skill.action.DamageType;
import gameserver.skill.model.Effect;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;



/**
 * @author kecimis
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProcAtkInstantEffect")
public class ProcAtkInstantEffect extends DamageEffect
{
	@Override
	public void applyEffect(final Effect effect)
	{
		
		final boolean isGodstone = (effect.getItemTemplate() != null && effect.getItemTemplate().getGodstoneInfo() != null);
		ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				if (effect.getEffector() instanceof Player && !isGodstone)
					PacketSendUtility.sendPacket((Player)effect.getEffector(), new SM_SYSTEM_MESSAGE(1301062, new DescriptionId(effect.getSkillTemplate().getNameId())));
				//TODO figure out logId
				effect.getEffected().getController().onAttack(effect.getEffector(), effect.getSkillId(), TYPE.HP, effect.getReserved1(), 0,effect.getAttackStatus(), false, true);
			}
		}, 800);
	}

	@Override
	public void calculate(Effect effect)
	{
		super.calculate(effect, DamageType.MAGICAL, true, false);
	}
}
