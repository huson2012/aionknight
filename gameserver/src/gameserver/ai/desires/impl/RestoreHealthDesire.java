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

package gameserver.ai.desires.impl;

import gameserver.ai.AI;
import gameserver.ai.desires.AbstractDesire;
import gameserver.ai.events.Event;
import gameserver.model.gameobjects.Creature;
import gameserver.model.siege.FortressGeneral;
import gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;

public class RestoreHealthDesire extends AbstractDesire
{	
	private Creature owner;
	private int restoreHpValue;
	private int restoreMpValue;

	public RestoreHealthDesire(Creature owner, int desirePower)
	{
		super(desirePower);
		this.owner = owner;
		restoreHpValue = owner.getLifeStats().getMaxHp() / 5;
		restoreMpValue = owner.getLifeStats().getMaxMp() / 5;
	}
	
	@Override
	public boolean handleDesire(AI<?> ai)
	{
		if(owner == null || owner.getLifeStats().isAlreadyDead() || owner instanceof FortressGeneral)
			return false;
		
		if(!owner.isInCombat())
		{
			owner.getLifeStats().increaseHp(TYPE.NATURAL_HP, restoreHpValue);
			owner.getLifeStats().increaseMp(TYPE.NATURAL_MP, restoreMpValue);
		}

		if(owner.getLifeStats().isFullyRestoredHpMp())
		{
			ai.handleEvent(Event.RESTORED_HEALTH);
			return false;
		}
		return true;
	}


	@Override
	public int getExecutionInterval()
	{
		return 1;
	}

	@Override
	public void onClear()
	{
		// TODO Auto-generated method stub
	}
}