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

package gameserver.utils.rates;

public abstract class Rates
{
	public abstract int getGroupXpRate();

	public abstract int getXpRate();

	public abstract float getApNpcRate();

	public abstract float getApPlayerRate();

	public abstract float getGatheringXPRate();

	public abstract float getGatheringLvlRate();

	public abstract float getCraftingXPRate();

	public abstract float getCraftingLvlRate();

	public abstract int getDropRate();

	public abstract int getChestDropRate();

	public abstract int getQuestXpRate();

	public abstract int getQuestKinahRate();

	public abstract int getKinahRate();

	public abstract int getBokerRate();

	public static Rates getRatesFor(byte membership)
	{
		switch(membership)
		{
			case 0:
				return new RegularRates();
			case 1:
				return new PremiumRates();
			case 2:
				return new VipRates();
			default:
				return new RegularRates();
		}
	}
}
