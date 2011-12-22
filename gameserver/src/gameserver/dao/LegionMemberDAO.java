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

import gameserver.model.legion.LegionMember;
import gameserver.model.legion.LegionMemberEx;
import java.util.ArrayList;

/**
 * Class that is responsible for storing/loading legion data
 */

public abstract class LegionMemberDAO implements IDFactoryAwareDAO
{

	/**
	 * Returns true if name is used, false in other case
	 * @param name name to check
	 * @return true if name is used, false in other case
	 */
	public abstract boolean isIdUsed(int playerObjId);

	/**
	 * Creates legion member in DB
	 * @param legionMember
	 */
	public abstract void saveNewLegionMember(LegionMember legionMember);

	/**
	 * Stores legion member to DB
	 * @param player
	 */
	public abstract void storeLegionMember(int playerObjId, LegionMember legionMember);

	/**
	 * Loads a legion member
	 * @param playerObjId
	 * @param legionService
	 * @return LegionMember
	 */
	public abstract LegionMember loadLegionMember(int playerObjId);

	/**
	 * Loads an off line legion member by id
	 * @param playerObjId
	 * @param legionService
	 * @return LegionMemberEx
	 */
	public abstract LegionMemberEx loadLegionMemberEx(int playerObjId);

	/**
	 * Loads an off line legion member by name
	 * @param playerName
	 * @param legionService
	 * @return LegionMemberEx
	 */
	public abstract LegionMemberEx loadLegionMemberEx(String playerName);

	/**
	 * Loads all legion members of a legion
	 * @param legionId
	 * @return ArrayList<Integer>
	 */
	public abstract ArrayList<Integer> loadLegionMembers(int legionId);

	/**
	 * Removes legion member and all related data (Done by CASCADE DELETION)
	 * @param playerId legion member to delete
	 */
	public abstract void deleteLegionMember(int playerObjId);

	/**
	 * Identifier name for all LegionDAO classes
	 * @return LegionDAO.class.getName()
	 */
	@Override
	public final String getClassName()
	{
		return LegionMemberDAO.class.getName();
	}
}