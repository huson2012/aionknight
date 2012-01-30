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
import java.sql.Timestamp;

public class Guild
{
	private int guildId;
	private int lastQuest;
	private Timestamp completeTime;
	private int currentQuest;
	
	private PersistentState persistentState;


    public Guild(int guildId, int lastQuest, Timestamp completeTime, int currentQuest)
	{
		super();
		this.guildId = guildId;
		this.lastQuest = lastQuest;
		this.completeTime = completeTime;
		this.currentQuest = currentQuest;
	}
	/**
	 * @return the guildId
	 */
	public int getGuildId()
	{
		return guildId;
	}
	
	/**
	 * @param guildId the guildId to set
	 */
	public void setGuildId(int guildId)
	{
		this.guildId = guildId;
		setPersistentState(PersistentState.UPDATE_REQUIRED);
	}
	
	/**
	 * @return lastQuest
	 */
	public int getLastQuest()
	{
		return lastQuest;
	}
	
	/**
	 * @param lastQuest the lastQuest to set
	 */
	public void setLastQuest(int lastQuest)
	{
		this.lastQuest = lastQuest;
		setPersistentState(PersistentState.UPDATE_REQUIRED);
	}
	
	/**
	 * @return completeTime
	 */
	public Timestamp getCompleteTime()
	{
		return completeTime;
	}
	
	/**
	 * @param completeTime the completeTime to set
	 */
	public void setCompleteTime(Timestamp completeTime)
	{
		this.completeTime = completeTime;
		setPersistentState(PersistentState.UPDATE_REQUIRED);
	}
	
	/**
	 * @return currentQuest
	 */
	public int getCurrentQuest()
	{
		return currentQuest;
	}
	
	/**
	 * @param currentQuest the currentQuest to set
	 */
	public void setCurrentQuest(int currentQuest)
	{
		this.currentQuest = currentQuest;
		setPersistentState(PersistentState.UPDATE_REQUIRED);
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
		switch(persistentState)
		{
			case UPDATE_REQUIRED:
				if(this.persistentState == PersistentState.NEW)
					break;
			default:
				this.persistentState = persistentState;
		}
	}	
}
