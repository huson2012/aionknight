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

package gameserver.controllers.attack;

import gameserver.model.gameobjects.AionObject;

public class AggroInfo
{
	private AionObject attacker;
	private int hate;
	private int damage;
	
	/**
	 * @param attacker
	 */
	AggroInfo(AionObject attacker)
	{
		this.attacker = attacker;
	}

	/**
	 * @return attacker
	 */
	public AionObject getAttacker()
	{
		return attacker;
	}

	/**
	 * @param damage
	 */
	public void addDamage(int damage)
	{
		this.damage += damage;
		if (this.damage < 0)
			this.damage = 0;
	}

	/**
	 * @param damage
	 */
	public void addHate(int damage)
	{
		this.hate += damage;
		if (this.hate < 1)
			this.hate = 1;
	}

	/**
	 * @return hate
	 */
	public int getHate()
	{
		return this.hate;
	}

	/**
	 * @param hate
	 */
	public void setHate(int hate)
	{
		this.hate = hate;
	}

	/**
	 * @return damage
	 */
	public int getDamage()
	{
		return this.damage;
	}

	/**
	 * @param damage
	 */
	public void setDamage(int damage)
	{
		this.damage = damage;
	}
}