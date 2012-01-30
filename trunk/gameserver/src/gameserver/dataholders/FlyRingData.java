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

package gameserver.dataholders;

import gameserver.model.templates.flyring.FlyRingTemplate;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@XmlRootElement(name = "fly_rings")
@XmlAccessorType(XmlAccessType.FIELD)
public class FlyRingData
{
	@XmlElement(name = "fly_ring")
	private List<FlyRingTemplate> flyRingTemplates;
	
	public int size ()
	{
		if (flyRingTemplates == null)
		{
			flyRingTemplates = new ArrayList<FlyRingTemplate>();
			return 0;
		}
		return flyRingTemplates.size();
	}

	public List<FlyRingTemplate> getFlyRingTemplates()
	{
		if (flyRingTemplates == null)
		{
			return new ArrayList<FlyRingTemplate>();
		}
		return flyRingTemplates;
	}
	
	public void addAll (Collection<FlyRingTemplate> templates)
	{
		if (flyRingTemplates == null)
		{
			flyRingTemplates = new ArrayList<FlyRingTemplate> ();
		}
		flyRingTemplates.addAll(templates);
	}
}