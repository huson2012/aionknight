/**
 * Эмулятор игрового сервера Aion 2.7 от команды разработчиков 'Aion-Knight Dev. Team' является 
 * свободным программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного программного 
 * обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой более поздней 
 * версии.
 * 
 * Программа распространяется в надежде, что она будет полезной, но БЕЗ КАКИХ БЫ ТО НИ БЫЛО 
 * ГАРАНТИЙНЫХ ОБЯЗАТЕЛЬСТВ; даже без косвенных  гарантийных  обязательств, связанных с 
 * ПОТРЕБИТЕЛЬСКИМИ СВОЙСТВАМИ и ПРИГОДНОСТЬЮ ДЛЯ ОПРЕДЕЛЕННЫХ ЦЕЛЕЙ. Для подробностей смотрите 
 * Стандартную Общественную Лицензию GNU.
 * 
 * Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе с этой программой. 
 * Если это не так, напишите в Фонд Свободного ПО (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * Веб-cайт разработчиков : http://aion-knight.ru
 * Поддержка клиента игры : Aion 2.7 - 'Арена Смерти' (Иннова) 
 * Версия серверной части : Aion-Knight 2.7 (Beta version)
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