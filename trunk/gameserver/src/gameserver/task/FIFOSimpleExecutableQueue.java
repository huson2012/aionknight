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

package gameserver.task;

import javolution.util.FastList;
import java.util.Collection;

public abstract class FIFOSimpleExecutableQueue<T> extends FIFOExecutableQueue
{
	private final FastList<T> queue = new FastList<T>();
	public final void execute(T t)
	{
		synchronized (queue)
		{
			queue.addLast(t);
		}
		
		execute();
	}
	
	public final void executeAll(Collection<T> c)
	{
		synchronized (queue)
		{
			queue.addAll(c);
		}
		
		execute();
	}
	
	public final void remove(T t)
	{
		synchronized (queue)
		{
			queue.remove(t);
		}
	}
	
	@Override
	protected final boolean isEmpty()
	{
		synchronized (queue)
		{
			return queue.isEmpty();
		}
	}
	
	protected final T removeFirst()
	{
		synchronized (queue)
		{
			return queue.removeFirst();
		}
	}
	
	@Override
	protected abstract void removeAndExecuteFirst();
}