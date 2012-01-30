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

package gameserver.dao;

import gameserver.model.legion.Legion;
import gameserver.model.legion.LegionEmblem;
import gameserver.model.legion.LegionHistory;
import gameserver.model.legion.LegionWarehouse;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * Class that is responsible for storing/loading legion data
 */

public abstract class LegionDAO implements IDFactoryAwareDAO
{
	/**
	 * Returns true if name is used, false in other case
	 * @param name name to check
	 * @return true if name is used, false in other case
	 */
	public abstract boolean isNameUsed(String name);

	/**
	 * Creates legion in DB
	 * @param legion
	 */
	public abstract void saveNewLegion(Legion legion);

	/**
	 * Stores legion to DB
	 * @param legion
	 */
	public abstract void storeLegion(Legion legion);

	/**
	 * Loads a legion
	 * @param legionName
	 * @return
	 */
	public abstract Legion loadLegion(String legionName);

	/**
	 * Loads a legion
	 * @param legionId
	 * @return Legion
	 */
	public abstract Legion loadLegion(int legionId);

	/**
	 * Removes legion and all related data (Done by CASCADE DELETION)
	 * @param legionId legion to delete
	 */
	public abstract void deleteLegion(int legionId);

	/**
	 * Returns the announcement list of a legion
	 * @param legion
	 * @return announcementList
	 */
	public abstract TreeMap<Timestamp, String> loadAnnouncementList(int legionId);

	/**
	 * Creates announcement in DB
	 * @param legionId
	 * @param currentTime 
	 * @param message
	 * @return true or false
	 */
	public abstract void saveNewAnnouncement(int legionId, Timestamp currentTime, String message);

	/**
	 * Identifier name for all LegionDAO classes
	 * @return LegionDAO.class.getName()
	 */
	@Override
	public final String getClassName()
	{
		return LegionDAO.class.getName();
	}

	/**
	 * Stores a legion emblem in the database
	 * @param legionId
	 * @param emblemId
	 * @param red
	 * @param green
	 * @param blue
	 */
	public abstract void storeLegionEmblem(int legionId, LegionEmblem legionEmblem);

	/**
	 * @param legionId
	 * @param key
	 * @return
	 */
	public abstract void removeAnnouncement(int legionId, Timestamp key);

	/**
	 * Loads a legion emblem
	 * @param legion
	 * @return LegionEmblem
	 */
	public abstract LegionEmblem loadLegionEmblem(int legionId);
	
	/**
	 * Loads the warehouse of legions
	 * @param legion
	 * @return Storage
	 */
	public abstract LegionWarehouse loadLegionStorage(Legion legion);

	/**
	 * @return the legion ranking
	 */
	public abstract HashMap<Integer, Integer> loadLegionRanking();

	/**
	 * @param legion
	 */
	public abstract void loadLegionHistory(Legion legion);
	
	/**
	 * @param legionId
	 * @param legionHistory
	 * @return true if query successful
	 */
	public abstract void saveNewLegionHistory(int legionId, LegionHistory legionHistory);
}