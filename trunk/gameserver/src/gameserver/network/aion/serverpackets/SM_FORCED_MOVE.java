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
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_FORCED_MOVE extends AionServerPacket
{
	private Creature creature;
	private Creature target;
	private float x = 0;
	private float y = 0;
	private float z = 0;
	
	public SM_FORCED_MOVE(Creature creature, float x, float y, float z)
	{
		this.creature = creature;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public SM_FORCED_MOVE(Creature creature, Creature target)
	{
		this.creature = creature;
		this.target = target;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, creature.getObjectId());
		if(target != null)
			writeD(buf, target.getObjectId());
		else
			writeD(buf, creature.getObjectId());
		writeC(buf, 16); // unk
		if(x == 0 && y == 0 && z == 0)
		{
			writeF(buf, target.getX());
			writeF(buf, target.getY());
			writeF(buf, target.getZ() + 0.25f);
		}
		else
		{
			writeF(buf, x);
			writeF(buf, y);
			writeF(buf, z);
		}
	}
}
