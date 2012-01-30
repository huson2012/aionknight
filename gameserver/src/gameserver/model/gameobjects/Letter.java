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

package gameserver.model.gameobjects;

import java.sql.Timestamp;

public class Letter extends AionObject
{
	private int recipientId;
	private Item attachedItem;
	private long attachedKinahCount;
	private String senderName;
	private String title;
	private String message;
	private boolean unread;
	private boolean express;
	private Timestamp timeStamp;
	private PersistentState persistentState;
	
	/**
	 * @param objId
	 * @param attachedItem
	 * @param attachedKinah
	 * @param title
	 * @param message
	 * @param senderId 
	 * @param senderName
	 * @param timeStamp
	 * 	new letter constructor
	 */
	public Letter(int objId, int recipientId, Item attachedItem, long attachedKinahCount, String title, String message,
		String senderName, Timestamp timeStamp, boolean unread, boolean express)
	{
		super(objId);

		this.recipientId = recipientId;
		this.attachedItem = attachedItem;
		this.attachedKinahCount = attachedKinahCount;
		this.title = title;
		this.message = message;
		this.senderName = senderName;
		this.timeStamp = timeStamp;
		this.unread = unread;
		this.express = express;
		this.persistentState = PersistentState.NEW;

	}
	
	@Override
	public String getName()
	{
		return String.valueOf(attachedItem.getItemTemplate().getNameId());
	}
	
	public int getRecipientId()
	{
		return recipientId;
	}
	
	public Item getAttachedItem()
	{
		return attachedItem;
	}
	
	public long getAttachedKinah()
	{
		return attachedKinahCount;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public String getMessage()
	{
		return message;
	}
	
	public String getSenderName()
	{
		return senderName;
	}
	
	public boolean isSystemMessage()
	{
		return (senderName.startsWith("$$"));
	}
	
	public boolean isUnread()
	{
		return unread;
	}
	
	public void setReadLetter()
	{
		this.unread = false;
		this.persistentState = PersistentState.UPDATE_REQUIRED;
	}
	
	public boolean isExpress()
	{
		return express;
	}
	
	public PersistentState getLetterPersistentState()
	{
		return persistentState;
	}
	
	public void removeAttachedItem()
	{
		this.attachedItem = null;
		this.persistentState = PersistentState.UPDATE_REQUIRED;
	}
	
	public void removeAttachedKinah()
	{
		this.attachedKinahCount = 0;
		this.persistentState = PersistentState.UPDATE_REQUIRED;
	}
	
	public void delete()
	{
		this.persistentState = PersistentState.DELETED;
	}
	
	public void setPersistState(PersistentState state)
	{
		this.persistentState = state;
	}

	/**
	 * @return the timeStamp
	 */
	public Timestamp getTimeStamp()
	{
		return timeStamp;
	}
}
