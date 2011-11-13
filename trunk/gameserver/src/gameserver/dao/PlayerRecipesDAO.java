/**
 * This file is part of Aion-Knight <aionu-unique.org>.
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */

package gameserver.dao;

import commons.database.dao.DAO;
import gameserver.model.gameobjects.player.RecipeList;

public abstract class PlayerRecipesDAO implements DAO
{
	@Override
	public String getClassName()
	{
		return PlayerRecipesDAO.class.getName();	
	}

	public abstract RecipeList load(final int playerId);
	public abstract void addRecipe(final int playerId, final int recipeId);	
	public abstract void deleteRecipe(final int playerId, final int recipeId);
}