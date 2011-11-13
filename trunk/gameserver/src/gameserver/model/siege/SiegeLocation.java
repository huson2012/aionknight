/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
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
		if (getSiegeType() == SiegeType.FORTRESS)
			this.isShieldActive = value;
	}

	public boolean isShieldActive()
	{
		return isShieldActive;
	}

	public void setShieldActive(boolean value)
	{
		if (getSiegeType() == SiegeType.FORTRESS)
			this.isShieldActive = value;
	}

	public int getInfluenceValue()
	{
		return 0;
	}
}