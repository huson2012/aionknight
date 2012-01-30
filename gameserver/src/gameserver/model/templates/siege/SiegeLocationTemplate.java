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

package gameserver.model.templates.siege;

import gameserver.model.siege.SiegeType;
import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SiegeLocation")
public class SiegeLocationTemplate
{
	@XmlAttribute(name = "id")
	protected int		id;
	@XmlAttribute(name = "type")
	protected SiegeType	type;
	@XmlAttribute(name = "world")
	protected int		world;
	
	@XmlElement(name = "siege_reward")
	protected List<SiegeRewardTemplate> siegeRewards;
	
	@XmlElement(name = "defense_reward")
	protected DefenseReward defenseReward;
	
	@XmlAttribute(name = "artifact_cooldown")
	protected int artifactCooldown;
	
	/**
	 * @return the artifactCooldown
	 */
	public int getArtifactCooldown()
	{
		return artifactCooldown;
	}

	/**
	 * @return the location id
	 */
	public int getId()
	{
		return this.id;
	}
	
	/**
	 * @return the location type
	 */
	public SiegeType getType()
	{
		return this.type;
	}
	
	/**
	 * @return the world id
	 */
	public int getWorldId()
	{
		return this.world;
	}
	
	public List<SiegeRewardTemplate> getSiegeRewards()
	{
		return this.siegeRewards;
	}
	
	public DefenseReward getDefenseReward()
	{
		return this.defenseReward;
	}
	
}
