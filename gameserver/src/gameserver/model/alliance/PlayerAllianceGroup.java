/*
 * Emulator game server Aion 2.7 from the command of developers 'Aion-Knight Dev. Team' is
 * free software; you can redistribute it and/or modify it under the terms of
 * GNU affero general Public License (GNU GPL)as published by the free software
 * security (FSF), or to License version 3 or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranties related to
 * CONSUMER PROPERTIES and SUITABILITY FOR CERTAIN PURPOSES. For details, see
 * General Public License is the GNU.
 *
 * You should have received a copy of the GNU affero general Public License along with this program.
 * If it is not, write to the Free Software Foundation, Inc., 675 Mass Ave,
 * Cambridge, MA 02139, USA
 *
 * Web developers : http://aion-knight.ru
 * Support of the game client : Aion 2.7- 'Arena of Death' (Innova)
 * The version of the server : Aion-Knight 2.7 (Beta version)
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
