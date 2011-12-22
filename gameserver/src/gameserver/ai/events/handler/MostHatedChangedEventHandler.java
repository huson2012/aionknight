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
import gameserver.model.ShoutEventType;
import gameserver.model.gameobjects.Npc;
import gameserver.services.NpcShoutsService;

public class MostHatedChangedEventHandler implements EventHandler
{
	@Override
	public Event getEvent()
	{
		return Event.MOST_HATED_CHANGED;
	}

	@Override
	public void handleEvent(Event event, AI<?> ai)
	{
		ai.setAiState(AIState.THINKING);
		if(ai.getOwner() instanceof Npc)
			NpcShoutsService.getInstance().handleEvent((Npc)ai.getOwner(), ai.getOwner().getAggroList().getMostHated(), ShoutEventType.SWICHTARGET);
	}
}
