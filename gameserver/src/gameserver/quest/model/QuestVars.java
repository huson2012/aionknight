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

package gameserver.quest.model;

public class QuestVars
{
	private Integer[]	questVars	= new Integer[6];

	public QuestVars()
	{
	}

	public QuestVars(int var)
	{
		setVar(var);
	}

	/**
	 * @param id
	 * @return Quest var by id.
	 */
	public int getVarById(int id)
	{
		if(id == 5)
			return (questVars[id] & 0x03);
		return questVars[id];
	}

	/**
	 * @param id
	 * @param var
	 */
	public void setVarById(int id, int var)
	{
		if(id == 5)
			questVars[id] = (var & 0x03);
		else
			questVars[id] = (var & 0x3F);
	}

	/**
	 * @return integer
	 */
	public int getQuestVars()
	{
		int var = 0;
		var |= questVars[5];
		for(int i = 5; i >= 0; i--)
		{
			if(i == 5)
				var <<= 0x02;
			else
				var <<= 0x06;
			var |= questVars[i];
		}
		return var;
	}
	
	public void setVar(int var)
	{
		for(int i = 0; i < 6; i++)
		{
			if(i == 5)
				questVars[i] = (var & 0x03);
			else
			{
				questVars[i] = (var & 0x3F);
				var >>= 0x06;
			}
		}
	}
}
