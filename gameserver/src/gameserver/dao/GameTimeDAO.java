/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
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

public abstract class GameTimeDAO implements DAO
{
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getClassName()
	{
		return GameTimeDAO.class.getName();
	}

	/**
	 * Loads the game time stored in the database
	 * @returns Time stored in database
	 */
	public abstract int load();
	
	/**
	 * Stores the given time in the database as the GameTime
	 */
	public abstract boolean store(int time);
}