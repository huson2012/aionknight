/*
 * Emulator game server Aion 2.7 from the command of developers 'Aion-Knight Dev. Team' is
 * free software; you can redistribute it and/or modify it under the terms of
 * GNU affero general Public License (GNU GPL)as published by the free software
 * security (FSF), or to License version 3 or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranties related to
 * CONSUMER PROPERTIES and SUITABILITY FOR CERTAIN PURPOSES. For details, see
 * General Public License is the GNU.
 *
 * You should have received a copy of the GNU affero general Public License along with this program.
 * If it is not, write to the Free Software Foundation, Inc., 675 Mass Ave,
 * Cambridge, MA 02139, USA
 *
 * Web developers : http://aion-knight.ru
 * Support of the game client : Aion 2.7- 'Arena of Death' (Innova)
 * The version of the server : Aion-Knight 2.7 (Beta version)
 */

package gameserver.model.templates.stats;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class is only a container for Stats.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "stats_template")
public abstract class StatsTemplate
{
	@XmlAttribute(name = "maxHp")
	private int maxHp;
	@XmlAttribute(name = "maxMp")
	private int maxMp;

	@XmlAttribute(name = "walk_speed")
	private float walkSpeed;
	@XmlAttribute(name = "run_speed")
	private float runSpeed;
	@XmlAttribute(name = "fly_speed")
	private float flySpeed;

	@XmlAttribute(name = "attack_speed")
	private float attackSpeed;

	@XmlAttribute(name = "evasion")
	private int evasion;
	@XmlAttribute(name = "block")
	private int block;
	@XmlAttribute(name = "parry")
	private int parry;

	@XmlAttribute(name = "main_hand_attack")
	private int mainHandAttack;
	@XmlAttribute(name = "main_hand_accuracy")
	private int mainHandAccuracy;
	@XmlAttribute(name = "main_hand_crit_rate")
	private int mainHandCritRate;

	@XmlAttribute(name = "magic_accuracy")
	private int magicAccuracy;

	/** ======================================= */

	/**
	 * @param evasion the evasion to set
	 */
	public void setEvasion(int evasion)
	{
		this.evasion = evasion;
	}

	/**
	 * @param block the block to set
	 */
	public void setBlock(int block)
	{
		this.block = block;
	}

	/**
	 * @param parry the parry to set
	 */
	public void setParry(int parry)
	{
		this.parry = parry;
	}

	/**
	 * @param mainHandAttack the mainHandAttack to set
	 */
	public void setMainHandAttack(int mainHandAttack)
	{
		this.mainHandAttack = mainHandAttack;
	}

	/**
	 * @param mainHandAccuracy the mainHandAccuracy to set
	 */
	public void setMainHandAccuracy(int mainHandAccuracy)
	{
		this.mainHandAccuracy = mainHandAccuracy;
	}

	/**
	 * @param mainHandCritRate the mainHandCritRate to set
	 */
	public void setMainHandCritRate(int mainHandCritRate)
	{
		this.mainHandCritRate = mainHandCritRate;
	}

	/**
	 * @param magicAccuracy the magicAccuracy to set
	 */
	public void setMagicAccuracy(int magicAccuracy)
	{
		this.magicAccuracy = magicAccuracy;
	}
	
	public void setRunSpeed(int runSpeed)
	{
		this.runSpeed = runSpeed;
	}
	
	public void setFlySpeed(int flySpeed)
	{
		this.flySpeed = flySpeed;
	}

	public int getMaxHp()
	{
		return maxHp;
	}

	public int getMaxMp()
	{
		return maxMp;
	}


	/** ======================================= */

	public float getWalkSpeed()
	{
		return walkSpeed;
	}

	public float getRunSpeed()
	{
		return runSpeed;
	}

	public float getFlySpeed()
	{
		return flySpeed;
	}

	public float getAttackSpeed()
	{
		return attackSpeed;
	}

	/** ======================================= */

	public int getEvasion()
	{
		return evasion;
	}

	public int getBlock()
	{
		return block;
	}

	public int getParry()
	{
		return parry;
	}

	/** ======================================= */

	public int getMainHandAttack()
	{
		return mainHandAttack;
	}

	public int getMainHandAccuracy()
	{
		return mainHandAccuracy;
	}

	public int getMainHandCritRate()
	{
		return mainHandCritRate;
	}

	/** ======================================= */

	public int getMagicAccuracy()
	{
		return magicAccuracy;
	}
}
