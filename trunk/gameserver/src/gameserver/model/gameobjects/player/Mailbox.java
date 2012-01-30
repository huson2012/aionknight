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

import gameserver.model.gameobjects.Letter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Mailbox
{
	private Map<Integer, Letter> mails = new ConcurrentHashMap<Integer, Letter>();
	private int sendMailLimit = 0;

	/**
	 * @param letter
	 */
	public void putLetterToMailbox(Letter letter)
	{
		mails.put(letter.getObjectId(), letter);
	}

	/**
	 * Get all letters in mailbox (sorted according to time received)
	 * 
	 * @return
	 */
	public Collection<Letter> getLetters()
	{
		SortedSet<Letter> letters = new TreeSet<Letter>(new Comparator<Letter>(){

			@Override
			public int compare(Letter o1, Letter o2)
			{
				if(o1.getTimeStamp().getTime() > o2.getTimeStamp().getTime())
					return -1;
				if(o1.getTimeStamp().getTime() < o2.getTimeStamp().getTime())
					return 1;

				return o1.getObjectId() > o2.getObjectId() ? 1 : -1;
			}

		});

		for(Letter letter : mails.values())
		{
			if(letters.size() >= 98)
				break;

			letters.add(letter);
		}
		return letters;
	}

	/**
	 * Get letter with specified letter id
	 * 
	 * @param letterObjId
	 * @return
	 */
	public Letter getLetterFromMailbox(int letterObjId)
	{
		return mails.get(letterObjId);
	}

	/**
	 * Check whether mailbox contains empty letters
	 * 
	 * @return
	 */
	public boolean haveUnread()
	{
		for(Letter letter : mails.values())
		{
			if(letter.isUnread())
				return true;
		}

		return false;
	}

	/**
	 * 
	 * @return
	 */
	public int getFreeSlots()
	{
		return 65536 - mails.size();
	}

	/**
	 *
	 * @return
	 */
	public boolean canSendMail()
	{
		if(sendMailLimit >= 10)
			return false;

		sendMailLimit++;

		return true;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean haveFreeSlots()
	{
		return mails.size() < 100;
	}

	/**
	 * @param letterId
	 */
	public void removeLetter(int letterId)
	{
		mails.remove(letterId);
	}

	/**
	 * Current size of mailbox
	 * 
	 * @return
	 */
	public int size()
	{
		return mails.size();
	}
}