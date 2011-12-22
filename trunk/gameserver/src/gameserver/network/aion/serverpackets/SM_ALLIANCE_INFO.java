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

import gameserver.model.alliance.PlayerAlliance;
import gameserver.model.group.LootDistribution;
import gameserver.model.group.LootGroupRules;
import gameserver.model.group.LootRuleType;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_ALLIANCE_INFO extends AionServerPacket
{
	private PlayerAlliance alliance;
	
	public SM_ALLIANCE_INFO(PlayerAlliance alliance)
	{
		this.alliance = alliance;
	}
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeH(buf, 4);
		writeD(buf, alliance.getObjectId());
		writeD(buf, alliance.getCaptainObjectId());
		
		int i = 0;
		for (int group : alliance.getViceCaptainObjectIds())
		{
			writeD(buf,group);
			i++;
		}

		for (;i<4 ; i++)
		{
			writeD(buf,0);
		}

		LootGroupRules lootRules = this.alliance.getLootAllianceRules();
		LootRuleType lootruletype = lootRules.getLootRule();
		LootDistribution autodistribution = lootRules.getAutodistribution();

		writeD(buf, lootruletype.getId());
		writeD(buf, autodistribution.getId());
		writeD(buf, lootRules.getCommon_item_above());
		writeD(buf, lootRules.getSuperior_item_above());
		writeD(buf, lootRules.getHeroic_item_above());
		writeD(buf, lootRules.getFabled_item_above());
		writeD(buf, lootRules.getEthernal_item_above());
		writeD(buf, lootRules.getOver_ethernal());
		writeD(buf, lootRules.getOver_over_ethernal());
		writeC(buf, 0);
        writeD(buf, 63);
		writeD(buf, 0);

		for (i = 0; i < 4; i++)
		{
			writeD(buf, i);
			writeD(buf, 1000+i);
		}

		writeD(buf, 0);
		writeD(buf, 0);
	}
}