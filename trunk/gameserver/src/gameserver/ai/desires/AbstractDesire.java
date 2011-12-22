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

package gameserver.ai.desires;

import gameserver.ai.AI;

/**
 * This class implements basic functionality common for each desire
 * @see gameserver.ai.desires.Desire
 * @see gameserver.ai.desires.DesireQueue
 * @see gameserver.ai.AI
 * @see gameserver.ai.AI#handleDesire(Desire)
 */
public abstract class AbstractDesire implements Desire
{
	/**
	 * Current execution counter
	 */
	protected int executionCounter;
	/**
	 * Desire power. It's used to calculate what npc whants to do most of all.
	 */
	protected int	desirePower;

	/**
	 * Creates new desire. By design any desire should have desire power. So constructor accepts basic amout.
	 * 
	 * @param desirePower
	 *           basic amount of desirePower
	 */
	protected AbstractDesire(int desirePower)
	{
		this.desirePower = desirePower;
	}

	/**
	 * Compares this desire with another, used by {@link gameserver.ai.desires.DesireQueue} to keep track of
	 * desire priorities.
	 * 
	 * @param o
	 *           desire to compare with
	 * @return result of desire comparation
	 */
	@Override
	public int compareTo(Desire o)
	{
		return o.getDesirePower() - getDesirePower();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getDesirePower()
	{
		return desirePower;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void increaseDesirePower(int desirePower)
	{
		this.desirePower = this.desirePower + desirePower;
	}

	@Override
	public boolean handleDesire(AI<?> ai)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	public abstract int getExecutionInterval();
	
	@Override
	public boolean isReadyToRun()
	{
		boolean isReady =  executionCounter % getExecutionInterval() == 0;
		executionCounter++;
		return isReady;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void reduceDesirePower(int desirePower)
	{
		this.desirePower = this.desirePower - desirePower;
	}
}
