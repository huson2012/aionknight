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

package gameserver.model;

public class AbyssRankingResult
{
	private String playerName;
	private int ap;
	private int abyssRank;
	private int topRanking;
	private int oldRanking;
	private PlayerClass playerClass;
	private int playerLevel;
	private int playerId;
	private final String legionName;
	private int cp;
	private int legionId;
	private int legionLevel;
	private int legionMembers;
	private int rank;
	private int oldRank;
	
	public AbyssRankingResult(String playerName, int playerId, int ap, int abyssRank, int topRanking, 
	int oldRanking, PlayerClass playerClass, int playerLevel, String legionName)
	{
		this.playerName = playerName;
		this.playerId = playerId;
		this.ap = ap;
		this.abyssRank = abyssRank;
		this.topRanking = topRanking;
		this.oldRanking = oldRanking;
		this.playerClass = playerClass;
		this.playerLevel = playerLevel;
		this.legionName = legionName;
	}
	
	public AbyssRankingResult(int cp, String legionName, int legionId, int legionLevel, int legionMembers, 
	int rank, int oldRank)
	{
		this.cp = cp;
		this.legionName = legionName;
		this.legionId = legionId;
		this.legionLevel = legionLevel;
		this.legionMembers = legionMembers;
		this.rank = rank;
		this.oldRank = oldRank;
	}
	
	public String getPlayerName()
	{
		return playerName;
	}
	
	public int getPlayerId()
	{
		return playerId;
	}
	
	public int getPlayerAP()
	{
		return ap;
	}
	
	public int getPlayerRank()
	{
		return abyssRank;
	}
	
	public int getTopRanking()
	{
		return topRanking;
	}
	
	public int getOldRanking()
	{
		return oldRanking;
	}
	
	public int getPlayerLevel()
	{
		return playerLevel;
	}

	public PlayerClass getPlayerClass()
	{
		return playerClass;
	}
	
	public String getLegionName()
	{
		return legionName;
	}
	
	public int getLegionCP()
	{
		return cp;
	}
	
	public int getLegionId()
	{
		return legionId;
	}
	
	public int getLegionLevel()
	{
		return legionLevel;
	}
	
	public int getLegionMembers()
	{
		return legionMembers;
	}
	
	public int getLegionRank()
	{
		return rank;
	}
	public int getLegionOldRank()
	{
		return oldRank;
	}
}