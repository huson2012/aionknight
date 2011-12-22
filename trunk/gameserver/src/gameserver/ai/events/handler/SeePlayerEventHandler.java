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
import gameserver.ai.desires.impl.AggressionDesire;
import gameserver.ai.events.Event;
import gameserver.ai.state.AIState;
import gameserver.model.gameobjects.AionObject;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.stats.modifiers.Executor;

public class SeePlayerEventHandler implements EventHandler
{
	@Override
	public Event getEvent()
	{
		return Event.SEE_PLAYER;
	}

	@Override
	public void handleEvent(Event event, final AI<?> ai)
	{
		ai.getOwner().updateKnownlist();
		ai.setAiState(AIState.ACTIVE);
		if(!ai.isScheduled())
			ai.analyzeState();
		else if(ai.getDesireQueue().hasWalkingDesire())
		{
			ai.getOwner().getKnownList().doOnAllObjects(new Executor<AionObject>(){
				@Override
				public boolean run(AionObject object)
				{
					if (object instanceof Creature && ai.getOwner().isAggressiveTo((Creature)object))
					{
						ai.addDesire(new AggressionDesire((Npc)ai.getOwner(), AIState.ACTIVE.getPriority()));
						return false;
					}
					return true;
				}
			}, true);
		}
	}
}
