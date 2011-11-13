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
import gameserver.model.gameobjects.player.Player;

/**
 * @author Mugen 
 */
public abstract class PlayerMotionDAO implements DAO
{
	/**
	* @param playerId
	*/
	public abstract void insertPlayerMotion(int playerId);

	/**
	* @param learnNinja
	* @param learnHober
	* @param player
	* @return
	*/
	public abstract void updatePlayerMotion(int learnNinja, int learnHober, Player player);

	/**
	* @param learnNinja
	* @param learnHober
	* @param waitingMotion
	* @param runningMotion
	* @param jumpingMotion
	* @param restMotion
	* @param player
	* @return
	*/
	public abstract boolean updatePlayerMotion(int learnNinja, int learnHober, int waitingMotion, int runningMotion, int jumpingMotion, int restMotion, Player player);

	/**
	* @param player
	* @return
	*/
	public abstract void loadPlayerMotion(Player player);

	/**
	* (non-Javadoc)
	* @see commons.database.dao.DAO#getClassName()
	*/
	@Override
	public final String getClassName()
	{
		return PlayerMotionDAO.class.getName();
	}
}