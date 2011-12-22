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

package gameserver.dataholders;

import gameserver.model.templates.tribe.*;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlRootElement(name = "tribe_relations")
@XmlAccessorType(XmlAccessType.FIELD)
public class TribeRelationsData
{
	@XmlElement(name = "tribe", required = true)
	protected List<Tribe> tribeList;

	protected Map<String, Tribe> tribeNameMap = new HashMap<String, Tribe>();

	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		for(Tribe tribe : tribeList)
		{
			tribeNameMap.put(tribe.getName(), tribe);
		}
		tribeList = null;
	}

	public int size()
	{
		return tribeNameMap.size();
	}

	public boolean hasAggressiveRelations(String tribeName)
	{
		Tribe tribe = tribeNameMap.get(tribeName);
		if(tribe == null)
			return false;
		AggroRelations aggroRelations = tribe.getAggroRelations();
		return aggroRelations != null && !aggroRelations.getTo().isEmpty();
	}

	public boolean hasHostileRelations(String tribeName)
	{
		Tribe tribe = tribeNameMap.get(tribeName);
		if(tribe == null)
			return false;
		HostileRelations hostileRelations = tribe.getHostileRelations();
		return hostileRelations != null && !hostileRelations.getTo().isEmpty();
	}

	public boolean isAggressiveRelation(String tribeName1, String tribeName2)
	{
		Tribe tribe1;
		AggroRelations aggroRelations;
		
		while((tribe1 = tribeNameMap.get(tribeName1)) != null) // Navigate through tribeName1 aggro relation and its bases if needed
		{
			aggroRelations = tribe1.getAggroRelations();

			if(aggroRelations != null && aggroRelations.getTo().contains(tribeName2))
				return true;

			tribeName1 = tribe1.getBase();
		}
		
		return false;
	}

	public boolean isSupportRelation(String tribeName1, String tribeName2)
	{
		Tribe tribe1 = tribeNameMap.get(tribeName1);
		if(tribe1 == null)
			return false;
		SupportRelations supportRelations = tribe1.getSupportRelations();
		if(supportRelations == null)
			return false;
		return supportRelations.getTo().contains(tribeName2);
	}

	public boolean isFriendlyRelation(String tribeName1, String tribeName2)
	{
		Tribe tribe1 = tribeNameMap.get(tribeName1);
		if(tribe1 == null)
			return false;
		FriendlyRelations friendlyRelations = tribe1.getFriendlyRelations();
		if(friendlyRelations == null)
			return false;
		return friendlyRelations.getTo().contains(tribeName2);
	}

	public boolean isNeutralRelation(String tribeName1, String tribeName2)
	{
		Tribe tribe1 = tribeNameMap.get(tribeName1);
		if(tribe1 == null)
			return false;
		NeutralRelations neutralRelations = tribe1.getNeutralRelations();
		if(neutralRelations == null)
			return false;
		return neutralRelations.getTo().contains(tribeName2);
	}

	public boolean isHostileRelation(String tribeName1, String tribeName2)
	{
		Tribe tribe1 = tribeNameMap.get(tribeName1);
		if(tribe1 == null)
			return false;
		HostileRelations hostileRelations = tribe1.getHostileRelations();
		if(hostileRelations == null)
			return false;
		return hostileRelations.getTo().contains(tribeName2);
	}

	public boolean isGuardDark(String tribeName)
	{
		Tribe tribe = tribeNameMap.get(tribeName);
		if(tribe == null)
			return false;
		
		if(Tribe.GUARD_DARK.equals(tribe.getName()))
			return true;
		
		String baseTribe = tribe.getBase();
		if(baseTribe != null)
			return isGuardDark(baseTribe);
		
		return false;
	}

	public boolean isGuardLight(String tribeName)
	{
		Tribe tribe = tribeNameMap.get(tribeName);
		if(tribe == null)
			return false;
		
		if(Tribe.GUARD_LIGHT.equals(tribe.getName()))
			return true;
		
		String baseTribe = tribe.getBase();
		if(baseTribe != null)
			return isGuardLight(baseTribe);
		
		return false;
	}

	public boolean isGuardDrakan(String tribeName)
	{
		Tribe tribe = tribeNameMap.get(tribeName);
		if(tribe == null)
			return false;
		
		if(Tribe.GUARD_DRAGON.equals(tribe.getName()))
			return true;
		
		String baseTribe = tribe.getBase();
		if(baseTribe != null)
			return isGuardDrakan(baseTribe);
		
		return false;
	}
}