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
@XmlRootElement(name = "player_stats_template")
public class PlayerStatsTemplate extends StatsTemplate
{
	@XmlAttribute(name = "power")
	private int power;
	@XmlAttribute(name = "health")
	private int health;
	@XmlAttribute(name = "agility")
	private int agility;
	@XmlAttribute(name = "accuracy")
	private int accuracy;
	@XmlAttribute(name = "knowledge")
	private int knowledge;
	@XmlAttribute(name = "will")
	private int will;
	
	public int getPower()
	{
		return power;
	}

	public int getHealth()
	{
		return health;
	}

	public int getAgility()
	{
		return agility;
	}

	public int getAccuracy()
	{
		return accuracy;
	}

	public int getKnowledge()
	{
		return knowledge;
	}

	public int getWill()
	{
		return will;
	}
}
