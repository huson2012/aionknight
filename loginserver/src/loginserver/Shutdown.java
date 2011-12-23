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

package loginserver;

import loginserver.network.IOServer;
import loginserver.utils.ThreadPoolManager;
import org.apache.log4j.Logger;
import commons.database.DatabaseFactory;

public class Shutdown extends Thread
{
	/**
	 * Logger for this class
	 */
	private static final Logger	log	= Logger.getLogger(Shutdown.class);
	
	/**
	 * Instance of Shutdown.
	 */
	private static Shutdown instance = new Shutdown();

	/**
	 * get the shutdown-hook instance the shutdown-hook instance is created by the first call of this function, but it
	 * has to be registrered externaly.
	 * 
	 * @return instance of Shutdown, to be used as shutdown hook
	 */
	public static Shutdown getInstance()
	{
		return instance;
	}

	/**
	 * this function is called, when a new thread starts if this thread is the thread of getInstance, then this is the
	 * shutdown hook and we save all data and disconnect all clients. after this thread ends, the server will completely
	 * exit if this is not the thread of getInstance, then this is a countdown thread. we start the countdown, and when
	 * we finished it, and it was not aborted, we tell the shutdown-hook why we call exit, and then call exit when the
	 * exit status of the server is 1, startServer.sh / startServer.bat will restart the server.
	 */
	@Override
	public void run()
	{
		/** 
		 * Disconnecting all the clients 
		 */
		try
		{
			IOServer.getInstance().shutdown();
		}
		catch (Throwable t)
		{
			log.error("Can't shutdown I/O Server", t);
		}

		/** 
		 * Shuting down DB connections 
		 */
		try
		{
			DatabaseFactory.shutdown();
		}
		catch (Throwable t)
		{
			log.error("Can't shutdown DatabaseFactory", t);
		}

		/** 
		 * Shuting down threadpools 
		 */
		try
		{
			ThreadPoolManager.getInstance().shutdown();
		}
		catch (Throwable t)
		{
			log.error("Can't shutdown ThreadPoolManager", t);
		}
	}
}