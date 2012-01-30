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

import gameserver.model.templates.pet.FoodGroups;
import gameserver.model.templates.pet.FoodType;
import gameserver.model.templates.pet.PetFlavour;
import gameserver.model.templates.pet.PetRewards;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "foodGroups", "flavours" })
@XmlRootElement(name = "pet_feed")

public class PetFeedData
{
	@XmlElement(name="groups", required = true)
	protected FoodGroups foodGroups;
	
	@XmlElement(name="flavour", required = true)
	protected List<PetFeedData.Flavour> flavours;

	public FoodGroups getFoodGroups() 
	{
		return foodGroups;
	}

	public List<PetFeedData.Flavour> getFlavours() 
	{
		if (flavours == null)
			flavours = new ArrayList<PetFeedData.Flavour>();

		return this.flavours;
	}

	public synchronized PetFlavour getFlavour(int flavour, int feedItemId)
	{
		List<FoodType> foodTypes = foodGroups.getFoodTypes(feedItemId);
		if (foodTypes.isEmpty() || PetFlavour.isReward(feedItemId))
			return null;

		for (PetFlavour f : flavours)
		{
			if (f.getId() != flavour)
				continue;
			
			List<PetRewards> rewards = null;
			for (FoodType foodType : foodTypes)
			{
				rewards = f.getRewards(foodType);
				if (!rewards.isEmpty() && !rewards.get(0).getResults().isEmpty())
					return f;
			}
		}
		
		return null;
	}

	public int size()
	{
		return getFlavours().size();
	}
	
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "")
	public static class Flavour extends PetFlavour
	{
	}
}