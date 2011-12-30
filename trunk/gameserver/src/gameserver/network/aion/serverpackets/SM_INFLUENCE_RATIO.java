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

import gameserver.model.siege.Influence;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.services.SiegeService;
import java.nio.ByteBuffer;

public class SM_INFLUENCE_RATIO extends AionServerPacket
{

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		Influence inf = Influence.getInstance();
		
		writeD(buf, SiegeService.getInstance().getSiegeTime());
		writeF(buf, inf.getElyos());
		writeF(buf, inf.getAsmos());
		writeF(buf, inf.getBalaur());

		writeH(buf, 3);
		for(int i=0; i<3; i++)
		{
			switch(i)
			{
				case 0:
					writeD(buf, 210050000);
					writeF(buf, inf.getElyosInggison());
					writeF(buf, inf.getAsmosInggison());
					writeF(buf, inf.getBalaurInggison());
					break;
				case 1:
					writeD(buf, 220070000);
					writeF(buf, inf.getElyosGelkmaros());
					writeF(buf, inf.getAsmosGelkmaros());
					writeF(buf, inf.getBalaurGelkmaros());
					break;
				case 2:
					writeD(buf, 400010000);
					writeF(buf, inf.getElyosAbyss());
					writeF(buf, inf.getAsmosAbyss());
					writeF(buf, inf.getBalaurAbyss());
					break;
			}
		}
	}
}
