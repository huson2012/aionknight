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

package gameserver.utils.stats;

public enum AbyssRankEnum
{
	GRADE9_SOLDIER(1, 120, 24, 0),
	GRADE8_SOLDIER(2, 168, 37, 1200),
	GRADE7_SOLDIER(3, 235, 58, 4220),
	GRADE6_SOLDIER(4, 329, 91, 10990),
	GRADE5_SOLDIER(5, 461, 143, 23500),
	GRADE4_SOLDIER(6, 645, 225, 42780),
	GRADE3_SOLDIER(7, 903, 356, 69700),
	GRADE2_SOLDIER(8, 1264, 561, 105600),
	GRADE1_SOLDIER(9, 1770, 885, 150800),
	STAR1_OFFICER(10, 2124, 1195, 214100, 1000),
	STAR2_OFFICER(11, 2549, 1616, 278700, 700),
	STAR3_OFFICER(12, 3059, 2184, 344500, 500),
	STAR4_OFFICER(13, 3671, 2949, 411700, 300),
	STAR5_OFFICER(14, 4405, 3981, 488200, 100),
	GENERAL(15, 5286, 5374, 565400, 30),
	GREAT_GENERAL(16, 6343, 7258, 643200, 10),
	COMMANDER(17, 7612, 9799, 721600, 3),
	SUPREME_COMMANDER(18, 9134, 13229, 800700, 1);
	
	private int id;
	private int pointsGained;
	private int pointsLost;		
	private int required;
	private int quota;
	
	/**
	 * 
	 * @param id
	 * @param pointsGained
	 * @param pointsLost
	 * @param required
	 */
	private AbyssRankEnum(int id, int pointsGained, int pointsLost, int required)
	{
		this.id				= id;
		this.pointsGained	= pointsGained;
		this.pointsLost		= pointsLost;
		this.required		= required;
		this.quota			= 0;
	}
	
	/**
	 * @author Divinity
	 * 
	 * @param id
	 * @param pointsGained
	 * @param pointsLost
	 * @param required
	 * @param quota
	 */
	private AbyssRankEnum(int id, int pointsGained, int pointsLost, int required, int quota)
	{
		this.id				= id;
		this.pointsGained	= pointsGained;
		this.pointsLost		= pointsLost;
		this.required		= required;
		this.quota			= quota;
	}

	/**
	 * @return the id
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * @return the pointsLost
	 */
	public int getPointsLost()
	{
		return pointsLost;
	}

	/**
	 * @return the pointsGained
	 */
	public int getPointsGained()
	{
		return pointsGained;
	}	
	
	/**
	 * @return AP required for Rank
	 */
	public int getRequired()
	{
		return required;
	}
	
	/**
	 * @return The quota is the maximum number of allowed player to have the rank
	 */
	public int getQuota()
	{
		return quota;
	}
	
	/**
	 * @param id
	 * @return The abyss rank enum by his id
	 */
	public static AbyssRankEnum getRankById(int id)
	{
		for(AbyssRankEnum rank : values())
		{
			if(rank.id == id)
				return rank;
		}
		throw new IllegalArgumentException("Invalid abyss rank provided");
	}
	
	/**
	 * @param ap
	 * @return The abyss rank enum for his needed ap
	 */
	public static AbyssRankEnum getRankForAp(int ap)
	{
		AbyssRankEnum r = AbyssRankEnum.GRADE9_SOLDIER;
		for(AbyssRankEnum rank : values())
		{
			if(rank.required <= ap)
				r = rank;
			else
				break;
		}
		return r;
	}
	
	public static AbyssRankEnum getRankForPosition(int position, int ap)
	{
		AbyssRankEnum r = getRankForAp(ap);
		if (r.quota != 0)
		{
			if(position == 0)
			{
				r = AbyssRankEnum.getRankById(AbyssRankEnum.getLastRankWithQuota().id -1);
			}
			else
			{
				while (position > r.quota)
				{
					r = AbyssRankEnum.getRankById(r.id -1);
				}
			}
		}
		return r;
	}
	
	public static AbyssRankEnum getLastRankWithQuota()
	{
		AbyssRankEnum rank = AbyssRankEnum.GRADE9_SOLDIER;
		for(int i = 18; i > 0;i--)
		{
			if (getRankById(i).quota == 0)
				break;
			
			rank = getRankById(i);
		}
		
		return rank;
	}
}
