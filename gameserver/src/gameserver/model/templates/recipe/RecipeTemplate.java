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

package gameserver.model.templates.recipe;

import gameserver.skill.model.learn.SkillRace;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RecipeTemplate")
public class RecipeTemplate
{
	protected List<Component> component;
	protected List<ComboProduct> comboproduct;
	@XmlAttribute
	protected int maxcount = 0;
	@XmlAttribute
	protected int tasktype;
	@XmlAttribute
	protected int componentquantity;
	@XmlAttribute
	protected int quantity;
	@XmlAttribute
	protected int productid;
	@XmlAttribute
	protected int autolearn;
	@XmlAttribute
	protected int dp;
	@XmlAttribute
	protected int skillpoint;
	@XmlAttribute
	protected SkillRace race;
	@XmlAttribute
	protected int skillid;
	@XmlAttribute
	protected int itemid;
	@XmlAttribute
	protected int nameid;
	@XmlAttribute
	protected int id;

	/**
	 * Gets the value of the component property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list,
	 * not a snapshot. Therefore any modification you make to the
	 * returned list will be present inside the JAXB object.
	 * This is why there is not a <CODE>set</CODE> method for the component property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * <pre>
	 *	getComponent().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link Component }
	 * 
	 * 
	 */
	public List<Component> getComponent()
	{
		if(component == null)
			component = new ArrayList<Component>();
		return this.component;
	}

	public List<ComboProduct> getComboProduct()
	{
		if(comboproduct == null)
			comboproduct = new ArrayList<ComboProduct>();
		return comboproduct;
	}
	
	/**
	 * Gets the value of the maxcount property.
	 * 
	 * @return
	 *	 possible object is
	 *	 {@link Integer }
	 *	 
	 */
	public Integer getMaxProductionCount()
	{
		return maxcount;
	}
	
	/**
	 * Gets the value of the tasktype property.
	 * 
	 * @return
	 *	 possible object is
	 *	 {@link Integer }
	 *	 
	 */
	public Integer getTasktype()
	{
		return tasktype;
	}

	/**
	 * Gets the value of the componentquantity property.
	 * 
	 * @return
	 *	 possible object is
	 *	 {@link Integer }
	 *	 
	 */
	public Integer getComponentquantity()
	{
		return componentquantity;
	}

	/**
	 * Gets the value of the quantity property.
	 * 
	 * @return
	 *	 possible object is
	 *	 {@link Integer }
	 *	 
	 */
	public Integer getQuantity()
	{
		return quantity;
	}

	/**
	 * Gets the value of the productid property.
	 * 
	 * @return
	 *	 possible object is
	 *	 {@link Integer }
	 *	 
	 */
	public Integer getProductid()
	{
		return productid;
	}

	/**
	 * Gets the value of the autolearn property.
	 * 
	 * @return
	 *	 possible object is
	 *	 {@link Integer }
	 *	 
	 */
	public Integer getAutolearn()
	{
		return autolearn;
	}

	/**
	 * Gets the value of the dp property.
	 * 
	 * @return
	 *	 possible object is
	 *	 {@link Integer }
	 *	 
	 */
	public Integer getDp()
	{
		return dp;
	}

	/**
	 * Gets the value of the skillpoint property.
	 * 
	 * @return
	 *	 possible object is
	 *	 {@link Integer }
	 *	 
	 */
	public Integer getSkillpoint()
	{
		return skillpoint;
	}

	/**
	 * Gets the value of the race property.
	 * 
	 * @return
	 *	 possible object is
	 *	 {@link String }
	 *	 
	 */
	public SkillRace getRace()
	{
		return race;
	}

	/**
	 * Gets the value of the skillid property.
	 * 
	 * @return
	 *	 possible object is
	 *	 {@link Integer }
	 *	 
	 */
	public Integer getSkillid()
	{
		return skillid;
	}

	/**
	 * Gets the value of the itemid property.
	 * 
	 * @return
	 *	 possible object is
	 *	 {@link Integer }
	 *	 
	 */
	public Integer getItemid()
	{
		return itemid;
	}

	/**
	 * @return the nameid
	 */
	public int getNameid()
	{
		return nameid;
	}

	/**
	 * Gets the value of the id property.
	 * 
	 * @return
	 *	 possible object is
	 *	 {@link Integer }
	 *	 
	 */
	public Integer getId()
	{
		return id;
	}

}
