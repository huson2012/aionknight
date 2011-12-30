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

import gameserver.model.legion.LegionMemberEx;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class SM_LEGION_MEMBERLIST extends AionServerPacket
{
	private static final int			OFFLINE	= 0x00;
	private static final int			ONLINE	= 0x01;
	private ArrayList<LegionMemberEx>	legionMembers;

	/**
	 * This constructor will handle legion member info when a List of members is given
	 * 
	 * @param ArrayList
	 *           <LegionMemberEx> legionMembers
	 */
	public SM_LEGION_MEMBERLIST(ArrayList<LegionMemberEx> legionMembers)
	{
		this.legionMembers = legionMembers;
	}

	@Override
	public void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeC(buf, 0x01);
		writeH(buf, (65536 - legionMembers.size()));
		for(LegionMemberEx legionMember : legionMembers)
		{
			writeD(buf, legionMember.getObjectId());
			writeS(buf, legionMember.getName());
			writeC(buf, legionMember.getPlayerClass().getClassId());
			writeD(buf, legionMember.getLevel());
			writeC(buf, legionMember.getRank().getRankId());
			writeD(buf, legionMember.getWorldId());
			writeC(buf, legionMember.isOnline() ? ONLINE : OFFLINE);
			writeS(buf, legionMember.getSelfIntro());
			writeS(buf, legionMember.getNickname());
			writeD(buf, legionMember.getLastOnline());
		}
	}
}
