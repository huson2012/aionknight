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

package gameserver.skill.task;

import commons.utils.Rnd;
import gameserver.configs.main.CustomConfig;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;

public abstract class AbstractCraftTask extends AbstractInteractionTask
{
	protected int maxValue = 100;
	protected int currentSuccessValue = 0;
	protected int currentFailureValue = 0;
	protected int skillLvlDiff;
	protected boolean speedUp;
	
	/**
	 * 
	 * @param requestor
	 * @param responder
	 * @param skillLvlDiff
	 */
	public AbstractCraftTask(Player requestor, VisibleObject responder, int skillLvlDiff)
	{
		super(requestor, responder, skillLvlDiff);
		this.skillLvlDiff = skillLvlDiff;
	}

	@Override
	protected boolean onInteraction()
	{
		if(currentSuccessValue == maxValue)
		{
			return onSuccessFinish();
		}
		if(currentFailureValue == maxValue)
		{
			onFailureFinish();
			return true;
		}
		
		analyzeInteraction();
		
		sendInteractionUpdate();
		return false;
	}
	
	/**
	 * Perform interaction calculation
	 */
	private void analyzeInteraction()
	{
		speedUp = false;
		int multi = Math.max(0, CustomConfig.REGULAR_CRAFTING_SUCCESS-skillLvlDiff*5);
		if(skillLvlDiff == 99999)
		{
			currentSuccessValue = maxValue;
			return;
		}
		
		speedUp = Rnd.get(100) <= CustomConfig.CRAFTING_SPEEDUP;
		
		if(speedUp)
			currentSuccessValue += Rnd.get(maxValue/2,maxValue);
		
		if(Rnd.get(100) > multi)
			currentSuccessValue += Rnd.get(maxValue/(multi+1)/2,maxValue);
		else
			currentFailureValue += Rnd.get(maxValue/(multi+1)/2,maxValue);
		
		if(currentSuccessValue >= maxValue)
			currentSuccessValue = maxValue;
		else if(currentFailureValue >= maxValue)
			currentFailureValue = maxValue;
	}
	
	protected abstract void sendInteractionUpdate();
	
	protected abstract boolean onSuccessFinish();
	
	protected abstract void onFailureFinish();
}