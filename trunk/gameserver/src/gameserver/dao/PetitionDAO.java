/**
 *
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
import gameserver.model.Petition;

import java.util.Set;

public abstract class PetitionDAO implements DAO
{
	public abstract int getNextAvailableId();
	public abstract void insertPetition(Petition p);
	public abstract void deletePetition(int playerObjId);
	public abstract Set<Petition> getPetitions();
	public abstract Petition getPetitionById(int petitionId);	
	public abstract void setReplied(int petitionId);
	
	@Override
	public final String getClassName()
	{
		return PetitionDAO.class.getName();
	}
}