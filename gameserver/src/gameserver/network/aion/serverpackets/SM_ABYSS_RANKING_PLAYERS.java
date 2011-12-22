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

import gameserver.model.AbyssRankingResult;
import gameserver.model.Race;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.services.AbyssRankingService;
import gameserver.utils.PacketSendUtility;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class SM_ABYSS_RANKING_PLAYERS extends AionServerPacket
{	
	private ArrayList<AbyssRankingResult> 	data;
	private ArrayList<AbyssRankingResult>	dataTemp;
	private int 							race;
	private int action = 0;
	private int section = 1;
	private Player player;
	public SM_ABYSS_RANKING_PLAYERS(ArrayList<AbyssRankingResult> data, Race race, Player player)
	{
		this.data = data;
		dataTemp = new ArrayList<AbyssRankingResult>();
		this.race = race.getRaceId();
		this.player = player;
	}
	
	public SM_ABYSS_RANKING_PLAYERS(ArrayList<AbyssRankingResult> data, int race, int action, int section, Player player)
	{
		this.data = data;
		dataTemp = new ArrayList<AbyssRankingResult>();
		this.race = race;
		this.action = action;
		this.section = section;
		this.player = player;
	}
	
	@Override	
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		int count = 0;
		
		writeD(buf, race);
		writeD(buf, Math.round(AbyssRankingService.getInstance().getTimeOfUpdate() / 1000));
		writeD(buf, section);
		writeD(buf, action);
		
		if(data.size() > 46)
			writeH(buf, 0x2E);
		else
			writeH(buf, data.size());
			
		
		for (AbyssRankingResult rs : data)
		{
			if(count >= 46)
			{
				dataTemp.add(rs);
			}	
			else
			{
				writeD(buf, rs.getTopRanking());
				writeD(buf, rs.getPlayerRank());
				writeD(buf, rs.getOldRanking());
				writeD(buf, rs.getPlayerId());
				writeD(buf, race);
				writeD(buf, rs.getPlayerClass().getClassId());
				writeD(buf, 0);
				writeD(buf, rs.getPlayerAP());
				writeD(buf, 0);
				writeC(buf, rs.getPlayerLevel());
				writeC(buf, 0);
				writeS(buf, rs.getPlayerName());
				writeB(buf, new byte[50 - (rs.getPlayerName().length() * 2)]);

				if(rs.getLegionName() == null)
				{
				    writeS(buf, "");
				    writeB(buf, new byte[80]);
				}
				else
				{
				    writeS(buf, rs.getLegionName());
				    writeB(buf, new byte[80 - (rs.getLegionName().length() * 2)]);
				}

				count++;
			}
		}
		if (section < 64)
		{
			section *= 2 ;
			if(section == 64)
				action = 127;
			else
				action = 0;
				
			data = null;	
			PacketSendUtility.sendPacket(player, new SM_ABYSS_RANKING_PLAYERS(dataTemp, race, action, section, player));
		}
		data = null;		
	}
}