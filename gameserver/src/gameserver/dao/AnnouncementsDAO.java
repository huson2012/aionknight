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

import java.util.Set;
import commons.database.dao.DAO;
import gameserver.model.Announcement;
/**
 * DAO that manages Announcements
 */
public abstract class AnnouncementsDAO implements DAO
{
	public abstract Set<Announcement> getAnnouncements();
	public abstract void addAnnouncement(final Announcement announce);
	public abstract boolean delAnnouncement(final int idAnnounce);

	/**
	 * Returns class name that will be uses as unique identifier for all DAO classes
	 * 
	 * @return class name
	 */
	@Override
	public final String getClassName()
	{
		return AnnouncementsDAO.class.getName();
	}
}
