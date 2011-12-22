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

import gameserver.controllers.attack.AttackResult;
import gameserver.controllers.attack.AttackStatus;
import gameserver.model.gameobjects.Creature;
import java.util.List;

public class AttackCalcObserver
{
	/**
	 * @param status
	 * @return false
	 */
	public boolean checkStatus(AttackStatus status)
	{
		return false;
	}
	
	/**
	 * @param value
	 * @return value
	 */
	public void checkShield(List<AttackResult> attackList, Creature attacker)
	{
		
	}

	/**
	 * @param status
	 * @return
	 */
	public boolean checkAttackerStatus(AttackStatus status)
	{
		return false;
	}
	
	public float getBasePhysicalDamageMultiplier ()
	{
		return 1;
	}
	
	public float getBaseMagicalDamageMultiplier ()
	{
		return 1;
	}
}