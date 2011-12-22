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

package gameserver.controllers.movement;

import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.state.CreatureState;
import gameserver.skill.action.DamageType;
import gameserver.skill.model.Skill;

public class ActionObserver
{
	public enum ObserverType
	{
		MOVE,
		ATTACK,
		ATTACKED,
		EQUIP,
		SKILLUSE,
		STATECHANGE,
		DEATH,
		JUMP,
		DOT,
		HITTED
	}
	
	private ObserverType observerType;
	
	public ActionObserver(ObserverType observerType)
	{
		this.observerType = observerType;
	}
	
	/**
	 * @return the observerType
	 */
	public ObserverType getObserverType()
	{
		return observerType;
	}

	public void moved(){};
	public void attacked(Creature creature){};
	public void attack(Creature creature){};
	public void equip(Item item, Player owner){};
	public void unequip(Item item, Player owner){};
	public void skilluse(Skill skill){};
	public void stateChanged(CreatureState state, boolean isSet) {};
	public void died(Creature creature) {};
	public void jump(){};
	public void onDot(Creature creature) {};
	public void hitted(Creature creature, DamageType type) {};
}