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
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "pet")
public class PetTemplate
{
	@XmlAttribute(name = "id", required = true)
	private int			id;
	
	@XmlAttribute(name = "name", required = true)
	private String		name;
	
	@XmlAttribute(name = "nameid", required = true)
	private int			nameId;
	
	@XmlAttribute(name = "eggid", required = false)
	private int			eggId;
	
	@XmlElement(name = "petstats", required = true)
	private PetStatsTemplate statsTemplate;
	
	@XmlElement(name = "petfunction")
	private List<PetFunctionTemplate> functionTemplates;

	public int getPetId()
	{
		return id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getNameId()
	{
		return nameId;
	}
	
	public int getEggId()
	{
		return eggId;
	}
	
	public PetStatsTemplate getStatsTemplate()
	{
		return statsTemplate;
	}
	
	public List<PetFunctionTemplate> getFunctionTemplates()
	{
		if (functionTemplates == null)
			return new ArrayList<PetFunctionTemplate>();
		return functionTemplates;
	}
	
	public int getFoodFlavourId()
	{
		for (PetFunctionTemplate t : getFunctionTemplates())
		{
			if ("food".equals(t.getType()))
				return t.getId();
		}
		return 0;
	}
	
	void afterUnmarshal (Unmarshaller u, Object parent)
	{
		
	}
}
