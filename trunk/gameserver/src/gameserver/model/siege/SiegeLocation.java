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

package gameserver.model.siege;

import gameserver.model.templates.siege.SiegeLocationTemplate;

public class SiegeLocation
{
	public static final int	INVULNERABLE = 0;
	public static final int	VULNERABLE = 1;
	private int locationId;
	private int worldId;
	private SiegeType type;
	private SiegeLocationTemplate template;
	private SiegeRace siegeRace = SiegeRace.BALAUR;
	private int legionId = 0;
	private boolean isVulnerable = false;
	private boolean isShieldActive = false;
	private int nextState = 1;
	
	public SiegeLocation() {}
	public SiegeLocation(SiegeLocationTemplate template)
	{
		this.template = template;
		this.locationId = template.getId();
		this.worldId = template.getWorldId();
		this.type = template.getType();
	}

	public int getLocationId()
	{
		return this.locationId;
	}
	
	public SiegeType getSiegeType()
	{
		return type;
	}
	
	public int getWorldId()
	{
		return this.worldId;
	}
	
	public SiegeLocationTemplate getLocationTemplate()
	{
		return this.template;
	}
	
	public SiegeRace getRace()
	{
		return this.siegeRace;
	}
	
	public void setRace(SiegeRace siegeRace)
	{
		this.siegeRace = siegeRace;
	}
	
	public int getLegionId()
	{
		return this.legionId;
	}
	
	public void setLegionId(int legionId)
	{
		this.legionId = legionId;
	}

	public int getNextState()
	{
		return this.nextState;
	}

	public void setNextState(Integer nextState)
	{
		this.nextState = nextState;
	}

	public boolean isVulnerable()
	{
		return this.isVulnerable;
	}

	public void setVulnerable(boolean value)
	{
		this.isVulnerable = value;
		if (type == SiegeType.FORTRESS)
			this.isShieldActive = value;
	}

	public boolean isShieldActive()
	{
		return isShieldActive;
	}

	public void setShieldActive(boolean value)
	{
		if (type == SiegeType.FORTRESS)
			this.isShieldActive = value;
	}

	public int getInfluenceValue()
	{
		return 0;
	}
}
