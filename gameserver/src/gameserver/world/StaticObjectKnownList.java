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

package gameserver.world;

import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.modifiers.Executor;
import java.util.ArrayList;
import java.util.List;

public class StaticObjectKnownList extends NpcKnownList
{

	public StaticObjectKnownList(VisibleObject owner)
	{
		super(owner);
	}

	@Override
	protected void findVisibleObjects()
	{
		if(owner == null || !owner.isSpawned())
			return;
		
		final List<VisibleObject> objectsToAdd = new ArrayList<VisibleObject>();
		
		for (MapRegion r : owner.getActiveRegion().getNeighbours())
		{
			r.doOnAllPlayers(new Executor<Player>(){
				@Override
				public boolean run(Player newObject)
				{
					if(newObject == owner || newObject == null)
						return true;
					
					if(!checkObjectInRange(owner, newObject))
						return true;
					
					objectsToAdd.add(newObject);

					return true;
				}
			}, true);
		}
		
		for (VisibleObject object : objectsToAdd)
		{
			owner.getKnownList().storeObject(object);
			object.getKnownList().storeObject(owner);
		}
		
		objectsToAdd.clear();
	}
}