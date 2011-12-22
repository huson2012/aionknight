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

package gameserver.skill.condition;

import gameserver.model.gameobjects.player.Player;
import gameserver.skill.model.Skill;
import gameserver.skill.properties.TargetSpeciesAttribute;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SelfCondition")
public class SelfCondition
    extends Condition
{

    @XmlAttribute(required = true)
    protected TargetSpeciesAttribute value;
    
    @XmlAttribute
    protected FlyRestrictionAttribute restriction;

    /**
     * Gets the value of the value property.
     * 
     * @return
     *    possible object is
     *    {@link TargetSpeciesAttribute }
     *    
     */
    public TargetSpeciesAttribute getValue() {
        return value;
    }

	@Override
	public boolean verify(Skill skill)
	{
		if(skill.getEffector() == null)
			return false;
		
		//0: regular, 1: fly, 2: glide
		if (skill.getEffector() instanceof Player)
		{
			switch (restriction)
			{
				case GROUND:
					if (((Player)skill.getEffector()).getFlyState() != 0)
						return false;
					break;
				case FLY:
					if (((Player)skill.getEffector()).getFlyState() != 1)
						return false;
					break;
			}
		}
		
		return true;
	}
}