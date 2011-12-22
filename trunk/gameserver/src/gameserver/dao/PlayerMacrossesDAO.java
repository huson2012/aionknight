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
import gameserver.model.gameobjects.player.MacroList;

public abstract class PlayerMacrossesDAO implements DAO
{
	/**
	 * Returns unique identifier for PlayerMacroDAO
	 * @return unique identifier for PlayerMacroDAO
	 */
	@Override
	public final String getClassName()
	{
		return PlayerMacrossesDAO.class.getName();
	}

	/**
	 * Returns a list of macrosses for player
	 * @param playerId Player object id.
	 * @return a list of macrosses for player
	 */
	public abstract MacroList restoreMacrosses(int playerId);

	/**
	 * Add a macro information into database
	 * @param playerId player object id
	 * @param macroPosition macro order # of player
	 * @param macro macro contents.
	 */
	public abstract void addMacro(int playerId, int macroPosition, String macro);

	/**
	 * Update a macro information in database
	 */	
	public abstract void updateMacro(int playerId, int macroPosition, String macro);

	/**
	 * Remove macro in database
	 * @param playerId player object id
	 * @param macroPosition order of macro in macro list
	 */
	public abstract void deleteMacro(int playerId, int macroPosition);
}