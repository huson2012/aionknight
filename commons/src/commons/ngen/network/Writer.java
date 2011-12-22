/**
 * Игровой эмулятор от команды разработчиков 'Aion-Knight Dev. Team' является свободным 
 * программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного 
 * программного обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой 
 * более поздней версии.
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

package commons.ngen.network;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

public class Writer extends Processor
{
	private int maxTries; 
	private List<Connection> queue = new ArrayList<Connection> ();
	private int numberOfConnections = 0;
	private SocketChannel sc = null;
	private boolean writeResult = true;
	private int writed = 0, total = 0;
	private int tries = 0;
	private Selector selector;
	private int selection;
	private long lostTimeInRetries = 0;
	private long before = 0;
	private boolean debugEnabled = false;

	public Writer(String name, int writeTries, boolean debugEnabled) throws IOException
	{
		super(name, false);
		this.maxTries = writeTries;
		this.selector = Selector.open();
		this.debugEnabled = debugEnabled;
	}

	public void wakeup (Connection c)
	{
		synchronized (queue)
		{
			queue.add(c);
			
			if (queue.size() == 1)
				queue.notify();
		}
	}

	@Override
	public void manage (Connection c)
	{
		c.setWriter(this);
		numberOfConnections ++;
	}

	@Override
	public int getNumberOfConnections ()
	{
		return numberOfConnections;
	}

	public void remove ()
	{
		numberOfConnections--;
	}
	
	@Override
	public void close ()
	{
		synchronized (queue)
		{
			super.close();
			
			if (queue.size() > 0)
			{
				queue.clear();
			}
			
			queue.notify();
		}
	}

	@Override
	public void run ()
	{
		Connection c = null;
		
		imRunning();

		while (running())
		{
			synchronized (queue) {
				while (queue.size() == 0 && running())
				{
					imIdle();
					try { queue.wait(); } catch (InterruptedException e) { }  
				}

				if (!running())
					break;
			
				imBusy();
				c = queue.remove(0);
			}

			if (c != null)
			{
				try {
					if (!write(c))
					{
						synchronized (queue)
						{
							queue.add(c);
						}
					}
				} catch (Exception e) {
					if (debugEnabled)
						log.error(e.getClass().getSimpleName()+" while writing for connection "+c, e);
					c.close(false);
				}

				c = null;
			}
		}
		
		if (debugEnabled)
			log.debug(getName()+" stopped");
	}
	
	private boolean write (Connection c) throws IOException
	{
		writeResult = true;
		sc = c.channel();
		buffer.clear();
		
		if (sc == null || !sc.isOpen() || c.isWriteDisabled())
		{
			c.close(false);
			return true;
		}
		
		try {
			writeResult = c.writeData(buffer);
		} catch (Exception e) {
			log.error(e.getClass().getSimpleName()+" writing data to buffer", e);
			return true;
		}
		
		if (debugEnabled)
			log.debug("Written data to buffer "+buffer);
		
		try {
			tries = total = 0;
			
			do
			{
				synchronized (sc)
				{
					total += writed = sc.write(buffer);
				}
				
				if (writed == 0)
				{
					before = System.currentTimeMillis();
					
					tries++;
					if (sc.keyFor(selector) == null) {
						sc.register(selector, SelectionKey.OP_WRITE);
					} else {
						sc.keyFor(selector).interestOps(SelectionKey.OP_WRITE);
					}
					selection = selector.select();
					if (selection > 0)
					{
						selector.selectedKeys().clear();
					}
				}
			} while (buffer.hasRemaining() && tries < maxTries);
			
			if (sc.keyFor(selector) != null)
			{
				sc.keyFor(selector).interestOps(0);
			}
			
			if (tries == maxTries)
			{
				if (debugEnabled)
					log.error("Too much write tries ("+maxTries+") without any bytes written (written: "+total+", remaining: "+buffer.remaining()+") for connection "+c+", kicking client");
				c.close(false);
				return true;
			} else if (tries > 0 && debugEnabled) {
				lostTimeInRetries += System.currentTimeMillis() - before;
				log.debug("Written successfully "+total+" bytes after "+tries+" tries (total time lost: "+lostTimeInRetries+" ms)");
			}
		}
		catch (IOException e)
		{
			if (debugEnabled)
				log.debug("IOException writing to connection "+c, e);
			c.close (false);
			return true;
		}
		
		if (writeResult)
		{
			if (c.isPendingClose())
			{
				c.close(false);
			}
			return true;
		}
		else
		{
			return false;
		}
	}
}