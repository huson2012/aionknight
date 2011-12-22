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

package gameserver.network.aion;

import gameserver.model.account.PlayerAccountData;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.PlayerAppearance;
import gameserver.model.gameobjects.player.PlayerCommonData;
import gameserver.model.items.GodStone;
import gameserver.model.items.ItemSlot;
import org.apache.log4j.Logger;
import java.nio.ByteBuffer;
import java.util.List;

public abstract class PlayerInfo extends AionServerPacket
{
	private static Logger log = Logger.getLogger(PlayerInfo.class);

	protected PlayerInfo()
	{
	}

	protected void writePlayerInfo(ByteBuffer buf, PlayerAccountData accPlData)
	{
		PlayerCommonData pbd = accPlData.getPlayerCommonData();
		final int raceId = pbd.getRace().getRaceId();
		final int genderId = pbd.getGender().getGenderId();
		final PlayerAppearance playerAppearance = accPlData.getAppereance();
		writeD(buf, pbd.getPlayerObjId());
		writeS(buf, pbd.getName());
		writeB(buf, new byte[52 - (pbd.getName().length() * 2 + 2)]);
		writeD(buf, genderId);
		writeD(buf, raceId);
		writeD(buf, pbd.getPlayerClass().getClassId());
		writeD(buf, playerAppearance.getVoice());
		writeD(buf, playerAppearance.getSkinRGB());
		writeD(buf, playerAppearance.getHairRGB());
		writeD(buf, playerAppearance.getEyeRGB());
		writeD(buf, playerAppearance.getLipRGB());
		writeC(buf, playerAppearance.getFace());
		writeC(buf, playerAppearance.getHair());
		writeC(buf, playerAppearance.getDecoration());
		writeC(buf, playerAppearance.getTattoo());		
		writeC(buf, playerAppearance.getFaceContour());
		writeC(buf, playerAppearance.getExpression());
		writeC(buf, 6);// always 6 - 2.5.x
		writeC(buf, playerAppearance.getJawLine());
		writeC(buf, playerAppearance.getForehead());
		writeC(buf, playerAppearance.getEyeHeight());
		writeC(buf, playerAppearance.getEyeSpace());
		writeC(buf, playerAppearance.getEyeWidth());
		writeC(buf, playerAppearance.getEyeSize());
		writeC(buf, playerAppearance.getEyeShape());
		writeC(buf, playerAppearance.getEyeAngle());
		writeC(buf, playerAppearance.getBrowHeight());
		writeC(buf, playerAppearance.getBrowAngle());
		writeC(buf, playerAppearance.getBrowShape());
		writeC(buf, playerAppearance.getNose());
		writeC(buf, playerAppearance.getNoseBridge());
		writeC(buf, playerAppearance.getNoseWidth());
		writeC(buf, playerAppearance.getNoseTip());
		writeC(buf, playerAppearance.getCheeks());
		writeC(buf, playerAppearance.getLipHeight());
		writeC(buf, playerAppearance.getMouthSize());
		writeC(buf, playerAppearance.getLipSize());
		writeC(buf, playerAppearance.getSmile());
		writeC(buf, playerAppearance.getLipShape());
		writeC(buf, playerAppearance.getChinHeight());
		writeC(buf, playerAppearance.getCheekBones());
		writeC(buf, playerAppearance.getEarShape());
		writeC(buf, playerAppearance.getHeadSize());
		writeC(buf, playerAppearance.getNeck());
		writeC(buf, playerAppearance.getNeckLength());
		writeC(buf, playerAppearance.getShoulderSize());
		writeC(buf, playerAppearance.getTorso());
		writeC(buf, playerAppearance.getChest());
		writeC(buf, playerAppearance.getWaist());
		writeC(buf, playerAppearance.getHips());
		writeC(buf, playerAppearance.getArmThickness());
		writeC(buf, playerAppearance.getHandSize());		
		writeC(buf, playerAppearance.getLegThickness());
		writeC(buf, playerAppearance.getFootSize());		
		writeC(buf, playerAppearance.getFacialRatio());
		writeC(buf, 0x00); // 0x00	
		writeC(buf, playerAppearance.getArmLength());
		writeC(buf, playerAppearance.getLegLength());	
		writeC(buf, playerAppearance.getShoulders());
		writeC(buf, playerAppearance.getFaceShape());		
		writeC(buf, 0x00); // always 0 may be acessLevel		
		writeC(buf, 0x00); // always 0 - unk -17
		writeC(buf, 0x00); // added 119
		writeF(buf, playerAppearance.getHeight());
		int raceSex = 100000 + raceId * 2 + genderId;
		writeD(buf, raceSex);
		writeD(buf, pbd.getPosition().getMapId());//mapid for preloading map
		writeF(buf, pbd.getPosition().getX());
		writeF(buf, pbd.getPosition().getY());
		writeF(buf, pbd.getPosition().getZ());
		writeD(buf, pbd.getPosition().getHeading());
		writeD(buf, pbd.getLevel());// lvl confirmed
		writeD(buf, pbd.getTitleId());

		if(!accPlData.isLegionMember())
		{
			byte[] somebyte = new byte[92];
			writeB(buf, somebyte);
		}else{
			writeD(buf, accPlData.getLegion().getLegionId());
			writeS(buf, accPlData.getLegion().getLegionName());
			byte[] somebyte = new byte[86/**-2byte*/- (accPlData.getLegion().getLegionName().length() * 2)];
			writeB(buf, somebyte);
		}

		int itemsDataSize = 0;

		List<Item> items = accPlData.getEquipment();

		for(Item item : items)
		{
			if(itemsDataSize >= 208)
				break;

			if(item.getItemTemplate().isArmor() || item.getItemTemplate().isWeapon())
			{
				if(item.getEquipmentSlot() <= ItemSlot.PANTS.getSlotIdMask())
				{
					if (item.getItemTemplate().isWeapon() && item.getEquipmentSlot() == 2 )
					{
						writeC(buf, 0x02);
					}
					else 
					{
						writeC(buf, 0x01);
					}
					writeD(buf, item.getItemSkinTemplate().getTemplateId());
					GodStone godStone = item.getGodStone();
					writeD(buf, godStone != null ? godStone.getItemId() : 0);
					writeD(buf, item.getItemColor());

					itemsDataSize += 13;
				}
			}
		}
		writeB(buf, new byte[208 - itemsDataSize]);
		writeD(buf, accPlData.getDeletionTimeInSeconds());
		writeD(buf, 0x00);
	}
}