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

package gameserver.model.gameobjects.player;

import commons.database.dao.DAOManager;
import gameserver.dao.PlayerRecipesDAO;
import gameserver.dataholders.DataManager;
import gameserver.model.DescriptionId;
import gameserver.model.templates.recipe.RecipeTemplate;
import gameserver.network.aion.serverpackets.SM_LEARN_RECIPE;
import gameserver.network.aion.serverpackets.SM_RECIPE_DELETE;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.utils.PacketSendUtility;
import java.util.HashSet;
import java.util.Set;

public class RecipeList
{
	private Set<Integer> recipeList = new HashSet<Integer>();
	
	public RecipeList (HashSet<Integer> recipeList)
	{
		this.recipeList = recipeList;
	}
	
	public Set<Integer> getRecipeList()
	{
		return recipeList;
	}

	public void addRecipe(Player player, RecipeTemplate recipeTemplate)
	{
		int recipeId = recipeTemplate.getId();
		if (!recipeList.contains(recipeId))
		{
			recipeList.add(recipeId);
			DAOManager.getDAO(PlayerRecipesDAO.class).addRecipe(player.getObjectId(), recipeId);
			PacketSendUtility.sendPacket(player, new SM_LEARN_RECIPE(recipeId));
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.CRAFT_RECIPE_LEARN(new DescriptionId(recipeTemplate.getNameid())));
		}
	}

	public void deleteRecipe(Player player, int recipeId)
	{
		if (recipeList.contains(recipeId))
		{
			recipeList.remove(recipeId);
			DAOManager.getDAO(PlayerRecipesDAO.class).deleteRecipe(player.getObjectId(), recipeId);
			PacketSendUtility.sendPacket(player, new SM_RECIPE_DELETE(recipeId));
		}
	}

	public void autoLearnRecipe (Player player, int skillId, int skillLvl)
	{
		for (RecipeTemplate recipe : DataManager.RECIPE_DATA.getRecipeIdFor(player.getCommonData().getRace(), skillId, skillLvl))
		{
			player.getRecipeList().addRecipe(player, recipe);
		}
	}

	public boolean isRecipePresent(int recipeId)
	{
		return recipeList.contains(recipeId);
	}
}
