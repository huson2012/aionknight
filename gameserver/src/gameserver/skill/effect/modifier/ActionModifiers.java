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

package gameserver.skill.effect.modifier;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ActionModifiers")
public class ActionModifiers {

    @XmlElements({
        @XmlElement(name = "frontdamage", type = FrontDamageModifier.class),
        @XmlElement(name = "backdamage", type = BackDamageModifier.class),
        @XmlElement(name = "flyingdamage", type = FlyingDamageModifier.class),
        @XmlElement(name = "nonflyingdamage", type = NonFlyingDamageModifier.class),
        @XmlElement(name = "targetrace", type = TargetRaceDamageModifier.class),
        @XmlElement(name = "abnormaldamage", type = AbnormalDamageModifier.class)
    })
    protected List<ActionModifier> actionModifiers;
    /**
     * Gets the value of the actionModifiers property.
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StumbleDamageModifier }
     * {@link FrontDamageModifier }
     * {@link BackDamageModifier }
     * {@link StunDamageModifier }
     * {@link PoisonDamageModifier }
     * {@link TargetRaceDamageModifier }
     * 
     */
    public List<ActionModifier> getActionModifiers() {
        if (actionModifiers == null) {
            actionModifiers = new ArrayList<ActionModifier>();
        }
        return this.actionModifiers;
    }
}
