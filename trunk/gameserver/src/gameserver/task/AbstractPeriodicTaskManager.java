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

package gameserver.task;

import commons.taskmanager.AbstractLockManager;
import commons.utils.Rnd;
import gameserver.GameServer;
import gameserver.GameServer.StartupHook;
import gameserver.utils.ThreadPoolManager;
import org.apache.log4j.Logger;

public abstract class AbstractPeriodicTaskManager extends AbstractLockManager implements Runnable, StartupHook
{
	protected static final Logger log = Logger.getLogger(AbstractPeriodicTaskManager.class);
	private final int period;
	public AbstractPeriodicTaskManager(int period)
	{
		this.period = period;
		GameServer.addStartupHook(this);
		log.info(getClass().getSimpleName() + ": Ok!");
	}

	@Override
	public final void onStartup()
	{
		ThreadPoolManager.getInstance().scheduleAtFixedRate(this, 1000 + Rnd.get(period),
		Rnd.get(period - 5, period + 5));
	}
	@Override
	public abstract void run();
}