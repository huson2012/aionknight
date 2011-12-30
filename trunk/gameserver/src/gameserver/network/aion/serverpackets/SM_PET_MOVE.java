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

import gameserver.model.gameobjects.player.ToyPet;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_PET_MOVE extends AionServerPacket
{
	private int actionId;
	private ToyPet pet;
	
	public SM_PET_MOVE(int actionId, ToyPet pet)
	{
		this.actionId = actionId;
		this.pet = pet;
	}
	

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, pet.getUid());
		if (actionId != 0)
			writeC(buf, actionId);
		switch(actionId)
		{
			case 0:
				writeC(buf, 0);
				writeF(buf, pet.getX1());
				writeF(buf, pet.getY1());
				writeF(buf, pet.getZ1());
				writeC(buf, pet.getH());
			case 12:
				// move
				writeF(buf, pet.getX1());
				writeF(buf, pet.getY1());
				writeF(buf, pet.getZ1());
				writeC(buf, pet.getH());
				writeF(buf, pet.getX2());
				writeF(buf, pet.getY2());
				writeF(buf, pet.getZ2());
				break;
			default:
				break;					
		}
	}
}
