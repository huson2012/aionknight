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

package gameserver.skill.task;

import commons.utils.Rnd;
import gameserver.configs.main.CustomConfig;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;

public abstract class AbstractCraftTask extends AbstractInteractionTask
{
	protected int maxValue = 100;
	protected int currentSuccessValue = 0;
	protected int currentFailureValue = 0;
	protected int skillLvlDiff;
	protected boolean speedUp;
	
	/**
	 * 
	 * @param requestor
	 * @param responder
	 * @param skillLvlDiff
	 */
	public AbstractCraftTask(Player requestor, VisibleObject responder, int skillLvlDiff)
	{
		super(requestor, responder, skillLvlDiff);
		this.skillLvlDiff = skillLvlDiff;
	}

	@Override
	protected boolean onInteraction()
	{
		if(currentSuccessValue == maxValue)
		{
			return onSuccessFinish();
		}
		if(currentFailureValue == maxValue)
		{
			onFailureFinish();
			return true;
		}
		
		analyzeInteraction();
		
		sendInteractionUpdate();
		return false;
	}
	
	/**
	 * Perform interaction calculation
	 */
	private void analyzeInteraction()
	{
		speedUp = false;
		int multi = Math.max(0, CustomConfig.REGULAR_CRAFTING_SUCCESS-skillLvlDiff*5);
		if(skillLvlDiff == 99999)
		{
			currentSuccessValue = maxValue;
			return;
		}
		
		speedUp = Rnd.get(100) <= CustomConfig.CRAFTING_SPEEDUP;
		
		if(speedUp)
			currentSuccessValue += Rnd.get(maxValue/2,maxValue);
		
		if(Rnd.get(100) > multi)
			currentSuccessValue += Rnd.get(maxValue/(multi+1)/2,maxValue);
		else
			currentFailureValue += Rnd.get(maxValue/(multi+1)/2,maxValue);
		
		if(currentSuccessValue >= maxValue)
			currentSuccessValue = maxValue;
		else if(currentFailureValue >= maxValue)
			currentFailureValue = maxValue;
	}
	
	protected abstract void sendInteractionUpdate();
	
	protected abstract boolean onSuccessFinish();
	
	protected abstract void onFailureFinish();
}