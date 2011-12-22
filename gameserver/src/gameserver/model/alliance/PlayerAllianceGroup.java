/**
 * �������� �������� ������� Aion 2.7 �� ������� ������������� 'Aion-Knight Dev. Team' �������� 
 * ��������� ����������� ������������; �� ������ �������������� �/��� �������� ��� �������� �������� 
 * ����������� ������������ �������� GNU (GNU GPL), �������������� ������ ���������� ������������ 
 * ����������� (FSF), ���� �������� ������ 3, ���� (�� ���� ����������) ����� ����� ������� 
 * ������.
 * 
 * ��������� ���������������� � �������, ��� ��� ����� ��������, �� ��� ����� �� �� �� ���� 
 * ����������� ������������; ���� ��� ���������  �����������  ������������, ��������� � 
 * ���������������� ���������� � ������������ ��� ������������ �����. ��� ������������ �������� 
 * ����������� ������������ �������� GNU.
 * 
 * �� ������ ���� �������� ����� ����������� ������������ �������� GNU ������ � ���� ����������. 
 * ���� ��� �� ���, �������� � ���� ���������� �� (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * ���-c��� ������������� : http://aion-knight.ru
 * ��������� ������� ���� : Aion 2.7 - '����� ������' (������) 
 * ������ ��������� ����� : Aion-Knight 2.7 (Beta version)
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