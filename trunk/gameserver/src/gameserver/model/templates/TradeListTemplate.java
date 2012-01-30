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

package gameserver.model.templates;

import gameserver.model.trade.TradeListType;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="tradelist_template")
@XmlAccessorType(XmlAccessType.NONE)
public class TradeListTemplate
{
	/**
	 * Npc Id.
	 */
	@XmlAttribute(name = "npc_id", required = true)
	private int		npcId;
	
	/**
	 * Npc name.
	 */
	@XmlAttribute(name = "name", required = true)
	private String	name	= "";

	/**
	 * Number of twin instances [players will be balanced so every one could exp easy]
	 */
	@XmlAttribute(name = "count", required = true)
	private int		Count = 0;
	
	@XmlAttribute(name = "buy_rate")
	private float	buyRate = 1;
	
	@XmlAttribute(name = "sell_rate")
	private float	sellRate = 1;
	
	@XmlAttribute(name = "type")
	private TradeListType type = TradeListType.KINAH;

	@XmlElement(name = "tradelist")
	protected List<TradeTab> tradeTablist;
	
	/**
	 * 
	 * @return List<TradeTab>
	 */
	public List<TradeTab> getTradeTablist() {
        if (tradeTablist == null) {
            tradeTablist = new ArrayList<TradeTab>();
        }
        return this.tradeTablist;
    }
	
	public String getName()
	{
		return name;
	}

	public int getNpcId()
	{
		return npcId;
	}

	public int getCount()
	{
		return Count;
	}
	
	public float getBuyRate()
	{
		return buyRate;
	}
	
	public float getSellRate()
	{
		return sellRate;
	}

	/**
	 * @return the type of trade list
	 */
	public TradeListType getType()
	{
		return type;
	}



	/**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *  &lt;complexContent>
     *    &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *      &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}int" />
     *    &lt;/restriction>
     *  &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "Tradelist")
    public static class TradeTab {

        @XmlAttribute
        protected int id;

        /**
         * Gets the value of the id property.
         *    
         */
        public int getId() {
            return id;
        }
    }
}
