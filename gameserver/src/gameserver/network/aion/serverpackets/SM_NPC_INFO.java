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

import gameserver.model.NpcType;
import gameserver.model.gameobjects.*;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.StatEnum;
import gameserver.model.items.ItemSlot;
import gameserver.model.items.NpcEquippedGear;
import gameserver.model.siege.Artifact;
import gameserver.model.templates.NpcTemplate;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;
import java.util.Map.Entry;

/**
 * This packet is displaying visible npc/monsters.
 */
public class SM_NPC_INFO extends AionServerPacket
{
	/**
	 * Visible npc
	 */
	private Creature npc;
	private NpcTemplate npcTemplate;
	private int npcId;
	private int masterObjId;
	private String masterName = "";

	@SuppressWarnings("unused")
	private float speed = 0.3f;
	private final int npcTypeId;

	/**
	 * Constructs new SM_NPC_INFO packet
	 *
	 * @param npc visible npc.
	 * @param player
	 */
	public SM_NPC_INFO(Npc npc, Player player)
	{
		this.npc = npc;
		npcTemplate = npc.getObjectTemplate();

		if(npcTemplate.getNpcType() == NpcType.NEUTRAL || npcTemplate.getNpcType() == NpcType.ARTIFACT)
		{
			if(player.isAggroIconTo(npc.getTribe()))
				npcTypeId = NpcType.NEUTRAL.getId();
			else
				npcTypeId = NpcType.NON_ATTACKABLE.getId();
		}
		else
		{
			npcTypeId = (player.isAggroIconTo(npc.getTribe()) ? NpcType.AGGRESSIVE.getId() : npcTemplate.getNpcType().getId());
		}
		npcId = npc.getNpcId();

	}

	/**
	 * Constructs new <tt>SM_NPC_INFO </tt> packet
	 *
	 * @param player
	 * @param kisk - the visible npc.
	 */
	public SM_NPC_INFO(Player player, Kisk kisk)
	{
		this.npc = kisk;
		npcTypeId = (kisk.isAggroFrom(player) ? NpcType.ATTACKABLE.getId() : NpcType.NON_ATTACKABLE.getId());
		npcTemplate = kisk.getObjectTemplate();
		npcId = kisk.getNpcId();

		masterObjId = kisk.getOwnerObjectId();
		masterName = kisk.getOwnerName();
	}

	/**
	 * @param player
	 * @param groupgate - the visible npc.
	 */
	public SM_NPC_INFO(Player player, GroupGate groupgate)
	{
		this.npc = groupgate;
		npcTypeId = (groupgate.isAggroFrom(player) ? NpcType.ATTACKABLE.getId() : NpcType.NON_ATTACKABLE.getId());
		npcTemplate = groupgate.getObjectTemplate();
		npcId = groupgate.getNpcId();

		Player owner = (Player) groupgate.getCreator();
		if(owner != null)
		{
			masterObjId = owner.getObjectId();
			masterName = owner.getName();
		}
	}

	/**
	 * @param summon
	 */
	public SM_NPC_INFO(Summon summon)
	{
		this.npc = summon;
		npcTemplate = summon.getObjectTemplate();
		npcTypeId = npcTemplate.getNpcType().getId();
		npcId = summon.getNpcId();
		Player owner = summon.getMaster();
		if(owner != null)
		{
			masterObjId = owner.getObjectId();
			masterName = owner.getName();
			speed = owner.getGameStats().getCurrentStat(StatEnum.SPEED) / 1000f;
		}
		else
		{
			masterName = "LOST";
		}
	}

	public SM_NPC_INFO(Trap trap)
	{
		this.npc = trap;
		npcTemplate = trap.getObjectTemplate();
		npcTypeId = npcTemplate.getNpcType().getId();
		npcId = trap.getNpcId();
		Player owner = (Player) trap.getMaster();
		if(owner != null)
		{
			masterObjId = owner.getObjectId();
			masterName = owner.getName();
			speed = 0;
		}
		else
		{
			masterName = "LOST";
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeF(buf, npc.getX());// x
		writeF(buf, npc.getY());// y
		writeF(buf, npc.getZ());// z
		writeD(buf, npc.getObjectId());
		writeD(buf, npcId);
		writeD(buf, npcId);
		writeC(buf, npcTypeId);
		writeH(buf, npc.getState());

		writeC(buf, npc.getHeading());
		writeD(buf, npcTemplate.getNameId());
		writeD(buf, npcTemplate.getTitleId());// titleID
		writeH(buf, 0x00);// unk
		writeC(buf, 0x00);// unk
		writeD(buf, 0x00);// unk

		/**
		 * Master Info (Summon, Kisk, Etc)
		 */
		writeD(buf, masterObjId);// masterObjectId
		writeS(buf, masterName);// masterName

		int maxHp = npc.getLifeStats().getMaxHp();
		int currHp = npc.getLifeStats().getCurrentHp();
		writeC(buf, 100 * currHp / maxHp);// %hp
		writeD(buf, npc.getGameStats().getCurrentStat(StatEnum.MAXHP));
		writeC(buf, npc.getLevel());// lvl

		NpcEquippedGear gear = npcTemplate.getEquipment();
		if(gear == null)
			writeH(buf, 0x00);
		else
		{
			writeH(buf, gear.getItemsMask());
			for(Entry<ItemSlot, ItemTemplate> item : gear) // getting it from template ( later if we make sure that npcs actually use items, we'll make Item from it )
			{
				writeD(buf, item.getValue().getTemplateId());
				writeD(buf, 0x00);
				writeD(buf, 0x00);
				writeH(buf, 0x00);
			}
		}

		writeF(buf, 1.5f);
		writeF(buf, npcTemplate.getHeight());
		writeF(buf, npc.getMoveController().getSpeed()); // speed

		writeH(buf, 2000);// 0x834
		writeH(buf, 2000);// 0x834

		if(npc instanceof Servant)
			writeC(buf, 0x01);// unk
		else
			writeC(buf, 0x00);

		/**
		 * Movement
		 */
		writeF(buf, npc.getX());// x
		writeF(buf, npc.getY());// y
		writeF(buf, npc.getZ());// z
		writeC(buf, 0x00); // move type

		SpawnTemplate spawn = npc.getSpawn();
		if(spawn == null)
		{
			writeH(buf, 0);
		}
		else
		{
			int mask = (Math.round(spawn.getStaticid() / 0xFF) < 0 ? 0 : Math.round(spawn.getStaticid() / 0xFF));
			byte[] statics = new byte[] { (byte) spawn.getStaticid(), (byte) mask };
			writeB(buf, statics);
		}

		writeC(buf, 0);
		writeC(buf, 0);
		writeC(buf, 0);
		writeC(buf, 0);
		writeC(buf, 0);
		writeC(buf, 0);
		writeC(buf, 0);
		writeC(buf, 0);
		writeC(buf, npc.getVisualState());

		/**
		 * 1 : normal (kisk too)
		 * 2 : summon
		 * 32 : trap
		 * 1024 : holy servant, noble energy
		 */
		writeH(buf, npc.getNpcObjectType().getId());
		writeC(buf, 0x00);
		if(npc instanceof Artifact)
		{
			writeD(buf, 0);
		}
		else
		{
			writeD(buf, npc.getTarget() == null ? 0 : npc.getTarget().getObjectId());
		}
	}
}
