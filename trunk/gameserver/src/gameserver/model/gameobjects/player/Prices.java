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

import gameserver.GameServer;
import gameserver.configs.main.PricesConfig;
import gameserver.configs.main.SiegeConfig;
import gameserver.model.Race;
import gameserver.model.siege.Influence;

public class Prices
{
	public Prices()
	{
		
	}
	
	/**
	 * Used in SM_PRICES
	 * @return buyingPrice
	 */
	public int getGlobalPrices(Race playerRace)
	{
		int defaultPrices = PricesConfig.DEFAULT_PRICES;
		
		if(!SiegeConfig.SIEGE_ENABLED)
			return defaultPrices;
		
		int computedPrice = defaultPrices;
		double ratio = GameServer.getRatiosFor(playerRace);
		if(ratio < 50.0)
			computedPrice = (int) Math.round(defaultPrices - ((50 - ratio)/2));
		else if(ratio > 50.0)
			computedPrice = (int) Math.round(((ratio - 50)/2) + defaultPrices);
		
		return computedPrice;

	}

	/**
	 * Used in SM_PRICES
	 * @return
	 */
	public int getGlobalPricesModifier()
	{
		return PricesConfig.DEFAULT_MODIFIER;
	}

	/**
	 * Used in SM_PRICES
	 * @return taxes
	 */
	public int getTaxes(Race playerRace)
	{
		int defaultTax = PricesConfig.DEFAULT_TAXES;
		
		if(!SiegeConfig.SIEGE_ENABLED)
			return defaultTax;
		
		float influenceValue = 0;
		switch(playerRace)
		{
			case ASMODIANS: influenceValue = Influence.getInstance().getAsmos(); break;
			case ELYOS: influenceValue = Influence.getInstance().getElyos(); break;
			default: influenceValue = 0.5f; break;
		}
		return defaultTax + (Math.round(10 - (influenceValue * 10)));
	}
	
	/**
	 * Used in SM_TRADELIST.
	 * @return buyPriceModifier
	 */
	public int getVendorBuyModifier()
	{
		return PricesConfig.VENDOR_BUY_MODIFIER;
	}

	/**
	 * Used in SM_SELL_ITEM
	 * - Can be unique per NPC!
	 * @return sellingModifier
	 */
	public int getVendorSellModifier(Race playerRace)
	{
		return (int)(PricesConfig.VENDOR_SELL_MODIFIER);
	}

	/**
	 * @param basePrice
	 * @return modifiedPrice
	 */
	public long getPriceForService(long basePrice, Race playerRace)
	{
		// Tricky. Requires multiplication by Prices, Modifier, Taxes
		// In order, and round down each time to match client calculation.
		return (long)((long)((long)(basePrice *
			this.getGlobalPrices(playerRace) / 100D) *
			this.getGlobalPricesModifier() / 100D) *
			this.getTaxes(playerRace) / 100D);
	}

	/**
	 * @param requiredKinah
	 * @return modified requiredKinah
	 */
	public long getKinahForBuy(long requiredKinah, Race playerRace)
	{
		// Requires double precision for 2mil+ kinah items
		return (long)((long)((long)((long)(requiredKinah *
			this.getVendorBuyModifier() / 100.0D) *
			this.getGlobalPrices(playerRace) / 100.0D) *
			this.getGlobalPricesModifier() / 100.0D) *
			this.getTaxes(playerRace) / 100.0D);
	}

	/**
	 * @param kinahReward
	 * @return
	 */
	public long getKinahForSell(long kinahReward, Race playerRace)
	{
		return (long)(kinahReward * this.getVendorSellModifier(playerRace) / 100D);
	}

}
