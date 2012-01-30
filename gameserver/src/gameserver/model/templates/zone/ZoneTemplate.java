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

package gameserver.model.templates.zone;

import gameserver.world.zone.ZoneName;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Zone")
public class ZoneTemplate
{
	@XmlElement(required = true)
	protected Points points;
	protected List<ZoneName> link;
	@XmlAttribute
	protected int priority;
	@XmlAttribute(name="fly")
	protected boolean flightAllowed;
    @XmlAttribute(name = "flyBan")
    protected boolean flightBanned;
	@XmlAttribute(name="breath")
	protected boolean breath;
	@XmlAttribute
	protected ZoneName name;
	@XmlAttribute
	protected int mapid;

	/**
	 * Gets the value of the points property.
	 */
	public Points getPoints() {
		return points;
	}

	/**
	 * Gets the value of the link property.
	 */
	public List<ZoneName> getLink() {
		if (link == null) {
			link = new ArrayList<ZoneName>();
		}
		return this.link;
	}

	/**
	 * @return the priority
	 */
	public int getPriority()
	{
		return priority;
	}

	/**
	 * @return the flightAllowed
	 */
	public boolean isFlightAllowed()
	{
		return flightAllowed;
	}

	/**
     * @return the flightAllowed
     */
    public boolean isFlightBanned() {
        return flightBanned;
    }

    /**
	 * Gets the value of the name property.   
	 */
	public ZoneName getName() {
		return name;
	}

	/**
	 * Gets the value of the mapid property.
	 */
	public int getMapid() {
		return mapid;
	}

	/**
	 * @return the breath
	 */
	public boolean isBreath()
	{
		return breath;
	}
}
