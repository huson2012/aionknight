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

import java.util.Map;
import commons.database.dao.DAO;
import gameserver.model.siege.SiegeLocation;

public abstract class SiegeDAO implements DAO
{
	@Override
	public final String getClassName()
	{
		return SiegeDAO.class.getName();
	}
	
	public abstract boolean loadSiegeLocations(Map<Integer, SiegeLocation> locations);
	public abstract boolean updateSiegeLocation(SiegeLocation siegeLocation);
	public abstract void insertSiegeLogEntry(String legionName, String action, long tstamp, int locationId);
	
}