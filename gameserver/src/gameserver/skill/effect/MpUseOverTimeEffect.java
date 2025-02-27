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

import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.stats.StatEnum;
import gameserver.skill.model.Effect;
import gameserver.utils.ThreadPoolManager;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.util.concurrent.Future;


/**

 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MpUseOverTimeEffect")
public class MpUseOverTimeEffect extends AbstractOverTimeEffect
{
	@XmlAttribute
	private int cost_start;
	@XmlAttribute
	private int cost_end;
	
	@Override
	public void startEffect(final Effect effect)
	{
		Creature effected = effect.getEffected();
		int mpUsed = 0;
		if (percent)
		{
			int maxMp = effected.getGameStats().getCurrentStat(StatEnum.MAXMP);
			mpUsed = maxMp * value/100;
		}
		else
			mpUsed = value;

		if (cost_start != 0)
		{
			effected.getLifeStats().reduceMp(cost_start);
		}
			
		
		final int requiredMp = mpUsed;
		Future<?> task = ThreadPoolManager.getInstance().scheduleEffectAtFixedRate(new Runnable(){

			@Override
			public void run()
			{
				onPeriodicAction(effect, requiredMp);
			}
		}, 0, checktime);
		effect.setMpUseTask(task);
	}

	public void onPeriodicAction(Effect effect, int value)
	{
		Creature effected = effect.getEffected();
		if(effected.getLifeStats().getCurrentMp() < value)
			effect.endEffect();

		effected.getLifeStats().reduceMp(value);
	}

	@Override
	public void calculate(Effect effect)
	{
		Creature effected = effect.getEffected();
		
		int mpUsed = 0;
		if (percent)
		{
			int maxMp = effected.getGameStats().getCurrentStat(StatEnum.MAXMP);
			mpUsed = maxMp * value/100;
		}
		else//changempuseconsumptioneffect
			mpUsed = value;

		if(effected.getLifeStats().getCurrentMp() < mpUsed)
			return;
		if (checktime > 0)
			super.calculate(effect);
	}
	@Override
	public void endEffect(Effect effect)
	{
		if (cost_end != 0)
		{
			effect.getEffected().getLifeStats().reduceMp(cost_end);
		}
		super.endEffect(effect);
	}

}
