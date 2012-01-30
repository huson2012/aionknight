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

import gameserver.model.Race;
import gameserver.model.templates.recipe.RecipeTemplate;
import gameserver.skill.model.learn.SkillRace;
import gnu.trove.TIntObjectHashMap;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "recipe_templates")
@XmlAccessorType(XmlAccessType.FIELD)
public class RecipeData
{
	@XmlElement(name = "recipe_template")
	protected List<RecipeTemplate> list;
	private TIntObjectHashMap<RecipeTemplate> recipeData;
	private final TIntObjectHashMap<ArrayList<RecipeTemplate>> learnTemplates = new TIntObjectHashMap<ArrayList<RecipeTemplate>>();

	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		recipeData = new TIntObjectHashMap<RecipeTemplate>();
		for(RecipeTemplate it : list)
		{
			recipeData.put(it.getId(), it);
			if (it.getAutolearn() == 0)
				continue;
			addTemplate(it);
		}
		list = null;
	}

	public RecipeTemplate getRecipeTemplateById(int id)
	{
		return recipeData.get(id);
	}

	private void addTemplate(RecipeTemplate template)
	{	
		SkillRace race = template.getRace();
		if(race == null)
			race = SkillRace.ALL;

		int hash = makeHash(race.ordinal(), template.getSkillid(), template.getSkillpoint());
		ArrayList<RecipeTemplate> value = learnTemplates.get(hash);
		if(value == null)
		{
			value = new ArrayList<RecipeTemplate>();
			learnTemplates.put(hash, value);
		}
			
		value.add(template);
	}

	public RecipeTemplate[] getRecipeIdFor(Race race, int skillId, int skillPoint)
	{
		List<RecipeTemplate> newRecipes = new ArrayList<RecipeTemplate>();
		List<RecipeTemplate> raceSpecificTemplates = 
			learnTemplates.get(makeHash(race.ordinal(), skillId, skillPoint));
		List<RecipeTemplate> allRaceSpecificTemplates = 
			learnTemplates.get(makeHash(SkillRace.ALL.ordinal(), skillId, skillPoint));
		
		if (raceSpecificTemplates != null)
			newRecipes.addAll(raceSpecificTemplates);
		if (allRaceSpecificTemplates != null)
			newRecipes.addAll(allRaceSpecificTemplates);

		return newRecipes.toArray(new RecipeTemplate[newRecipes.size()]);
	}

	public int size()
	{
		return recipeData.size();
	}
	
	private static int makeHash(int race, int skillId, int skillLevel)
	{
		int result = race << 8;
        result = (result | skillId) << 8;
        return result | skillLevel;
	}
}