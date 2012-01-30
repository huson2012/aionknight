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

import commons.database.dao.DAO;

public abstract class PlayerPasskeyDAO implements DAO
{
	/**
	* @param accountId
	* @param passkey
	*/
	public abstract void insertPlayerPasskey(int accountId, String passkey);

	/**
	* @param accountId
	* @param oldPasskey
	* @param newPasskey
	* @return
	*/
	public abstract boolean updatePlayerPasskey(int accountId, String oldPasskey, String newPasskey);

	/**
	* @param accountId
	* @param newPasskey
	* @return
	*/
	public abstract boolean updateForcePlayerPasskey(int accountId, String newPasskey);

	/**
	* @param accountId
	* @param passkey
	* @return
	*/
	public abstract boolean checkPlayerPasskey(int accountId, String passkey);

	/**
	* @param accountId
	* @return
	*/
	public abstract boolean existCheckPlayerPasskey(int accountId);

	/**
	* (non-Javadoc)
	* @see commons.database.dao.DAO#getClassName()
	*/
	@Override
	public final String getClassName()
	{
		return PlayerPasskeyDAO.class.getName();
	}
}