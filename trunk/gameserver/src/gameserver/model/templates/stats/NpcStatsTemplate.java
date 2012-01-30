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

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "npc_stats_template")
public class NpcStatsTemplate extends StatsTemplate
{
	@XmlAttribute(name = "run_speed_fight")
	private float runSpeedFight;
	@XmlAttribute(name = "pdef")
	private int pdef;
	@XmlAttribute(name = "mdef")
	private int mdef;
	@XmlAttribute(name = "crit")
	private int crit;
	@XmlAttribute(name = "accuracy")
	private int accuracy;
	@XmlAttribute(name = "power")
	private int power;
	@XmlAttribute(name = "maxXp")
	private int maxXp;
	
	public float getRunSpeedFight()
	{
		return runSpeedFight;
	}

	/**
	 * @return the pdef
	 */
	public float getPdef()
	{
		return pdef;
	}

	/**
	 * @return the mdef
	 */
	public float getMdef()
	{
		return mdef;
	}

	/**
	 * @return the crit
	 */
	public float getCrit()
	{
		return crit;
	}

	/**
	 * @return the accuracy
	 */
	public float getAccuracy()
	{
		return accuracy;
	}

	/**
	 * @return the power
	 */
	public int getPower()
	{
		return power;
	}

	/**
	 * @return the maxXp
	 */
	public int getMaxXp()
	{
		return maxXp;
	}

	/**
	 * @param pdef the pdef to set
	 */
	public void setPdef(int pdef)
	{
		this.pdef = pdef;
	}

	/**
	 * @param mdef the mdef to set
	 */
	public void setMdef(int mdef)
	{
		this.mdef = mdef;
	}

	/**
	 * @param crit the crit to set
	 */
	public void setCrit(int crit)
	{
		this.crit = crit;
	}

	/**
	 * @param accuracy the accuracy to set
	 */
	public void setAccuracy(int accuracy)
	{
		this.accuracy = accuracy;
	}

	/**
	 * @param power the power to set
	 */
	public void setPower(int power)
	{
		this.power = power;
	}
	
}
