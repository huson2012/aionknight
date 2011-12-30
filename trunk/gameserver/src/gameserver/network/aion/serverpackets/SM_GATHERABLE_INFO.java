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

import gameserver.model.gameobjects.VisibleObject;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_GATHERABLE_INFO  extends AionServerPacket
{
	private VisibleObject visibleObject;

	public SM_GATHERABLE_INFO(VisibleObject visibleObject)
	{
		super();
		this.visibleObject = visibleObject;
	}
	
	@Override
	public void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeF(buf, visibleObject.getX());
		writeF(buf, visibleObject.getY());
		writeF(buf, visibleObject.getZ());
		writeD(buf, visibleObject.getObjectId());
		writeD(buf, visibleObject.getSpawn().getStaticid()); //unk
		writeD(buf, visibleObject.getObjectTemplate().getTemplateId());
		writeH(buf, 1); //unk
		writeC(buf, 0);
		writeD(buf, visibleObject.getObjectTemplate().getNameId());
		writeH(buf, 0);
		writeH(buf, 0);
		writeH(buf, 0);
		writeC(buf, 100); //unk
	}
}
