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

package gameserver.network.aion.serverpackets;

import gameserver.configs.main.SiegeConfig;
import gameserver.model.siege.SiegeLocation;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.services.SiegeService;
import javolution.util.FastMap;
import java.nio.ByteBuffer;
import java.util.Map;

// SM_SIEGE_LOCATION_INFO
public class SM_SIEGE_LOCATION_INFO extends AionServerPacket
{
	/***
	 * infoType
	 * 0 - reset
	 * 1 - change
	 */
	private int infoType;
	
	private Map<Integer, SiegeLocation> locations;
	
	public SM_SIEGE_LOCATION_INFO()
	{
		this.infoType = 0; // Reset
		locations = SiegeService.getInstance().getSiegeLocations();
	}
	
	/**
	 * @param loc
	 */
	public SM_SIEGE_LOCATION_INFO(SiegeLocation loc)
	{
		this.infoType = 1; // Update
		locations = new FastMap<Integer, SiegeLocation>();
		locations.put(loc.getLocationId(), loc);
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		if (SiegeConfig.SIEGE_ENABLED == false)
		{
			// Siege is Disabled
			writeC(buf, 0);
			writeH(buf, 0);
			return;
		}
		
		writeC(buf, infoType);
		writeH(buf, locations.size());

		for(SiegeLocation sLoc : locations.values())
		{	
			writeD(buf, sLoc.getLocationId()); // Artifact ID
			
			writeD(buf, sLoc.getLegionId()); // Legion ID
			
			writeD(buf, 0);		
			
			writeD(buf, 0); // unk
			
			writeC(buf, sLoc.getRace().getRaceId());
			
			// is vulnerable (0 - no, 2 - yes)
			writeC(buf, sLoc.isVulnerable() ? 2 : 0);
			
			 // faction can teleport (0 - no, 1 - yes)
			writeC(buf, 1);
			
			// Next State (0 - invulnerable, 1 - vulnerable)
			writeC(buf, sLoc.getNextState());
			
			writeH(buf, 0); // unk
			writeH(buf, 1);
			switch(sLoc.getLocationId())
			{
				case 2111:
					writeD(buf, sLoc.isVulnerable() ? 2*60*60 : 0);// mastarius & veille timer
					break;
				case 3111:
					writeD(buf, sLoc.isVulnerable() ? 2*60*60 : 0); // mastarius & veille timer
					break;
				default:
					writeD(buf, 0); // mastarius & veille timer
					break;
			}
		}
	}
}
