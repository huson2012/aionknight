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

package gameserver.network.loginserver.clientpackets;

import gameserver.model.gameobjects.player.Player;
import gameserver.network.loginserver.LoginServer;
import gameserver.network.loginserver.LsClientPacket;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.Util;
import gameserver.utils.rates.Rates;
import gameserver.world.World;

public class CM_LS_CONTROL_RESPONSE extends LsClientPacket
{
	private int	type;
	private boolean result;
	private String playerName;
	private byte param;
	private String adminName;
	private int	accountId;

	public CM_LS_CONTROL_RESPONSE(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		type = readC();
		result = readC() == 1;
		adminName = readS();
		playerName = readS();
		param = (byte) readC();
		accountId = readD();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		World world = World.getInstance();
		Player admin = world.findPlayer(Util.convertName(adminName));
		Player player = world.findPlayer(Util.convertName(playerName));
		LoginServer.getInstance().accountUpdate(accountId, param, type);
		switch (type)
		{
			case 1:
				if(!result)
				{
				   if(admin != null)
				      PacketSendUtility.sendMessage(admin, playerName + " access level has been updated to " + param );
				   if(player != null)
				   {
				      PacketSendUtility.sendMessage(player, "Your access level have been updated to " + param + " by " + adminName);
				   }
				}
				else
				{
				   if(admin != null)
				      PacketSendUtility.sendMessage(admin, " Abnormal, the operation failed! ");
				}
			break;
			case 2:
				if(!result)
				{
				   if(admin != null)
				      PacketSendUtility.sendMessage(admin, playerName + " membership has been updated to " + param );
				   if(player != null)
				   {
				      player.setRates(Rates.getRatesFor(param));
				      PacketSendUtility.sendMessage(player, "Your membership have been updated to " + param + " by " + adminName);
				   }
				}
				else
				{
				   if(admin != null)
				      PacketSendUtility.sendMessage(admin, " Abnormal, the operation failed! ");
				}
			break;
		}
	}
}
