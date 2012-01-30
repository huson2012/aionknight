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

package gameserver.model.templates.quest;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Rewards", propOrder = {
    "selectableRewardItem",
    "rewardItem"
})
public class Rewards {

    @XmlElement(name = "selectable_reward_item")
    protected List<QuestItems> selectableRewardItem;
    @XmlElement(name = "reward_item")
    protected List<QuestItems> rewardItem;
    @XmlAttribute
    protected Integer gold;
    @XmlAttribute
    protected Integer exp;
    @XmlAttribute(name = "reward_abyss_point")
    protected Integer rewardAbyssPoint;
    @XmlAttribute
    protected Integer title;
	@XmlAttribute(name = "extend_inventory")
    protected Integer extendInventory;
    @XmlAttribute(name = "extend_stigma")
    protected Integer extendStigma;
    /**
     * Gets the value of the selectableRewardItem property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the selectableRewardItem property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *   getSelectableRewardItem().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link QuestItems }
     * 
     * 
     */
    public List<QuestItems> getSelectableRewardItem() {
        if (selectableRewardItem == null) {
            selectableRewardItem = new ArrayList<QuestItems>();
        }
        return this.selectableRewardItem;
    }

    /**
     * Gets the value of the rewardItem property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rewardItem property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *   getRewardItem().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link QuestItems }
     * 
     * 
     */
    public List<QuestItems> getRewardItem() {
        if (rewardItem == null) {
            rewardItem = new ArrayList<QuestItems>();
        }
        return this.rewardItem;
    }

    /**
     * Gets the value of the gold property.
     * 
     * @return
     *    possible object is
     *    {@link Integer }
     *    
     */
    public Integer getGold() {
        return gold;
    }

    /**
     * Gets the value of the exp property.
     * 
     * @return
     *    possible object is
     *    {@link Integer }
     *    
     */
    public Integer getExp() {
        return exp;
    }

    /**
     * Gets the value of the rewardAbyssPoint property.
     * 
     * @return
     *    possible object is
     *    {@link Integer }
     *    
     */
    public Integer getRewardAbyssPoint() {
        return rewardAbyssPoint;
    }

    /**
     * Gets the value of the title property.
     * 
     * @return
     *    possible object is
     *    {@link Integer }
     *    
     */
    public Integer getTitle() {
        return title;
    }

    /**
	 * @return the extendInventory
	 */
	public Integer getExtendInventory()
	{
		return extendInventory;
	}

	/**
	 * @return the extendStigma
	 */
	public Integer getExtendStigma()
	{
		return extendStigma;
	}
}
