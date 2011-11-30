package gameserver.model.templates;

import gameserver.model.templates.expand.Expand;

import javax.xml.bind.annotation.*;
import java.util.List;


/**
 * @author Simple
 */
@XmlRootElement(name = "cube_npc")
@XmlAccessorType(XmlAccessType.FIELD)
public class CubeExpandTemplate
{
	@XmlElement(name = "expand", required = true)
	protected List<Expand>	cubeExpands;

	/**
	 * NPC ID
	 */
	@XmlAttribute(name = "id", required = true)
	private int				Id;

	/**
	 * NPC name
	 */
	@XmlAttribute(name = "name", required = true)
	private String			name		= "";

	public String getName()
	{
		return name;
	}

	public int getNpcId()
	{
		return Id;
	}
	
	/**
	 * Returns true if list contains level
	 * @return true or false
	 */
	public boolean contains(int level)
	{
		for(Expand expand : cubeExpands)
		{
			if(expand.getLevel() == level)
				return true;
		}
		return false;
	}
	
	/**
	 * Returns true if list contains level
	 * @return expand
	 */
	public Expand get(int level)
	{
		for(Expand expand : cubeExpands)
		{
			if(expand.getLevel() == level)
				return expand;
		}
		return null;
	}	
}
