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

import commons.utils.Rnd;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PetRewards", propOrder = { "results" })
@XmlSeeAlso({ gameserver.model.templates.pet.PetFlavour.Food.class })

public class PetRewards 
{
	@XmlElement(name="result")
    protected List<PetRewards.Result> results;
    
    @XmlAttribute(name = "type", required = true)
    protected FoodType type;
    
    @XmlAttribute(name = "loved")
    protected boolean loved = false;

    public List<PetRewards.Result> getResults()
    {
        if (results == null)
            results = new ArrayList<PetRewards.Result>();
        return this.results;
    }
    
    /**
     * Returns results with price = -1 (additionally rewarded, like during events)
     */
    public List<PetRewardDescription> getAdditionalRewards()
    {
    	List<PetRewardDescription> results = new ArrayList<PetRewardDescription>();
    	for (PetRewardDescription descr : getResults())
    	{
    		if (descr.getPrice() == -1)
    			results.add(descr);
    	}
    	return results;
    }
    
    public PetRewardDescription getRandomReward()
    {
    	for (PetRewardDescription descr : getResults())
    	{
    		if (descr.getChance() == 0)
    			continue;
   			if(Rnd.get(100) <= descr.getChance())
    			return descr;
    	}
    	return null;
    }    

    public FoodType getType()
    {
        return type;
    }

    public boolean isLoved() 
    {
        return loved;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Result extends PetRewardDescription
    {
    }
}
