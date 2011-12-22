/**   
 * Эмулятор игрового сервера Aion 2.7 от команды разработчиков 'Aion-Knight Dev. Team' является 
 * свободным программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного программного 
 * обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой более поздней 
 * версии.
 * 
 * Программа распространяется в надежде, что она будет полезной, но БЕЗ КАКИХ БЫ ТО НИ БЫЛО 
 * ГАРАНТИЙНЫХ ОБЯЗАТЕЛЬСТВ; даже без косвенных  гарантийных  обязательств, связанных с 
 * ПОТРЕБИТЕЛЬСКИМИ СВОЙСТВАМИ и ПРИГОДНОСТЬЮ ДЛЯ ОПРЕДЕЛЕННЫХ ЦЕЛЕЙ. Для подробностей смотрите 
 * Стандартную Общественную Лицензию GNU.
 * 
 * Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе с этой программой. 
 * Если это не так, напишите в Фонд Свободного ПО (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * Веб-cайт разработчиков : http://aion-knight.ru
 * Поддержка клиента игры : Aion 2.7 - 'Арена Смерти' (Иннова) 
 * Версия серверной части : Aion-Knight 2.7 (Beta version)
 */

package gameserver.network.rdc.commands.impl;

import gameserver.model.siege.Influence;
import gameserver.model.siege.SiegeLocation;
import gameserver.model.siege.SiegeType;
import gameserver.network.rdc.commands.RDCCommand;
import gameserver.services.SiegeService;

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