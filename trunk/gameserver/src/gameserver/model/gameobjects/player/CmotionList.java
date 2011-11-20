package gameserver.model.gameobjects.player;

import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * @author jjhun
 */
public class CmotionList
{
	private LinkedHashMap<Integer, Cmotion> cmotions;
	private Player owner;

	public CmotionList()
	{
	    this.cmotions = new LinkedHashMap<Integer, Cmotion>();
	    this.owner = null;
	}

	public void setOwner(Player owner)
	{
		this.owner = owner;
	}

	public Player getOwner()
	{
		return owner;
	}

	public boolean add(int id, boolean active, long date, long expires_time)
	{
		if(!cmotions.containsKey(id))
		{
			cmotions.put(id, new Cmotion(id, active, date, expires_time));
			return true;
		}
		return false;
	}

	public void remove(int id)
	{
		if(cmotions.containsKey(id))
		{
			cmotions.remove(id);
		}
	}

	public Cmotion get(int id)
	{
		if(cmotions.containsKey(id))
			return cmotions.get(id);

		return null;
	}

	public boolean canAdd(int id)
	{
		if(cmotions.containsKey(id))
			return false;

		return true;
	}

	public int size()
	{
		return cmotions.size();
	}

	public Collection<Cmotion> getCmotions()
	{

		return cmotions.values();
	}
}
