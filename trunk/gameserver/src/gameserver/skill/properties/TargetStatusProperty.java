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

package gameserver.skill.properties;

import gameserver.model.gameobjects.Creature;
import gameserver.skill.effect.EffectId;
import gameserver.skill.model.Skill;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.EnumSet;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TargetStatusProperty")
public class TargetStatusProperty extends Property
{

	@XmlAttribute(name = "value", required = true)
	protected String stateSet;
	
	@XmlTransient
	protected EnumSet<EffectId> value = EnumSet.noneOf(EffectId.class);
	
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		if (stateSet == null || stateSet.isEmpty())
			return;

		String[] states = stateSet.split(" ");
		for (String state : states)
			value.add(EffectId.valueOf(state));

		stateSet = null;
	}
	
	@Override
	public boolean set(Skill skill)
	{
		if (skill.getEffectedList().size() != 1)
			return false;
		
		Creature effected = skill.getEffectedList().first().getCreature();
		boolean result = false;
		
		for (EffectId as : value)
		{
			if (effected.getEffectController().isAbnormalSet(as))
				result = true;
		}
		
		return result;
	}
}