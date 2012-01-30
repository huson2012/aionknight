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

import gameserver.model.templates.pet.PetTemplate;
import gnu.trove.TIntObjectHashMap;
import gnu.trove.TIntObjectIterator;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "pets")
@XmlAccessorType(XmlAccessType.FIELD)
public class PetData
{
	@XmlElement(name="pet")
	private List<PetTemplate> pts;
	
	private TIntObjectHashMap<PetTemplate> templates;
	
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		templates = new TIntObjectHashMap<PetTemplate>();
		for(PetTemplate pt: pts)
		{
			templates.put(pt.getPetId(), pt);
		}
		pts = null;
	}
	
	public PetTemplate getPetTemplate(int petId)
	{
		return templates.get(petId);
	}
	
	public PetTemplate getPetTemplateByEggId(int eggId)
	{
		for (TIntObjectIterator<PetTemplate> it = templates.iterator();it.hasNext();)
		{
			it.advance();
			if (it.value().getEggId()==eggId)
				return it.value();
		}
		return null;
	}

	public int size()
	{
		return templates.size();
	}
}