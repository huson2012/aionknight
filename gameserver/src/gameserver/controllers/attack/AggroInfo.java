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