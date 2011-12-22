/**   
 * Эмулятор игрового сервера Aion 2.7 от команды разработчиков 'Aion-Knight Dev. Team' является 
 * свободным программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного программного 
 * обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой более поздней 
 * версии.
 * 
 * Программа распространяется в надежде, что она будет полезной, но БЕЗ КАКИХ БЫ ТО НИ БЫЛО 
 * ГАРАНТИЙНЫХ ОБЯЗАТЕЛЬСТВ; даже без косвенных  гарантийных  обязательств, связанных с 
 * ПОТРЕБИТЕЛЬСКИМИ СВОЙСТВАМИ и ПРИГОДНОСТЬЮ ДЛЯ ОПРЕДЕЛЕННЫХ ЦЕЛЕЙ. Для подробностей смотрите 
 * Стандартную Общественную Лицензию GNU.
 * 
 * Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе с этой программой. 
 * Если это не так, напишите в Фонд Свободного ПО (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * Веб-cайт разработчиков : http://aion-knight.ru
 * Поддержка клиента игры : Aion 2.7 - 'Арена Смерти' (Иннова) 
 * Версия серверной части : Aion-Knight 2.7 (Beta version)
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