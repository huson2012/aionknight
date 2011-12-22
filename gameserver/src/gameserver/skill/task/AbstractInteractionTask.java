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

import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.utils.ThreadPoolManager;

import java.util.concurrent.Future;

public abstract class AbstractInteractionTask
{
	private Future<?> task;
	private int interval = 2500;
	
	protected Player requestor;
	protected VisibleObject responder;
	
	/**
	 * 
	 * @param requestor
	 * @param responder
	 */
	public AbstractInteractionTask(Player requestor, VisibleObject responder, int skillLvlDiff)
	{
		super();
		if(skillLvlDiff == 99999)
			this.interval = 1500;
		else
			this.interval = 2500;
		this.requestor = requestor;
		if(responder == null)
			this.responder = requestor;
		else
			this.responder = responder;
	}

	/**
	 * Called on each interaction 
	 * 
	 * @return
	 */
	protected abstract boolean onInteraction();
	
	/**
	 * Called when interaction is complete
	 */
	protected abstract void onInteractionFinish();
	
	/**
	 * Called before interaction is started
	 */
	protected abstract void onInteractionStart();
	
	/**
	 * Called when interaction is not complete and need to be aborted
	 */
	protected abstract void onInteractionAbort();
	
	/**
	 * Interaction scheduling method
	 */
	public void start()
	{
		onInteractionStart();
		
		task = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable(){
			
			@Override
			public void run()
			{
				if(!validateParticipants())
					abort();
					
				boolean stopTask = onInteraction();
				if(stopTask)
					stop();
			}
	
		}, 1000, interval);
	}
	
	/**
	 * Stop current interaction
	 */
	public void stop()
	{
		onInteractionFinish();
		
		if(task != null && !task.isCancelled())
		{
			task.cancel(true);
			task = null;
		}
	}
	
	/**
	 * Abort current interaction
	 */
	public void abort()
	{
		onInteractionAbort();
		stop();
	}
	
	/**
	 * 
	 * @return true or false
	 */
	public boolean isInProgress()
	{
		return task != null && !task.isCancelled();
	}
	/**
	 * 
	 * @return true or false
	 */
	public boolean validateParticipants()
	{
		return requestor != null;
	}
}