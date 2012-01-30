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

package gameserver.model.templates;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ObjectLocation")
public class ObjectLocation
{		
	@XmlAttribute(name = "xe", required = true)
	private float	xe;
	@XmlAttribute(name = "ye", required = true)
	private float	ye;
	@XmlAttribute(name = "ze", required = true)
	private float	ze;
	
	@XmlAttribute(name = "he", required = true)
	private byte	he;
	
	@XmlAttribute(name = "xa", required = true)
	private float	xa;
	@XmlAttribute(name = "ya", required = true)
	private float	ya;
	@XmlAttribute(name = "za", required = true)
	private float	za;
	
	@XmlAttribute(name = "ha", required = true)
	private byte	ha;

	public float getXe()
	{
		return xe;
	}

	public float getYe()
	{
		return ye;
	}

	public float getZe()
	{
		return ze;
	}

	public byte getHe()
	{
		return he;
	}

	public float getXa()
	{
		return xa;
	}

	public float getYa()
	{
		return ya;
	}

	public float getZa()
	{
		return za;
	}

	public byte getHa()
	{
		return ha;
	}
}