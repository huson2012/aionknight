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

package gameserver.model.gameobjects.player;

public class Cmotion
{
	private int cmotionId;
	private boolean cmotion_active;
	private long cmotion_date = 0;
	private long cmotion_expires_time = 0;
	
	
    /**
     * @param Cmotion 
     * 
     */       
	
	public Cmotion(int cmotionId, boolean cmotion_active, long cmotion_date, long cmotion_expires_time)
	{
		this.cmotionId 				 = cmotionId;
		this.cmotion_active			 = cmotion_active;
		this.cmotion_date			 = cmotion_date;
		this.cmotion_expires_time	 = cmotion_expires_time;
	}
	
	public int getCmotionId()
	{
		return cmotionId;
	}
	
	public boolean getActive()
	{
		return cmotion_active;
	}
	
	public long getCmotionCreationTime()
	{
		return cmotion_date;
	}

	public long getCmotionExpiresTime()
	{
		return cmotion_expires_time;
	}

	public void setCmotionId(int cmotionId)
	{
		this.cmotionId = cmotionId;
	}
	
	public void setActive(boolean en)
	{
		this.cmotion_active = en;
	}
	
	public long getCmotionTimeLeft()
	{
		if(cmotion_expires_time == 0)
			return 0;

		long timeLeft = (cmotion_date + ((cmotion_expires_time )  * 1000L)) - System.currentTimeMillis();
		if(timeLeft < 0)
			timeLeft = 0;

		return timeLeft /1000L ;
	}

	public void setCmotionDate(long cmotion_date)
	{
		this.cmotion_date = cmotion_date;
	}

	public void setCmotionExpiresTime(long cmotion_expires_time)
	{
		this.cmotion_expires_time = cmotion_expires_time;
	}
}
