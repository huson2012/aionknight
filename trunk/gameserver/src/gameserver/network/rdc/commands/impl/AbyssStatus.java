/**
 * This file is part of Aion-Knight Dev. Team [http://www.aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a  copy  of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */
package gameserver.network.rdc.commands.impl;


import gameserver.model.siege.Influence;
import gameserver.model.siege.SiegeLocation;
import gameserver.model.siege.SiegeType;
import gameserver.network.rdc.commands.RDCCommand;
import gameserver.services.SiegeService;


/**
 * @author Sylar
 *
 */
public class AbyssStatus extends RDCCommand
{
	
	public AbyssStatus()
	{
		super("AbyssStatus");
	}
	
	@Override
	public String handleRequest(String... args)
	{
		String xml = "<AbyssStatus time=\"" + String.valueOf(System.currentTimeMillis() / 1000) + "\">";
		
			xml += "<InfluenceRatio Elyos=\"" + String.valueOf(Influence.getInstance().getElyos() * 100);
			xml += "\" Asmodians=\"" + String.valueOf(Influence.getInstance().getAsmos() * 100);
			xml += "\" Balaur=\"" + String.valueOf(Influence.getInstance().getBalaur() * 100);
			xml += "\" />";
			
			xml += "<AbyssTimer Remains=\"" + String.valueOf(SiegeService.getInstance().getSiegeTime()) + "\" />";
			
			xml += "<Locations>";
			
				for(SiegeLocation loc : SiegeService.getInstance().getSiegeLocations().values())
				{
					if(loc.getSiegeType() == SiegeType.ARTIFACT || loc.getSiegeType() == SiegeType.FORTRESS)
					{
						xml += "<SiegeLocation id=\"" + String.valueOf(loc.getLocationId()) + "\" type=\"" + loc.getSiegeType().name() + "\">";
						
							xml += "<HoldingRace>" + loc.getRace().name() + "</HoldingRace>";
							xml += "<HoldingLegion>" + String.valueOf(loc.getLegionId()) + "</HoldingLegion>";
							if(loc.getSiegeType() == SiegeType.FORTRESS)
							{
								xml += "<CurrentStatus>";
									xml += (loc.isVulnerable()) ? "VULNERABLE" : "INVULNERABLE";
								xml += "</CurrentStatus>";
								xml += "<NextStatus>";
									xml += (loc.getNextState() == 0) ? "INVULNERABLE" : "VULNERABLE";
								xml += "</NextStatus>";
							}
						
						xml += "</SiegeLocation>";
					}
				}
			
			xml += "</Locations>";
		
		xml += "</AbyssStatus>";
		return xml;
	}
	
}
