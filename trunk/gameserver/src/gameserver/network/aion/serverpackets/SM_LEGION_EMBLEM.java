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

import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_LEGION_EMBLEM extends AionServerPacket
{
	/** Legion information **/
	private int	legionId;
	private int emblemVer;
	private boolean isCustom;
	private int color_r;
	private int color_g;
	private int color_b;
	private String legionName;
	
	private int emblemSize;

	/**
	 * This constructor will handle legion emblem info
	 * 
	 * @param legionId
	 */
	
	public SM_LEGION_EMBLEM(int legionId, int emblemVer, int emblemSize, int color_r, int color_g, int color_b, String legionName, boolean isCustom)
	{
		this.legionId = legionId;
		this.emblemVer = emblemVer;
		this.emblemSize = emblemSize;
		this.color_r = color_r;
		this.color_g = color_g;
		this.color_b = color_b;
		this.legionName = legionName;
		this.isCustom = isCustom;
	}
	
	public SM_LEGION_EMBLEM(int legionId, int emblemVer, int color_r, int color_g, int color_b, String legionName, boolean isCustom)
	{
		this.legionId = legionId;
		this.emblemVer = emblemVer;
		this.color_r = color_r;
		this.color_g = color_g;
		this.color_b = color_b;
		this.legionName = legionName;
		this.emblemSize = 0;
		this.isCustom = isCustom;
	}

	@Override
	public void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, legionId);
		writeC(buf, emblemVer);
		writeC(buf, isCustom ? 0x80: 0x00);
		writeD(buf, emblemSize); // Custom Emblem Size, otherwise 0x00
		writeC(buf, 0xFF); // default 0xFF; sets transparency and 0x00 looks better
		writeC(buf, color_r);
		writeC(buf, color_g);
		writeC(buf, color_b);
		writeS(buf, legionName);
		writeC(buf, 0x01);

		// ED 55 8A 6C 04 00 00 01 80 00 00 00 00 FF FF FF .U.l............
		// FF 44 00 72 00 61 00 6B 00 65 00 73 00 00 00 01 .D.r.a.k.e.s....
	}
}