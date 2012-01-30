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

package gameserver.skill.model;

import javolution.util.FastList;

public enum PreeffectsMasks
{
	NONE(0),
	e1(1),
	e2(2),
	e1_2(3),
	e3(4),
	e1_2_3(7);
	

	private PreeffectsMasks(int mask)
	{
	}
	
	public static FastList<Integer> getPositions(int mask)
	{
		FastList<Integer> positions = new FastList<Integer>();
		
		switch(mask)
		{
			case 1:
				positions.add(1);
				break;
			case 2:
				positions.add(2);
				break;
			case 3:
				positions.add(1);
				positions.add(2);
				break;
			case 4:
				positions.add(3);
				break;
			case 7:
				positions.add(1);
				positions.add(2);
				positions.add(3);
				break;
			default:
				return null;
		}
		
		return positions;
	}
}