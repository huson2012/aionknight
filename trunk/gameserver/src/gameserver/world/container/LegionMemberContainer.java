/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
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