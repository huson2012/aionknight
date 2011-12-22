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

package gameserver.ai.events.handler;

import gameserver.ai.AI;
import gameserver.ai.events.Event;
import gameserver.ai.state.AIState;
import gameserver.model.gameobjects.Npc;
import gameserver.utils.ThreadPoolManager;

public class TalkEventHandler implements EventHandler
{
	@Override
	public Event getEvent()
	{
		return Event.TALK;
	}

	@Override
	public void handleEvent(Event event, AI<?> ai)
	{
		final Npc owner = (Npc) ai.getOwner();

		if(owner.hasWalkRoutes())
		{
			owner.getMoveController().setCanWalk(false);
			owner.getController().stopMoving();

			ThreadPoolManager.getInstance().schedule(new Runnable(){
				@Override
				public void run()
				{
					owner.getMoveController().setCanWalk(true);
				}
			}, 60000);
		}
		ai.setAiState(AIState.TALKING);
	}
}
