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
package gameserver.dao;

import commons.database.dao.DAO;

public abstract class PlayerPasskeyDAO implements DAO
{
	/**
	* @param accountId
	* @param passkey
	*/
	public abstract void insertPlayerPasskey(int accountId, String passkey);

	/**
	* @param accountId
	* @param oldPasskey
	* @param newPasskey
	* @return
	*/
	public abstract boolean updatePlayerPasskey(int accountId, String oldPasskey, String newPasskey);

	/**
	* @param accountId
	* @param newPasskey
	* @return
	*/
	public abstract boolean updateForcePlayerPasskey(int accountId, String newPasskey);

	/**
	* @param accountId
	* @param passkey
	* @return
	*/
	public abstract boolean checkPlayerPasskey(int accountId, String passkey);

	/**
	* @param accountId
	* @return
	*/
	public abstract boolean existCheckPlayerPasskey(int accountId);

	/**
	* (non-Javadoc)
	* @see commons.database.dao.DAO#getClassName()
	*/
	@Override
	public final String getClassName()
	{
		return PlayerPasskeyDAO.class.getName();
	}
}