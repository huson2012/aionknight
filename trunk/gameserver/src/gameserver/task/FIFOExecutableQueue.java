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

import gameserver.utils.ThreadPoolManager;

import java.util.concurrent.locks.ReentrantLock;

public abstract class FIFOExecutableQueue implements Runnable
{
	private static final byte NONE = 0;
	private static final byte QUEUED = 1;
	private static final byte RUNNING = 2;
	private final ReentrantLock lock = new ReentrantLock();
	private volatile byte state = NONE;
	protected final void execute()
	{
		lock();
		try
		{
			if (state != NONE)
				return;
			
			state = QUEUED;
		}
		finally
		{
			unlock();
		}
		
		ThreadPoolManager.getInstance().execute(this);
	}
	
	public final void lock()
	{
		lock.lock();
	}
	
	public final void unlock()
	{
		lock.unlock();
	}
	
	public final void run()
	{
		try
		{
			while (!isEmpty())
			{
				setState(QUEUED, RUNNING);
				
				try
				{
					while (!isEmpty())
						removeAndExecuteFirst();
				}
				finally
				{
					setState(RUNNING, QUEUED);
				}
			}
		}
		finally
		{
			setState(QUEUED, NONE);
		}
	}
	
	private void setState(byte expected, byte value)
	{
		lock();
		try
		{
			if (state != expected)
				throw new IllegalStateException("state: " + state + ", expected: " + expected);
		}
		finally
		{
			state = value;
			unlock();
		}
	}
	protected abstract boolean isEmpty();
	protected abstract void removeAndExecuteFirst();
}