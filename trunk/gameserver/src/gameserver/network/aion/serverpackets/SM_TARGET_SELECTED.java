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

import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_TARGET_SELECTED extends AionServerPacket
{
	@SuppressWarnings("unused")
	private Player		player;
	private int	level;
	private int	maxHp;
	private int	currentHp;
	private int targetObjId;

	public SM_TARGET_SELECTED(Player player)
	{
		this.player = player;
		if(player.getTarget() instanceof Creature)
		{
			this.level = ((Creature) player.getTarget()).getLevel();
			this.maxHp = ((Creature) player.getTarget()).getLifeStats().getMaxHp();
			this.currentHp = ((Creature) player.getTarget()).getLifeStats().getCurrentHp();
		}
		else
		{
			//TODO: check various gather on retail
			this.level = 1;
			this.maxHp = 1;
			this.currentHp = 1;
		}
		
		if(player.getTarget() != null)
			targetObjId = player.getTarget().getObjectId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, targetObjId);
		writeH(buf, level);
		writeD(buf, maxHp);
		writeD(buf, currentHp);
	}
}