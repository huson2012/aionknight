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
import gameserver.model.gameobjects.Summon;
import gameserver.model.gameobjects.player.Player;
import gameserver.services.GroupService;
import gameserver.skill.model.CreatureWithDistance;
import gameserver.skill.model.Skill;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FirstTargetProperty")
public class FirstTargetProperty
    extends Property
{

    @XmlAttribute(required = true)
    protected FirstTargetAttribute value;

    /**
     * Gets the value of the value property.
     * 
     * @return
     *    possible object is
     *    {@link FirstTargetAttribute }
     *    
     */
    public FirstTargetAttribute getValue() {
        return value;
    }
    
    @Override
	public boolean set(Skill skill)
	{
		skill.setFirstTargetProperty(value);
    	switch(value)
		{
			case ME:
				skill.setFirstTarget(skill.getEffector());
				break;			
			case TARGETORME:
				if(skill.getFirstTarget() == null)
					skill.setFirstTarget(skill.getEffector());
				break;
			case TARGET:
				if(skill.getFirstTarget() == null || skill.getFirstTarget() == skill.getEffector())
					return false;
				break;
			case MYPET:
				Creature effector = skill.getEffector();
				if(effector instanceof Player)
				{
					Summon summon = ((Player)effector).getSummon();
					if(summon != null)
						skill.setFirstTarget(summon);
					else
						return false;
				}
				else
					return false;
				break;
			case PASSIVE:
				skill.setFirstTarget(skill.getEffector());
				break;
			case TARGET_MYPARTY_NONVISIBLE:
				
				if(!(skill.getFirstTarget() instanceof Player))
					return false;
				
				Player effected = (Player) skill.getFirstTarget();
				
				if(effected == null || !GroupService.getInstance().isGroupMember(effected.getObjectId()) || skill.getEffector().getWorldId() != effected.getWorldId() )
					return false;
				skill.setFirstTargetRangeCheck(false);
				break;
			case POINT:
				skill.setFirstTarget(null);
				break;
		}

		if(skill.getFirstTarget() != null)
			skill.getEffectedList().add(new CreatureWithDistance(skill.getFirstTarget(), 0));
		return true;
	}
}
