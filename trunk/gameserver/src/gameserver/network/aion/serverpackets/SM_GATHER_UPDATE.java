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

import gameserver.model.templates.GatherableTemplate;
import gameserver.model.templates.gather.Material;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_GATHER_UPDATE extends AionServerPacket
{
	private GatherableTemplate template;
	private int action;
	private int itemId;
	private int success;
	private int failure;
	private int nameId;

	public SM_GATHER_UPDATE(GatherableTemplate template, Material material, int success, int failure, int action)
	{
		this.action = action;
		this.template = template;
		this.itemId = material.getItemid();
		this.success = success;
		this.failure = failure;
		this.nameId = material.getNameid();
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeH(buf, template.getSkillLevel());
		writeC(buf, action);
		writeD(buf, itemId);

		switch(action)
		{
			case 0:
			{
				writeD(buf, 100); //success value
				writeD(buf, 100); //failure value
				writeD(buf, 0);
				writeD(buf, 1200);	//delay after which bar will start moving (ms)
				writeD(buf, 1330011); //start gathering system message
				writeH(buf, 0x24);
				writeD(buf, nameId); //item nameId to display it's name in system message above
				writeH(buf, 0);
				break;
			}
			case  1:
			{
				writeD(buf, success);
				writeD(buf, failure);
				writeD(buf, 700);	//time of moving execution (ms)
				writeD(buf, 1200);
				writeD(buf, 0);
				writeH(buf, 0);
				break;
			}
			case 2:
			{
				writeD(buf, 100);
				writeD(buf, failure);
				writeD(buf, 700);
				writeD(buf, 1200);
				writeD(buf, 0);
				writeH(buf, 0);
				break;
			}
			case 5: // you have stopped gathering
			{
				writeD(buf, 0);
				writeD(buf, 0);
				writeD(buf, 700);
				writeD(buf, 1200);
				writeD(buf, 1330080);
				writeH(buf, 0);
				break;
			}
			case 6:
			{
				writeD(buf, 100);
				writeD(buf, failure);
				writeD(buf, 700);
				writeD(buf, 1200);
				writeD(buf, 0);
				writeH(buf, 0);
				break;
			}
			case 7:
			{
				writeD(buf, success);
				writeD(buf, 100);
				writeD(buf, 0);
				writeD(buf, 1200);
				writeD(buf, 1330079);
				writeH(buf, 0x24);
				writeD(buf, nameId);
				writeH(buf, 0);
				break;
			}
		}
	}

}
