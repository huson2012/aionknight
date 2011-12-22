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

package gameserver.task.impl;

import gameserver.model.alliance.PlayerAllianceEvent;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.group.GroupEvent;
import gameserver.services.AllianceService;
import gameserver.task.AbstractIterativePeriodicTaskManager;

public final class GroupUpdater extends AbstractIterativePeriodicTaskManager<Player>
{
	private static final class SingletonHolder
	{
		private static final GroupUpdater INSTANCE	= new GroupUpdater();
	}

	public static GroupUpdater getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	public GroupUpdater()
	{
		super(2000);
	}
	
	@Override
	protected void callTask(Player player)
	{
		if (player.isInGroup())
			player.getPlayerGroup().updateGroupUIToEvent(player, GroupEvent.MOVEMENT);
		
		if (player.isInAlliance())
			AllianceService.getInstance().updateAllianceUIToEvent(player, PlayerAllianceEvent.MOVEMENT);
		
		this.stopTask(player);
	}
	
	@Override
	protected String getCalledMethodName()
	{
		return "groupAllianceUpdate()";
	}
}