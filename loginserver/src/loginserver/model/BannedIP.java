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
 * This class represents banned ip
 */
public class BannedIP
{
	/**
	 * Returns id of ip ban
	 */
	private Integer		id;

	/**
	 * Returns ip mask
	 */
	private String		mask;

	/**
	 * Returns expiration time
	 */
	private Timestamp	timeEnd;

	/**
	 * Checks if ban is still active
	 * 
	 * @return true if ban is still active
	 */
	public boolean isActive()
	{
		return timeEnd == null || timeEnd.getTime() > System.currentTimeMillis();
	}

	/**
	 * Returns ban id
	 * 
	 * @return ban id
	 */
	public Integer getId()
	{
		return id;
	}

	/**
	 * Sets ban id
	 * 
	 * @param id
	 *           ban id
	 */
	public void setId(Integer id)
	{
		this.id = id;
	}

	/**
	 * Retuns ip mask
	 * 
	 * @return ip mask
	 */
	public String getMask()
	{
		return mask;
	}

	/**
	 * Sets ip mask
	 * 
	 * @param mask
	 *           ip mask
	 */
	public void setMask(String mask)
	{
		this.mask = mask;
	}

	/**
	 * Returns expiration time of ban
	 * 
	 * @return expiration time of ban
	 */
	public Timestamp getTimeEnd()
	{
		return timeEnd;
	}

	/**
	 * Sets expiration time of ban
	 * 
	 * @param timeEnd
	 *           expiration time of ban
	 */
	public void setTimeEnd(Timestamp timeEnd)
	{
		this.timeEnd = timeEnd;
	}

	/**
	 * Returns true if this ip ban is equal to another. Based on {@link #mask}
	 * 
	 * @param o
	 *           another ip ban
	 * @return true if ban's are equals
	 */
	@Override
	public boolean equals(Object o)
	{
		if (this == o)
			return true;
		if (!(o instanceof BannedIP))
			return false;

		BannedIP bannedIP = (BannedIP) o;

		return !(mask != null ? !mask.equals(bannedIP.mask) : bannedIP.mask != null);
	}

	/**
	 * Returns ban's hashcode. Based on mask
	 * 
	 * @return ban's hashcode
	 */
	@Override
	public int hashCode()
	{
		return mask != null ? mask.hashCode() : 0;
	}
}
