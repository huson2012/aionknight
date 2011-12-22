/**
 * �������� �������� ������� Aion 2.7 �� ������� ������������� 'Aion-Knight Dev. Team' �������� 
 * ��������� ����������� ������������; �� ������ �������������� �/��� �������� ��� �������� �������� 
 * ����������� ������������ �������� GNU (GNU GPL), �������������� ������ ���������� ������������ 
 * ����������� (FSF), ���� �������� ������ 3, ���� (�� ���� ����������) ����� ����� ������� 
 * ������.
 * 
 * ��������� ���������������� � �������, ��� ��� ����� ��������, �� ��� ����� �� �� �� ���� 
 * ����������� ������������; ���� ��� ���������  �����������  ������������, ��������� � 
 * ���������������� ���������� � ������������ ��� ������������ �����. ��� ������������ �������� 
 * ����������� ������������ �������� GNU.
 * 
 * �� ������ ���� �������� ����� ����������� ������������ �������� GNU ������ � ���� ����������. 
 * ���� ��� �� ���, �������� � ���� ���������� �� (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * ���-c��� ������������� : http://aion-knight.ru
 * ��������� ������� ���� : Aion 2.7 - '����� ������' (������) 
 * ������ ��������� ����� : Aion-Knight 2.7 (Beta version)
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