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

package gameserver.utils.gametime;

import commons.database.dao.DAOManager;
import gameserver.dao.GameTimeDAO;
import gameserver.utils.ThreadPoolManager;
import org.apache.log4j.Logger;

/**
 * Manages ingame time
 */
public class GameTimeManager
{
	private static final Logger log = Logger.getLogger(GameTimeManager.class);
	private static GameTime			instance;
	private static GameTimeUpdater	updater;
	private static boolean			clockStarted	= false;

	static
	{
		GameTimeDAO dao = DAOManager.getDAO(GameTimeDAO.class);
		instance = new GameTime(dao.load());
	}

	/**
	 * Gets the current GameTime
	 * 
	 * @return GameTime
	 */
	public static GameTime getGameTime()
	{
		return instance;
	}

	/**
	 * Starts the counter that increases the clock every tick
	 * 
	 * @throws IllegalStateException
	 *            If called twice
	 */
	public static void startClock()
	{
		if(clockStarted)
		{
			throw new IllegalStateException("Clock is already started");
		}

		updater = new GameTimeUpdater(getGameTime());
		ThreadPoolManager.getInstance().scheduleAtFixedRate(updater, 0, 5000);
		
		clockStarted = true;
	}

	/**
	 * Saves the current time to the database
	 * 
	 * @return Success
	 */
	public static boolean saveTime()
	{
		log.info("Game time saved...");
		return DAOManager.getDAO(GameTimeDAO.class).store(getGameTime().getTime());
	}
}