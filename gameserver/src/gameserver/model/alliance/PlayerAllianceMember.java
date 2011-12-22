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

package gameserver.model.alliance;

import gameserver.model.gameobjects.AionObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.PlayerCommonData;

public class PlayerAllianceMember extends AionObject
{
	private Player player;
	private String name;
	private int allianceId;
	private PlayerCommonData playerCommonData;

	public PlayerAllianceMember(Player player)
	{
		super(player.getObjectId());
		this.player = player;
		this.name = player.getName();
		this.playerCommonData = player.getCommonData();
	}
	
	@Override
	public String getName()
	{
		return name;
	}

	public Player getPlayer()
	{
		return player;
	}

	public void onLogin(Player player)
	{
		this.player = player;
		this.playerCommonData = player.getCommonData();
	}

	public void onDisconnect()
	{
		this.player = null;
	}

	public boolean isOnline()
	{
		return (player != null);
	}

	public PlayerCommonData getCommonData()
	{
		return playerCommonData;
	}

	public int getAllianceId()
	{
		return allianceId;
	}

	public void setAllianceId(int allianceId)
	{
		this.allianceId = allianceId;
	}
}