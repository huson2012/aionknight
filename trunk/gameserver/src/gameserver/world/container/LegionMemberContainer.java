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

package gameserver.world.container;

import gameserver.model.legion.LegionMember;
import gameserver.model.legion.LegionMemberEx;
import gameserver.world.exceptions.DuplicateAionObjectException;
import javolution.util.FastMap;

public class LegionMemberContainer
{
	private final FastMap<Integer, LegionMember> legionMemberById = new FastMap<Integer, LegionMember>();
	private final FastMap<Integer, LegionMemberEx> legionMemberExById = new FastMap<Integer, LegionMemberEx>();
	private final FastMap<String, LegionMemberEx> legionMemberExByName = new FastMap<String, LegionMemberEx>();
	public void addMember(LegionMember legionMember)
	{
		if(legionMemberById.containsKey(legionMember.getObjectId()))
			throw new DuplicateAionObjectException();
		legionMemberById.put(legionMember.getObjectId(), legionMember);
	}

	public LegionMember getMember(int memberObjId)
	{
		return legionMemberById.get(memberObjId);
	}

	public void addMemberEx(LegionMemberEx legionMember)
	{
		if(legionMember == null)
			return;
		
		if(legionMemberExById.containsKey(legionMember.getObjectId())
			|| legionMemberExByName.containsKey(legionMember.getName()))
			throw new DuplicateAionObjectException();
		legionMemberExById.put(legionMember.getObjectId(), legionMember);
		legionMemberExByName.put(legionMember.getName(), legionMember);
	}

	public LegionMemberEx getMemberEx(int memberObjId)
	{
		return legionMemberExById.get(memberObjId);
	}

	public LegionMemberEx getMemberEx(String memberName)
	{
		return legionMemberExByName.get(memberName);
	}

	public void remove(LegionMemberEx legionMember)
	{
		legionMemberById.remove(legionMember.getObjectId());
		legionMemberExById.remove(legionMember.getObjectId());
		legionMemberExByName.remove(legionMember.getName());
	}

	public boolean contains(int memberObjId)
	{
		return legionMemberById.containsKey(memberObjId);
	}

	public boolean containsEx(int memberObjId)
	{
		return legionMemberExById.containsKey(memberObjId);
	}

	public boolean containsEx(String memberName)
	{
		return legionMemberExByName.containsKey(memberName);
	}
}