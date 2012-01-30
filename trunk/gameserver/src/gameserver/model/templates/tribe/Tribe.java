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

package gameserver.model.templates.tribe;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Tribe")
public class Tribe
{
	public static final String GUARD_DARK = "GUARD_DARK";
	public static final String GUARD_DRAGON = "GUARD_DRAGON";
	public static final String GUARD_LIGHT = "GUARD";
	
	@XmlElement(name = "aggro")
	protected AggroRelations aggroRelations;
	@XmlElement(name = "friend")
	protected FriendlyRelations friendlyRelations;
	@XmlElement(name = "support")
	protected SupportRelations supportRelations;
	@XmlElement(name = "neutral")
	protected NeutralRelations neutralRelations;
	@XmlElement(name = "hostile")
	protected HostileRelations hostileRelations;
	@XmlAttribute(required = true)
	protected String name;
	@XmlAttribute
	protected String base;
	/**
	 * @return the aggroRelations
	 */
	public AggroRelations getAggroRelations()
	{
		return aggroRelations;
	}
	/**
	 * @return the sypportRelations
	 */
	public SupportRelations getSupportRelations()
	{
		return supportRelations;
	}
	/**
	 * @return the friendlyRelations
	 */
	public FriendlyRelations getFriendlyRelations()
	{
		return friendlyRelations;
	}

	/**
	 * @return the neutralRelations
	 */
	public NeutralRelations getNeutralRelations()
	{
		return neutralRelations;
	}
	/**
	 * @return the hostileRelations
	 */
	public HostileRelations getHostileRelations()
	{
		return hostileRelations;
	}
	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}
	/**
	 * @return the base
	 */
	public String getBase()
	{
		return base;
	}
}