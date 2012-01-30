/*
 * Emulator game server Aion 2.7 from the command of developers 'Aion-Knight Dev. Team' is
 * free software; you can redistribute it and/or modify it under the terms of
 * GNU affero general Public License (GNU GPL)as published by the free software
 * security (FSF), or to License version 3 or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranties related to
 * CONSUMER PROPERTIES and SUITABILITY FOR CERTAIN PURPOSES. For details, see
 * General Public License is the GNU.
 *
 * You should have received a copy of the GNU affero general Public License along with this program.
 * If it is not, write to the Free Software Foundation, Inc., 675 Mass Ave,
 * Cambridge, MA 02139, USA
 *
 * Web developers : http://aion-knight.ru
 * Support of the game client : Aion 2.7- 'Arena of Death' (Innova)
 * The version of the server : Aion-Knight 2.7 (Beta version)
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
