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

package gameserver.ai.desires.impl;

import gameserver.ai.AI;
import gameserver.ai.desires.AbstractDesire;
import gameserver.ai.events.Event;
import gameserver.model.gameobjects.Creature;
import gameserver.model.siege.FortressGeneral;
import gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;

public class RestoreHealthDesire extends AbstractDesire
{	
	private Creature owner;
	private int restoreHpValue;
	private int restoreMpValue;

	public RestoreHealthDesire(Creature owner, int desirePower)
	{
		super(desirePower);
		this.owner = owner;
		restoreHpValue = owner.getLifeStats().getMaxHp() / 5;
		restoreMpValue = owner.getLifeStats().getMaxMp() / 5;
	}
	
	@Override
	public boolean handleDesire(AI<?> ai)
	{
		if(owner == null || owner.getLifeStats().isAlreadyDead() || owner instanceof FortressGeneral)
			return false;
		
		if(!owner.isInCombat())
		{
			owner.getLifeStats().increaseHp(TYPE.NATURAL_HP, restoreHpValue);
			owner.getLifeStats().increaseMp(TYPE.NATURAL_MP, restoreMpValue);
		}

		if(owner.getLifeStats().isFullyRestoredHpMp())
		{
			ai.handleEvent(Event.RESTORED_HEALTH);
			return false;
		}
		return true;
	}


	@Override
	public int getExecutionInterval()
	{
		return 1;
	}

	@Override
	public void onClear()
	{
		// TODO Auto-generated method stub
	}
}