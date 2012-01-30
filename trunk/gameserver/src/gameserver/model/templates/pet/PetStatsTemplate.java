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

package gameserver.model.templates.pet;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "petstats")
public class PetStatsTemplate
{
	@XmlAttribute(name = "reaction", required = true)
	private String		reaction;
	
	@XmlAttribute(name = "run_speed", required = true)
	private float			runSpeed;
	
	@XmlAttribute(name = "walk_speed", required = true)
	private float 			walkSpeed;
	
	@XmlAttribute(name = "height", required = true)
	private float			height;
	
	@XmlAttribute(name = "altitude", required = true)
	private float			altitude;

	public String getReaction()
	{
		return reaction;
	}
	
	public float getRunSpeed()
	{
		return runSpeed;
	}
	
	public float getWalkSpeed()
	{
		return walkSpeed;
	}
	
	public float getHeight()
	{
		return height;
	}
	
	public float getAltitude()
	{
		return altitude;
	}
	
	void afterUnmarshal (Unmarshaller u, Object parent)
	{
		
	}
}
