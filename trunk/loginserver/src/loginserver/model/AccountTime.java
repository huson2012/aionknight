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

package loginserver.model;

import java.sql.Timestamp;

/**
 * Class for storing account time data (last login time,
 * last session duration time, accumulated online time today,
 * accumulated rest time today)
 *
 * @author EvilSpirit
 */
public class AccountTime
{
	/**
	 * Time the account has last logged in
	 */
	private Timestamp	lastLoginTime;
	/**
	 * Time after the account will expired
	 */
	private Timestamp	expirationTime;
	/**
	 * Time when the penalty will end
	 */
	private Timestamp	penaltyEnd;
	/**
	 * The duration of the session
	 */
	private long		sessionDuration;
	/**
	 * Accumulated Online Time
	 */
	private long		accumulatedOnlineTime;
	/**
	 * Accumulated Rest Time
	 */
	private long		accumulatedRestTime;

	/**
	 * Default constructor. Set the lastLoginTime to current time
	 */
	public AccountTime()
	{
		this.lastLoginTime = new Timestamp(System.currentTimeMillis());
	}

	/**
	 * @return lastLoginTime
	 */
	public Timestamp getLastLoginTime()
	{
		return lastLoginTime;
	}

	/**
	 * @param lastLoginTime
	 */
	public void setLastLoginTime(Timestamp lastLoginTime)
	{
		this.lastLoginTime = lastLoginTime;
	}

	/**
	 * @return sessionDuration
	 */
	public long getSessionDuration()
	{
		return sessionDuration;
	}

	/**
	 * @param sessionDuration
	 */
	public void setSessionDuration(long sessionDuration)
	{
		this.sessionDuration = sessionDuration;
	}

	/**
	 * @return accumulatedOnlineTime
	 */
	public long getAccumulatedOnlineTime()
	{
		return accumulatedOnlineTime;
	}

	/**
	 * @param accumulatedOnlineTime
	 */
	public void setAccumulatedOnlineTime(long accumulatedOnlineTime)
	{
		this.accumulatedOnlineTime = accumulatedOnlineTime;
	}

	/**
	 * @return accumulatedRestTime
	 */
	public long getAccumulatedRestTime()
	{
		return accumulatedRestTime;
	}

	/**
	 * @param accumulatedRestTime
	 */
	public void setAccumulatedRestTime(long accumulatedRestTime)
	{
		this.accumulatedRestTime = accumulatedRestTime;
	}

	/**
	 * @return expirationTime
	 */
	public Timestamp getExpirationTime()
	{
		return expirationTime;
	}

	/**
	 * @param expirationTime
	 */
	public void setExpirationTime(Timestamp expirationTime)
	{
		this.expirationTime = expirationTime;
	}

	/**
	 * @return penaltyEnd
	 */
	public Timestamp getPenaltyEnd()
	{
		return penaltyEnd;
	}

	/**
	 * @param penaltyEnd
	 */
	public void setPenaltyEnd(Timestamp penaltyEnd)
	{
		this.penaltyEnd = penaltyEnd;
	}
}
