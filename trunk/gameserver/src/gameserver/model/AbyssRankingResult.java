/**
 * �������� �������� ������� Aion 2.7 �� ������� ������������� 'Aion-Knight Dev. Team' �������� 
 * ��������� ����������� ������������; �� ������ �������������� �/��� �������� ��� �������� �������� 
 * ����������� ������������ �������� GNU (GNU GPL), �������������� ������ ���������� ������������ 
 * ����������� (FSF), ���� �������� ������ 3, ���� (�� ���� ����������) ����� ����� ������� 
 * ������.
 * 
 * ��������� ���������������� � �������, ��� ��� ����� ��������, �� ��� ����� �� �� �� ���� 
 * ����������� ������������; ���� ��� ���������  �����������  ������������, ��������� � 
 * ���������������� ���������� � ������������ ��� ������������ �����. ��� ������������ �������� 
 * ����������� ������������ �������� GNU.
 * 
 * �� ������ ���� �������� ����� ����������� ������������ �������� GNU ������ � ���� ����������. 
 * ���� ��� �� ���, �������� � ���� ���������� �� (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * ���-c��� ������������� : http://aion-knight.ru
 * ��������� ������� ���� : Aion 2.7 - '����� ������' (������) 
 * ������ ��������� ����� : Aion-Knight 2.7 (Beta version)
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
	private String legionName;
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