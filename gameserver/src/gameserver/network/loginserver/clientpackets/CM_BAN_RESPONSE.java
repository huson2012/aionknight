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

import gameserver.configs.administration.AdminConfig;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.loginserver.LsClientPacket;
import gameserver.utils.PacketSendUtility;
import gameserver.world.World;

public class CM_BAN_RESPONSE extends LsClientPacket
{
	private byte		type;
	private int			accountId;
	private String		ip;
	private int			time;
	private int			adminObjId;
	private boolean		result;

	public CM_BAN_RESPONSE(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		this.type = (byte) readC();
		this.accountId = readD();
		this.ip = readS();
		this.time = readD();
		this.adminObjId = readD();
		this.result = readC() == 1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		Player admin = World.getInstance().findPlayer(adminObjId);
		
		if (admin == null)
			return;

		if (admin.getAccessLevel() < AdminConfig.COMMAND_BAN)
			return;
		
		// Some messages stuff
		String message;
		if (type == 1 || type == 3)
		{
			if (result)
			{
				if (time < 0)
					message = "Account " + accountId + " was successfully unbanned";
				else if (time == 0)
					message = "Accoun " + accountId + " was successfully banned";
				else
					message = "Account " + accountId + " was successfully banned for " + time + " minutes";
			}
			else
				message = "[!] Error occurred while banning player's account";
			PacketSendUtility.sendMessage(admin, message);
		}
		if (type == 2 || type == 3)
		{
			if (result)
			{
				if (time < 0)
					message = "IP mask " + ip + " was successfully removed from block list";
				else if (time == 0)
					message = "IP mask " + ip + " was successfully added to block list";
				else
					message = "IP mask " + ip + " was successfully added to block list for " + time + " minutes";
			}
			else
				message = "Error occurred while adding IP mask " + ip;
			PacketSendUtility.sendMessage(admin, message);
		}
	}
}