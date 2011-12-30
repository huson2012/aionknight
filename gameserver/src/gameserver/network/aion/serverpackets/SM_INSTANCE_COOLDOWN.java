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

import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_INSTANCE_COOLDOWN extends AionServerPacket
{
	private Player player;
	private boolean init = false;
	private int instanceId = 0;
	private int remainingTime = 0;
	private int type = 2;
	private boolean self = false;

	public SM_INSTANCE_COOLDOWN(Player player)
	{
		this.player = player;
	}
	
	public SM_INSTANCE_COOLDOWN(boolean init)
	{
		this.init = init;
	}
	
	public SM_INSTANCE_COOLDOWN(Player player, int id, int time, int type, boolean self)
	{
		this.player = player;
		this.instanceId = id;
		this.remainingTime = time;
		this.type = type;
		this.self = self;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		if(!init)
		{
			writeH(buf, type);
			writeD(buf, 0x0);
			writeH(buf, 0x1);
			writeD(buf, player.getObjectId());
			if(instanceId != 0 && remainingTime != 0)
			{
				writeH(buf, 0x1); //instance info
				writeD(buf, instanceId);
				writeD(buf, 0x0);
				writeD(buf, remainingTime); //remaingTime in seconds
				if(self)
					writeH(buf, 0x0);
				writeS(buf, player.getName());
			}
			else
			{
				writeH(buf, 0x0); //not instance info
				writeS(buf, player.getName());
			}
		}
		else
		{
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
		}
	}
}
