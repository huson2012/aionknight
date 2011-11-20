package gameserver.model.gameobjects.player;

/**
 * @author jjhun
 *
 */
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
