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

import gameserver.model.group.LootDistribution;
import gameserver.model.group.LootGroupRules;
import gameserver.model.group.LootRuleType;
import gameserver.model.group.PlayerGroup;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_GROUP_INFO extends AionServerPacket
{
	private int groupid;
	private int leaderid;
	private LootRuleType lootruletype; //0-free-for-all, 1-round-robin 2-leader
	private LootDistribution autodistribution;
	//rare item distribution
	//0-normal, 2-Roll-dice,3-bid
	private int common_item_above;
	private int superior_item_above;
	private int heroic_item_above;
	private int fabled_item_above;
	private int ethernal_item_above;
	private int over_ethernal;
	private int over_over_ethernal;

	public SM_GROUP_INFO(PlayerGroup group) //need a group class whit this parameters
	{
		this.groupid = group.getGroupId();
		this.leaderid = group.getGroupLeader().getObjectId();
		
		LootGroupRules lootRules = group.getLootGroupRules();
		this.lootruletype = lootRules.getLootRule();
		this.autodistribution = lootRules.getAutodistribution();
		this.common_item_above = lootRules.getCommon_item_above();
		this.superior_item_above = lootRules.getSuperior_item_above();
		this.heroic_item_above = lootRules.getHeroic_item_above();
		this.fabled_item_above = lootRules.getFabled_item_above();
		this.ethernal_item_above = lootRules.getEthernal_item_above();
		this.over_ethernal = lootRules.getOver_ethernal();
		this.over_over_ethernal = lootRules.getOver_over_ethernal();
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, this.groupid);
		writeD(buf, this.leaderid);
		writeD(buf, this.lootruletype.getId());
		writeD(buf, this.autodistribution.getId());
		writeD(buf, this.common_item_above);
		writeD(buf, this.superior_item_above);
		writeD(buf, this.heroic_item_above);
		writeD(buf, this.fabled_item_above);
		writeD(buf, this.ethernal_item_above);
		writeD(buf, this.over_ethernal);
		writeD(buf, this.over_over_ethernal);
        writeD(buf, 0x3F00);
		writeD(buf, 0x00);
		writeH(buf, 0x00);
		writeC(buf, 0x00);
	}
}
