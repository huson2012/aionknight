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

package gameserver.ai.desires;

import gameserver.ai.desires.impl.WalkDesire;
import java.util.Iterator;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * This class represents desire queue, it's thread-safe. Desires can be added and removed. If desire is added - previous
 * desires will be checked, if same desire found then desire previous one will be removed from the queue
 *
 * @see gameserver.ai.desires.Desire
 * @see gameserver.ai.desires.AbstractDesire
 */
public class DesireQueue extends PriorityBlockingQueue<Desire>
{
	private static final long	serialVersionUID	= 4903258139534394909L;


	/**
	 * Adds desire to the queue.
	 * <p/>
	 * <p/>
	 * When adding object this method checks first for the same object by {@link AbstractDesire#equals(Object)}, if such
	 * object found, next actions will be done:<br>
	 * <br>
	 * 1). Remove old desire instance from the list.<br>
	 * 2). Check if those desires are same instances by "==".<br>
	 * 3). If they are not the same instances, add desire power from old instance to new instance, if they are - do
	 * nothing.<br>
	 * <br>
	 * After all add new desire instance to the list.
	 * 
	 * @param desire
	 *           desire instance to add
	 */
	@Override
	public boolean add(Desire desire)
	{
		// Iterate over the list to find similiar desires
		Iterator<Desire> iterator = super.iterator();
		while(iterator.hasNext())
		{
			Desire iterated = iterator.next();

			// Find similiar desires by #equals method, they can be different instances.
			if(desire.equals(iterated))
			{
				// Remove the old desire from the list
				//iterator.remove();
				super.remove(desire);

				// If current desire instance was not at the list - increase it's power
				// by the value of another instance power
				// and after that add it to the list
				if(desire != iterated)
					desire.increaseDesirePower(iterated.getDesirePower());

				// Break iteration, desire list can't contain two same desires
				break;
			}
		}
	
		return super.add(desire);
	}
	
	/**
	 * Iterates over desires, you have to provide iteration handler and optionally filters.<br>
	 * <br>
	 * Handlers and filters can't call following methods:
	 * <ul>
	 * <li>{@link #addDesire(Desire)}</li>
	 * <li>{@link #poll()}</li>
	 * <li>{@link #removeDesire(Desire)}</li>
	 * </ul>
	 * <p/>
	 * However, method {@link #clear() can be called}.
	 * 
	 * @param handler
	 *           DesireIterationhandler that will be called on the iteration
	 * @param filters
	 *           optional filters that will prevent passing unneede desires to the handler
	 * @throws java.util.ConcurrentModificationException
	 *            only if called handler or filter modified this queue
	 * @see gameserver.ai.desires.DesireIteratorFilter
	 * @see gameserver.ai.desires.DesireIteratorFilter#isOk(Desire)
	 * @see gameserver.ai.desires.DesireIteratorHandler
	 * @see gameserver.ai.desires.DesireIteratorHandler#next(Desire , java.util.Iterator)
	 */
	public void iterateDesires(DesireIteratorHandler handler, DesireIteratorFilter... filters)
	{
		Iterator<Desire> iterator = super.iterator();
		outer: while(iterator.hasNext())
		{
			Desire desire = iterator.next();

			if(filters != null && filters.length > 0)
			{
				for(DesireIteratorFilter filter : filters)
				{
					if(!filter.isOk(desire))
					{
						continue outer;
					}
				}
			}

			handler.next(desire, iterator);
		}
	}

	@Override
	public void clear()
	{
		Desire desire = null;
		while((desire = super.poll()) != null)
			desire.onClear();
	}

	public boolean hasWalkingDesire() 
	{
		Iterator<Desire> iterator = super.iterator();
		while(iterator.hasNext())
		{
			Desire iterated = iterator.next();
			if(iterated instanceof WalkDesire)
				return true;
		}
		return false;
	}
}