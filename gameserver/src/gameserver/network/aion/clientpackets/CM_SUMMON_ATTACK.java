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

package gameserver.network.aion.clientpackets;

import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Summon;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.world.World;
import org.apache.log4j.Logger;

public class CM_SUMMON_ATTACK extends AionClientPacket
{
	private static final Logger	log	= Logger.getLogger(CM_SUMMON_ATTACK.class);

	@SuppressWarnings("unused")
	private int summonObjId;
	private int targetObjId;
	@SuppressWarnings("unused")
	private int unk1;
	@SuppressWarnings("unused")
	private int unk2;
	@SuppressWarnings("unused")
	private int unk3;
	
	public CM_SUMMON_ATTACK(int opcode)
	{
		super(opcode);
	}

	@Override
	protected void readImpl()
	{
		summonObjId = readD();
		targetObjId = readD();
		unk1 = readC();
		unk2 = readH();
		unk3 = readC();
	}

	@Override
	protected void runImpl()
	{
		// TODO: Use summonObjId to get summon, instead of activePlayer?
		Player activePlayer = getConnection().getActivePlayer();
		if (activePlayer == null)
		{
			log.error("CM_SUMMON_ATTACK packet received but cannot get master player.");
			return;
		}
		
		Summon summon = activePlayer.getSummon();
		
		if(summon == null)
		{
			log.error("CM_SUMMON_ATTACK packet received but cannot get summon.");
			return;
		}
		
		Creature creature = (Creature) World.getInstance().findAionObject(targetObjId);
		if (creature == null)
			return;
		summon.getController().attackTarget(creature);
	}
}