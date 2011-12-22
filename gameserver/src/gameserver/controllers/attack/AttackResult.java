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

import gameserver.skill.action.DamageType;

public class AttackResult
{
	private int damage;
	
	private AttackStatus attackStatus;
	
	private int shieldType;
	
	private int reflectedDamage;
	
	private int skillId;
	
	private DamageType damageType;
	
	public AttackResult(int damage, AttackStatus attackStatus)
	{
		this(damage, attackStatus, 0, 0, DamageType.PHYSICAL);
	}
	public AttackResult(int damage, AttackStatus attackStatus, int reflectedDamage, int skillId)
	{
		this(damage, attackStatus, reflectedDamage, skillId, DamageType.PHYSICAL);
	}
	public AttackResult(int damage, AttackStatus attackStatus, int reflectedDamage, int skillId, DamageType damageType)
	{
		this.damage = damage;
		this.attackStatus = attackStatus;
		this.reflectedDamage = reflectedDamage;
		this.skillId = skillId;
		this.damageType = damageType;
	}

	/**
	 * @return the damage
	 */
	public int getDamage()
	{
		return damage;
	}

	/**
	 * @param damage the damage to set
	 */
	public void setDamage(int damage)
	{
		this.damage = damage;
	}

	/**
	 * @return the attackStatus
	 */
	public AttackStatus getAttackStatus()
	{
		return attackStatus;
	}

	/**
	 * @return the shieldType
	 */
	public int getShieldType()
	{
		return shieldType;
	}

	/**
	 * @param shieldType the shieldType to set
	 */
	public void setShieldType(int shieldType)
	{
		this.shieldType = shieldType;
	}
	
	public int getReflectedDamage()
	{
		return this.reflectedDamage;
	}
	public void setReflectedDamage(int reflectedDamage)
	{
		this.reflectedDamage = reflectedDamage;
	}
	public int getSkillId()
	{
		return this.skillId;
	}
	public void setSkillId(int skillId)
	{
		this.skillId = skillId;
	}
	public DamageType getDamageType()
	{
		return this.damageType;
	}
	
}
