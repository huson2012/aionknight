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

import gameserver.model.gameobjects.PersistentState;

public class PlayerSettings
{
	private PersistentState persistentState;

	private byte[] uiSettings;
	private byte[] shortcuts;
	private int deny = 0;
	private int display = 0;

	public PlayerSettings()
	{

	}

	public PlayerSettings(byte[] uiSettings, byte[] shortcuts, int deny, int display)
	{
		this.uiSettings = uiSettings;
		this.shortcuts = shortcuts;
		this.deny = deny;
		this.display = display;
	}

	/**
	 * @return the persistentState
	 */
	public PersistentState getPersistentState()
	{
		return persistentState;
	}

	/**
	 * @param persistentState the persistentState to set
	 */
	public void setPersistentState(PersistentState persistentState)
	{
		this.persistentState = persistentState;
	}

	/**
	 * @return the uiSettings
	 */
	public byte[] getUiSettings()
	{
		return uiSettings;
	}

	/**
	 * @param uiSettings the uiSettings to set
	 */
	public void setUiSettings(byte[] uiSettings)
	{
		this.uiSettings = uiSettings;
		persistentState = PersistentState.UPDATE_REQUIRED;
	}

	/**
	 * @return the shortcuts
	 */
	public byte[] getShortcuts()
	{
		return shortcuts;
	}

	/**
	 * @param shortcuts the shortcuts to set
	 */
	public void setShortcuts(byte[] shortcuts)
	{
		this.shortcuts = shortcuts;
		persistentState = PersistentState.UPDATE_REQUIRED;
	}

	/**
	 * @return the display
	 */
	public int getDisplay()
	{
		return display;
	}

	/**
	 * @param display the display to set
	 */
	public void setDisplay(int display)
	{
		this.display = display;
		persistentState = PersistentState.UPDATE_REQUIRED;
	}

	/**
	 * @return the deny
	 */
	public int getDeny()
	{
		return deny;
	}

	/**
	 * @param deny the deny to set
	 */
	public void setDeny(int deny)
	{
		this.deny = deny;
		persistentState = PersistentState.UPDATE_REQUIRED;
	}

	public boolean isInDeniedStatus(DeniedStatus deny)
	{
		int isDeniedStatus = this.deny & deny.getId();

        return isDeniedStatus == deny.getId();

    }
}
