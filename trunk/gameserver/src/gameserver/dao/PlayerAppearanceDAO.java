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
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.PlayerAppearance;

/**
 * Class that is responsible for loading/storing player appearance
 */
public abstract class PlayerAppearanceDAO implements DAO
{
	/**
	 * Returns unique identifier for PlayerAppearanceDAO
	 * @return unique identifier for PlayerAppearanceDAO
	 */
	@Override
	public final String getClassName()
	{
		return PlayerAppearanceDAO.class.getName();
	}

	/**
	 * Loads player apperance DAO by player ID.<br>
	 * Returns nullIf not found in database
	 * @param playerId player id
	 * @return player appearance or null
	 */
	public abstract PlayerAppearance load(int playerId);

	/**
	 * Saves player appearance in database.
	 * Actually calls {@link #store(int, gameserver.model.gameobjects.player.PlayerAppearance)}
	 * @param player whos appearance to store
	 * @return true, if sql query was successful, false overwise
	 */
	public final boolean store(Player player)
	{
		return store(player.getObjectId(), player.getPlayerAppearance());
	}

	/**
	 * Stores appearance in database
	 * 
	 * @param id player id
	 * @param playerAppearance player appearance
	 * @return true, if sql query was successful, false overwise
	 */
	public abstract boolean store(int id, PlayerAppearance playerAppearance);
}