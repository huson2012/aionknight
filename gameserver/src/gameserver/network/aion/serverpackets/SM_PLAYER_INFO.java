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

import gameserver.configs.main.CustomConfig;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.PlayerAppearance;
import gameserver.model.gameobjects.player.PlayerCommonData;
import gameserver.model.gameobjects.stats.StatEnum;
import gameserver.model.items.GodStone;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;
import java.util.List;

public class SM_PLAYER_INFO extends AionServerPacket
{

	/**
	 * Visible player
	 */
	private final Player	player;
	private boolean			enemy;

	/**
	 * Constructs new <tt>SM_PLAYER_INFO </tt> packet
	 * 
	 * @param player
	 *           actual player.
	 * @param enemy
	 */
	public SM_PLAYER_INFO(Player player, boolean enemy)
	{
		this.player = player;
		this.enemy = enemy;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		if(con == null || con.getActivePlayer() == null)
			return;
		
		PlayerCommonData pcd = player.getCommonData();

		final int raceId = (player.getAdminNeutral() || con.getActivePlayer().getAdminNeutral()) ?
				con.getActivePlayer().getCommonData().getRace().getRaceId() :
				pcd.getRace().getRaceId();

		final int genderId = pcd.getGender().getGenderId();
		final PlayerAppearance playerAppearance = player.getPlayerAppearance();

		writeF(buf, player.getX());// x
		writeF(buf, player.getY());// y
		writeF(buf, player.getZ());// z
		writeD(buf, player.getObjectId());
		/**
		 * A3 female asmodian A2 male asmodian A1 female elyos A0 male elyos
		 */
		writeD(buf, pcd.getTemplateId());
		/**
		 * Transformed state - send transformed model id Regular state - send player model id (from common data)
		 */
		writeD(buf, player.getTransformedModelId() == 0 ? pcd.getTemplateId() : player.getTransformedModelId());

		writeC(buf, 0x00); // (2.5) 0 default, can be 3
		writeC(buf, 0x01); // (2.5) 1 default, can be 0 if the previous is 3
		writeC(buf, 0x00);
		writeC(buf, 0x00);
		writeC(buf, 0x00);
		
		writeC(buf, enemy ? 0x00 : 38); // (2.5) Old 0x26... o.o

		writeC(buf, raceId); // race
		writeC(buf, pcd.getPlayerClass().getClassId());
		writeC(buf, genderId); // sex
		writeH(buf, player.getState());

		byte[] unk = new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
			(byte) 0x00, (byte) 0x00 };
		writeB(buf, unk);

		writeC(buf, player.getHeading());

		String playerName = "";

		if(CustomConfig.GMTAG_DISPLAY)
		{
			if(player.getAccessLevel() == 1 )
			{
				playerName += CustomConfig.GM_LEVEL1.trim();
			}
			else if (player.getAccessLevel() == 2 )
			{
				playerName += CustomConfig.GM_LEVEL2.trim();
			}
			else if (player.getAccessLevel() == 3 )
			{
				playerName += CustomConfig.GM_LEVEL3.trim();
			}
		}

		playerName += player.getName();
		
		writeS(buf, playerName);

		writeH(buf, pcd.getTitleId());
		writeH(buf, 0); // (2.5) 0 default, 1 for cute mentor wings around your name <3
		writeH(buf, player.getCastingSkillId());
		writeD(buf, player.isLegionMember() ? player
			.getLegion().getLegionId() : 0);
		writeC(buf, player.isLegionMember() ? player
			.getLegion().getLegionEmblem().getEmblemVer() : 0);
		if (player.isLegionMember())
		{
			writeC(buf, player.getLegion().getLegionEmblem().getIsCustom() ? 0x80 : 0x00);
			writeC(buf, 0xFF); // This is the transparency setting; official is 0xFF for none, but I think changing the cape's color is cool ^^:
		} else {
			writeC(buf, 0x00);
			writeC(buf, 0x00);
		}
		writeC(buf, player.isLegionMember() ? player
			.getLegion().getLegionEmblem().getColor_r() : 0);
		writeC(buf, player.isLegionMember() ? player
			.getLegion().getLegionEmblem().getColor_g() : 0);
		writeC(buf, player.isLegionMember() ? player
			.getLegion().getLegionEmblem().getColor_b() : 0);
		writeS(buf, player.isLegionMember() ? player
			.getLegion().getLegionName() : "");

		int maxHp = player.getLifeStats().getMaxHp();
		int currHp = player.getLifeStats().getCurrentHp();
		writeC(buf, 100 * currHp / maxHp);// %hp
		writeH(buf, pcd.getDp());// current dp
		writeC(buf, 0); // (2.5) 0 default. In their fisrt login, newbies have it 2. Can be 1 sometimes in random players. O_o

		List<Item> items = player.getEquipment().getEquippedItemsWithoutStigma();
		short mask = 0;
		for(Item item : items)
		{
			mask |= item.getEquipmentSlot();
		}

		writeH(buf, mask);

		for(Item item : items)
		{
			if(item.getEquipmentSlot() < Short.MAX_VALUE * 2)
			{
				writeD(buf, item.getItemSkinTemplate().getTemplateId());
				GodStone godStone = item.getGodStone();
				writeD(buf, godStone != null ? godStone.getItemId() : 0); 
				writeD(buf, item.getItemColor());
				writeH(buf, 0x00);// unk (0x00)
			}
		}

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
		
		writeC(buf, playerAppearance.getVoice());

		writeF(buf, playerAppearance.getHeight());

		writeF(buf, 0.25f); // scale
		writeF(buf, 2.0f); // gravity or slide surface o_O
		writeF(buf, player.getGameStats().getCurrentStat(StatEnum.SPEED) / 1000f); // move speed

		writeH(buf, player.getGameStats().getBaseStat(StatEnum.ATTACK_SPEED));
		writeH(buf, player.getGameStats().getCurrentStat(StatEnum.ATTACK_SPEED));
		writeC(buf, 4);

		writeS(buf, player.hasStore() ? player.getStore().getStoreMessage() : "");// private store message

		/**
		 * Movement
		 */
		writeF(buf, 0);
		writeF(buf, 0);
		writeF(buf, 0);

		writeF(buf, player.getX());// x
		writeF(buf, player.getY());// y
		writeF(buf, player.getZ());// z
		writeC(buf, 0x00); // move type

		if(player.isUsingFlyTeleport())
		{
			writeD(buf, player.getFlightTeleportId());
			writeD(buf, player.getFlightDistance());
		}

		writeC(buf, player.getVisualState()); // visualState
		writeS(buf, player.getCommonData().getNote()); // note show in right down windows if your target on player
		
		writeH(buf, player.getLevel()); // [level]
		writeH(buf, player.getPlayerSettings().getDisplay()); // display helmet/cloak
		writeH(buf, player.getPlayerSettings().getDeny()); // config for auto deny invites etc
		writeH(buf, player.getAbyssRank().getRank().getId()); // abyss rank
		writeC(buf, 0); // (2.5) 0 default, can be 5 sometimes. O_o
		writeD(buf, (player.getTarget() == null || player.getTarget().getObjectId() == null) ? 
			0 : player.getTarget().getObjectId());
		writeH(buf, 0); // (2.5) 0 default. Can be 4 or 128 when target != 0. If sent wrong, a Supected Bot appears over your head >3
		writeH(buf, 0); // (2.5) unk1 4 digits something...
		writeH(buf, 0);
		writeC(buf, 0);
	}
}
