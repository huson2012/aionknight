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

package gameserver.controllers;

import gameserver.controllers.movement.FlyRingObserver;
import gameserver.model.flyring.FlyRing;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

public class FlyRingController extends CreatureController<FlyRing>
{
	FastMap<Player, FlyRingObserver> observed = new FastMap<Player, FlyRingObserver>().shared();
	
	@Override
	public void see(VisibleObject object)
	{
		super.see(object);
		
		if(!(object instanceof Player))
		{
			return;
		}
		
		Player p = (Player)object;
		FlyRingObserver observer = new FlyRingObserver ((FlyRing)getOwner(),p);
		p.getObserveController().addObserver(observer);
		observed.put(p, observer);
		Logger.getLogger(FlyRingController.class).debug(getOwner().getName()+" sees "+p.getName());
	}
	
	@Override
	public void notSee(VisibleObject object, boolean isOutOfRange)
	{
		super.notSee(object,isOutOfRange);
		if (isOutOfRange && object instanceof Player)
		{
			Player p = (Player)object;
			if (observed.containsKey(p))
			{
				FlyRingObserver observer = observed.remove(p);
				observer.moved();
				p.getObserveController().removeObserver(observer);
			}
			Logger.getLogger(FlyRingController.class).debug(getOwner().getName()+" not sees "+p.getName());
		}
	}
}
