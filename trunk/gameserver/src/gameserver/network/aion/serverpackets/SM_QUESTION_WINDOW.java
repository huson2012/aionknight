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

import gameserver.model.DescriptionId;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

/**
 * Opens a yes/no question window on the client. Question based on the code given, defined in client_strings.xml
 */
public class SM_QUESTION_WINDOW extends AionServerPacket
{
	public static final int	STR_BUDDYLIST_ADD_BUDDY_REQUETS		= 0x0DBEE9;
	public static final int	STR_EXCHANGE_DO_YOU_ACCEPT_EXCHANGE	= 0x15f91;
	public static final int	STR_EXCHANGE_HE_REJECTED_EXCHANGE	= 0x13D782;	// TODO: make it a simple box, not a
																			// question.
	public static final int	STR_DUEL_DO_YOU_CONFIRM_DUEL		= 0xc36e;
	public static final int	STR_DUEL_DO_YOU_ACCEPT_DUEL			= 0xc36c;
	public static final int	STR_SOUL_HEALING					= 160011;
	public static final int	STR_BIND_TO_LOCATION				= 160012;
	public static final int	STR_REQUEST_GROUP_INVITE			= 60000;
	public static final int	STR_REQUEST_ALLIANCE_INVITE			= 70004;
	public static final int	STR_WAREHOUSE_EXPAND_WARNING		= 900686;
	public static final int	STR_USE_RIFT						= 160019;
	public static final int	STR_LEGION_INVITE					= 80001;
	public static final int	STR_LEGION_DISBAND					= 80008;
	public static final int	STR_LEGION_DISBAND_CANCEL			= 80009;
	public static final int	STR_LEGION_CHANGE_MASTER			= 80011;
	public static final int STR_CRAFT_ADDSKILL_CONFIRM 			= 900852;
	public static final int STR_BIND_TO_KISK					= 160018;
	public static final int STR_SOUL_BOUND_ITEM_DO_YOU_WANT_SOUL_BOUND = 95006;
	public static final int STR_ASK_GROUP_GATE_DO_YOU_ACCEPT_MOVE = 160014;
	public static final int STR_QUEST_GIVEUP_WHEN_DELETE_QUEST_ITEM = 150001;
	public static final int STR_SUMMON_PARTY_DO_YOU_ACCEPT_REQUEST = 901721;

	/**
	 * %0 is an untradable item. Are you sure you want to acquire it?
	 */
	public static final int	STR_CONFIRM_LOOT					= 900495;

	private int				code;
	private int				senderId;
	private Object[]		params;

	/**
	 * Creates a new <tt>SM_QUESTION_WINDOW<tt> packet
	 * 
	 * @param code
	 *           code The string code to display, found in client_strings.xml
	 * @param senderId
	 *           sender Object id
	 * @param params
	 *           params The parameters for the string, if any
	 */
	public SM_QUESTION_WINDOW(int code, int senderId, Object... params)
	{
		this.code = code;
		this.senderId = senderId;
		this.params = params;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, code);

		for(Object param : params)
		{
			if (param instanceof DescriptionId)
			{
				writeH(buf, 0x24);
				writeD(buf, ((DescriptionId) param).getValue());
				writeH(buf, 0x00); //unk
			}
			else
				writeS(buf, String.valueOf(param));
		}

		// Guardian Stone Activation Window
		if(code == 160027)
		{
			writeD(buf, 0x00);
			writeD(buf, 0x00);
			writeD(buf, 0x00);
			writeD(buf, 0x00);
			writeD(buf, 0x00);
			writeH(buf, 0x00);
			writeC(buf, 0x00);
		}
		// Artifact Activation Window
		else if(code == 160028)
		{
			writeD(buf, 0x00);
			writeD(buf, 0x00);
			writeD(buf, 0x00);
			writeD(buf, 0x00);
			writeH(buf, 0x00);
			writeC(buf, 0x00);
		}
		else
		{
			writeD(buf, 0x00);// unk
			writeH(buf, 0x00);// unk
			writeC(buf, 0x01);// unk
			writeD(buf, senderId);
			writeD(buf, 0x06); // group 6, unk
		}
	}

}
