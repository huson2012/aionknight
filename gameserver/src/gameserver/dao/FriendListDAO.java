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
import gameserver.model.gameobjects.player.FriendList;
import gameserver.model.gameobjects.player.Player;

public abstract class FriendListDAO implements DAO
{

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getClassName()
	{
		return FriendListDAO.class.getName();
	}
	/**
	 * Loads the friend list for the given player
	 * @param player Player to get friend list of
	 * @return FriendList for player
	 */
	public abstract FriendList load(final Player player);
	
	/**
	 * Makes the given players friends
	 * <ul><li>Note: Adds for both players</li></ul>
	 * @param player Player who is adding
	 * @param friend Friend to add to the friend list
	 * @return Success
	 */
	public abstract boolean addFriends(final Player player, final Player friend);

	/**
	 * Deletes the friends from eachothers lists
	 * @param player Player whos is deleting
	 * @param friendName Name of friend to delete
	 * @return Success
	 */
	public abstract boolean delFriends(final int playerOid, final int friendOid);	
}