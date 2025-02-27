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

import gameserver.skill.model.DispelType;
import gameserver.skill.model.Effect;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DispelEffect")
public class DispelEffect extends EffectTemplate
{
	@XmlElement(type = Integer.class)
	protected List<Integer>	effectids;
	@XmlElement
	protected List<String> effecttype;
	@XmlAttribute
	protected DispelType	dispeltype;
	@XmlAttribute
	protected Integer		value;

	@Override
	public void applyEffect(Effect effect)
	{
		if(effect.getEffected() == null || effect.getEffected().getEffectController() == null)
			return;
		
		if(dispeltype == null)
			return;
		
		if(dispeltype == DispelType.EFFECTID && effectids == null)
			return;
		
		if(dispeltype == DispelType.EFFECTTYPE && effecttype == null)
			return;

		switch(dispeltype)
		{
			case EFFECTID:
				if (effectids == null)
					return;
				
				for(Integer effectId : effectids)
				{
					effect.getEffected().getEffectController().removeEffectByEffectId(effectId);
				}
				break;
			case EFFECTTYPE:
				if (effecttype == null)
					return;
				
				for(String type : effecttype)
				{
					EffectId abnormalType = EffectId.getEffectIdByName(type);
					if(abnormalType != null && effect.getEffected().getEffectController().isAbnormalSet(abnormalType))
					{
						for (Effect ef : effect.getEffected().getEffectController().getAbnormalEffects())
						{
							if ((ef.getAbnormals() & abnormalType.getEffectId()) == abnormalType.getEffectId())
								ef.endEffect();
						}
					}
				}
				break;
		}
	}
}
