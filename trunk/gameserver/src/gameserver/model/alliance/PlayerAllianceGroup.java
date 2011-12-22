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

package gameserver.model.alliance;

import javolution.util.FastMap;
import java.util.Collection;

public class PlayerAllianceGroup
{
	private FastMap<Integer, PlayerAllianceMember> groupMembers;
	private int allianceId;
	private PlayerAlliance owner;
	public PlayerAllianceGroup(PlayerAlliance alliance)
	{
		groupMembers = new FastMap<Integer, PlayerAllianceMember>().shared();
		this.owner = alliance;
	}
	
	public PlayerAlliance getParent()
	{
		return owner;
	}
	
	public void setAllianceId(int allianceId)
	{
		this.allianceId = allianceId;
	}
	
	public int getAllianceId()
	{
		return this.allianceId;
	}
	
	public void addMember(PlayerAllianceMember member)
	{
		groupMembers.put(member.getObjectId(), member);
		member.setAllianceId(allianceId);
	}

	public PlayerAllianceMember removeMember(int memberObjectId)
	{
		return groupMembers.remove(memberObjectId);
	}
	
	public PlayerAllianceMember getMemberById(int memberObjectId)
	{
		return groupMembers.get(memberObjectId);
	}
	
	public Collection<PlayerAllianceMember> getMembers()
	{
		return groupMembers.values();
	}
	public boolean isInSamePlayerAllianceGroup(int memberObjectId,int member2ObjectId)
	{
		return (groupMembers.containsKey(memberObjectId) && groupMembers.containsKey(member2ObjectId));
	}
}