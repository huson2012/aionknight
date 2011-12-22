/**   
 * Эмулятор игрового сервера Aion 2.7 от команды разработчиков 'Aion-Knight Dev. Team' является 
 * свободным программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного программного 
 * обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой более поздней 
 * версии.
 * 
 * Программа распространяется в надежде, что она будет полезной, но БЕЗ КАКИХ БЫ ТО НИ БЫЛО 
 * ГАРАНТИЙНЫХ ОБЯЗАТЕЛЬСТВ; даже без косвенных  гарантийных  обязательств, связанных с 
 * ПОТРЕБИТЕЛЬСКИМИ СВОЙСТВАМИ и ПРИГОДНОСТЬЮ ДЛЯ ОПРЕДЕЛЕННЫХ ЦЕЛЕЙ. Для подробностей смотрите 
 * Стандартную Общественную Лицензию GNU.
 * 
 * Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе с этой программой. 
 * Если это не так, напишите в Фонд Свободного ПО (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * Веб-cайт разработчиков : http://aion-knight.ru
 * Поддержка клиента игры : Aion 2.7 - 'Арена Смерти' (Иннова) 
 * Версия серверной части : Aion-Knight 2.7 (Beta version)
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
