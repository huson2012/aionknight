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

package gameserver.ai.events;

import gameserver.ai.events.handler.*;

public enum EventHandlers
{
	ATTACKED_EH(new AttackedEventHandler()),
	TIREDATTACKING_EH(new TiredAttackingEventHandler()),
	MOST_HATED_CHANGED_EH(new TiredAttackingEventHandler()),
	SEEPLAYER_EH(new SeePlayerEventHandler()),
	NOTSEEPLAYER_EH(new NotSeePlayerEventHandler()),
	RESPAWNED_EH(new RespawnedEventHandler()),
	BACKHOME_EH(new BackHomeEventHandler()),
	TALK_EH(new TalkEventHandler()),
	RESTOREDHEALTH_EH(new RestoredHealthEventHandler()),
	NOTHINGTODO_EH(new NothingTodoEventHandler()),
	DESPAWN_EH(new DespawnEventHandler()),
	DIED_EH(new DiedEventHandler());
	
	private EventHandler eventHandler;
	
	private EventHandlers(EventHandler eventHandler)
	{
		this.eventHandler = eventHandler;
	}
	
	public EventHandler getHandler()
	{
		return eventHandler;
	}
}