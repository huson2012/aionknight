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
import gameserver.model.gameobjects.player.BlockList;
import gameserver.model.gameobjects.player.Player;

/**
 * Responsible for saving and loading data on players' block lists
 */
public abstract class BlockListDAO implements DAO
{
	/**
	 * Loads the blocklist for the player given
	 * @param player
	 * @return BlockList
	 */
	public abstract BlockList load(Player player);
	
	/**
	 * Adds the given object id to the list of blocked players for the given player
	 * @param playerObjId ID of player to edit the blocklist of
	 * @param objIdToBlock ID of player to add to the blocklist
	 * @return Success
	 */
	public abstract boolean addBlockedUser(int playerObjId, int objIdToBlock, String reason);

	/**
	 * Deletes the given object id from the list of blocked players for the given player
	 * @param playerObjId ID of player to edit the blocklist of
	 * @param objIdToDelete ID of player to remove from the blocklist
	 * @return Success
	 */
	public abstract boolean delBlockedUser(int playerObjId, int objIdToDelete);
	
	/**
	 * Sets the reason for blocking a player
	 * @param playerObjId Object ID of the player whos list is being edited
	 * @param blockedObjId Object ID of the player whos reason is being edited
	 * @param reason The reason to be set
	 * @return true or false
	 */
	public abstract boolean setReason(int playerObjId, int blockedObjId, String reason);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getClassName()
	{
		return BlockListDAO.class.getName();
	}
}