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

import gameserver.dataholders.DataManager;
import gameserver.model.templates.item.ItemQuality;
import gameserver.model.templates.item.ItemTemplate;
import gnu.trove.TIntObjectHashMap;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FoodGroups", propOrder = { "groups" })

public class FoodGroups 
{
    private static final Pattern COMPILE = Pattern.compile(",");
    @XmlElement(name="group")
	protected List<Group> groups;

	@XmlTransient
	TIntObjectHashMap<Set<FoodType>> allFood;
	
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		allFood = new TIntObjectHashMap<Set<FoodType>>();

		for (Group group : this.groups)
		{
			String[] ids = COMPILE.split(group.getValue());

            for (String id : ids)
            {
                try
                {
                    int value = Integer.parseInt(id);
                    Set<FoodType> set;
                    if (allFood.containsKey(value))
                    {
                        set = allFood.get(value);
                    }
                    else
                    {
                        set = new HashSet<FoodType>();
                        allFood.put(value, set);
                    }

                    set.add(group.getType());
                }
                catch (Exception e)
                {

                }
            }
		}
		
		this.groups.clear();
		this.groups = null;
	}

	/**
	 * returns Food groups in which item id was defined
	 */
	public List<FoodType> getFoodTypes(int itemId)
	{
		List<FoodType> list = new ArrayList<FoodType>();
		if (allFood.containsKey(itemId))
		{
			list.addAll(allFood.get(itemId));
		}
		
		ItemTemplate template = DataManager.ITEM_DATA.getItemTemplate(itemId);
		if (template.getItemQuality() == ItemQuality.JUNK)
		{
			if (template.getItemQuestId() == 0)
			{
				// MISC items not listed
				list.add(FoodType.MISC);
				return list;
			}
		}

		return list;
	}
	
	public 
	
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "value" })
	static class Group 
	{
		@XmlValue
		protected String value;

		@XmlAttribute(name = "type", required = true)
		protected FoodType type;

		public String getValue()
		{
			return value;
		}

		public FoodType getType() 
		{
			return type;
		}
	}

}
