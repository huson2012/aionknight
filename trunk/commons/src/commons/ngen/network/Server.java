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
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import org.apache.log4j.Logger;

public class Server extends Thread
{
	private ServerConfig        config;
	private Acceptor			acceptor;
	private static final Logger log = Logger.getLogger(Server.class);
	private boolean             running = false;
	private boolean             debugEnabled = false;
	
	public Server (ServerConfig config)
	{
		super("server-"+config.name.replace(" ", "-").toLowerCase());
		
		this.config = config;
		this.debugEnabled = config.debugEnabled;
		
		if (config.readThreads <= 0 || config.writeThreads <= 0 || (config.enableWorkers && config.workerThreads <= 0))
		{
			if (config.enableWorkers)
			{
				throw new RuntimeException ("You should at least use one reader thread, one writer thread and one worker thread");
			}
			else
			{
				throw new RuntimeException ("You should at least use one write thread and one read thread");
			}
		}
	}
	
	public boolean isDebugEnabled ()
	{
		return debugEnabled;
	}
	
	@Override
	public void run ()
	{
		try
		{
			ServerSocketChannel ssc = ServerSocketChannel.open();

			ssc.configureBlocking(false);

			InetSocketAddress isa;
			if ("*".equals(config.hostname))
			{
				isa = new InetSocketAddress(config.port);
			}
			else
			{
				isa = new InetSocketAddress(config.hostname, config.port);
			}

			ssc.socket().bind(isa);
			acceptor = new Acceptor ("server-"+config.name.replace(" ", "-").toLowerCase()+"-acceptor", ssc, config.factory, config.readThreads, config.writeThreads, config.enableWorkers, config.workerThreads, config.bufferCount, config.readTries, config.writeTries, config.debugEnabled);
			acceptor.setDaemon(false);
			acceptor.start();
			
			synchronized (acceptor)
			{
				try { acceptor.wait(); } catch (InterruptedException e) { }
			}
			
			synchronized (this)
			{
				running = true;
				notifyAll();
			}
			
			log.info("Started "+getClass().getSimpleName()+" "+config.name+" listening... ");
		}
		catch (IOException e)
		{
			log.error("Error starting server !", e);
			System.exit(1);
		}
		
		try { acceptor.join(); } catch (InterruptedException e) { }
		log.debug(getName()+" stopped");
	}
	
	public void manage (Connection c) throws IOException
	{
		synchronized (this)
		{
			while (!running)
			{
				try { wait(); } catch (InterruptedException e) { }
			}
		}
		
		acceptor.manage(c);
	}

	public void close()
	{
		if (acceptor!=null)
		{
			acceptor.close();
		}
	}
}