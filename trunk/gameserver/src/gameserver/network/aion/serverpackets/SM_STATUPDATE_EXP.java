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

/**
 * This packet is used to update current exp / recoverable exp / max exp values.
 */
public class SM_STATUPDATE_EXP extends AionServerPacket
{
	private long	currentExp;
	private long	recoverableExp;
	private long	maxExp;

	/**
	 * 
	 * @param currentExp
	 * @param recoverableExp
	 * @param maxExp
	 */
	public SM_STATUPDATE_EXP(long currentExp, long recoverableExp, long maxExp)
	{
		this.currentExp = currentExp;
		this.recoverableExp = recoverableExp;
		this.maxExp = maxExp;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeQ(buf, currentExp);
		writeQ(buf, recoverableExp);
		writeQ(buf, maxExp);
		if(con.getActivePlayer() != null && con.getActivePlayer().getCommonData().getRepletionState() > 0)
		{
			writeQ(buf, con.getActivePlayer().getCommonData().getRepletionState());
			writeQ(buf, (((con.getActivePlayer().getLevel() * 1000) * 2) * con.getActivePlayer().getLevel()));
		}
		else
			writeQ(buf, 0);
			writeQ(buf, 0);
	}

}
