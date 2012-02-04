/*
 * Emulator game server Aion 2.7 from the command of developers 'Aion-Knight Dev. Team' is
 * free software; you can redistribute it and/or modify it under the terms of
 * GNU affero general Public License (GNU GPL)as published by the free software
 * security (FSF), or to License version 3 or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranties related to
 * CONSUMER PROPERTIES and SUITABILITY FOR CERTAIN PURPOSES. For details, see
 * General Public License is the GNU.
 *
 * You should have received a copy of the GNU affero general Public License along with this program.
 * If it is not, write to the Free Software Foundation, Inc., 675 Mass Ave,
 * Cambridge, MA 02139, USA
 *
 * Web developers : http://aion-knight.ru
 * Support of the game client : Aion 2.7- 'Arena of Death' (Innova)
 * The version of the server : Aion-Knight 2.7 (Beta version)
 */

package admincommands;

import gameserver.configs.administration.AdminConfig;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_CUSTOM_PACKET;
import gameserver.network.aion.serverpackets.SM_CUSTOM_PACKET.PacketElementType;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class SendRawPacket extends AdminCommand
{
	private static final File ROOT = new File("data/packets/");
	private static final Logger logger = Logger.getLogger(SendRawPacket.class);

	public SendRawPacket()
	{
		super("raw");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_SENDRAWPACKET)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
			return;
		}

		if (params.length != 1)
		{
			PacketSendUtility.sendMessage(admin, "Usage: //raw [name]");
			return;
		}

		File file = new File(ROOT, params[0]+".txt");

		if (!file.exists() || !file.canRead())
		{
			PacketSendUtility.sendMessage(admin, "Wrong file selected.");
			return;
		}

		try
		{
			List<String> lines = FileUtils.readLines(file);

			SM_CUSTOM_PACKET packet = null;

			for (String row : lines)
			{
				String[] tokens = row.substring(0,48).trim().split(" ");
				int len = tokens.length;

				for (int i = 0; i < len; i++)
				{
					if (i == 0)
					{
						packet = new SM_CUSTOM_PACKET(Integer.valueOf(tokens[i], 16));
					}
					else if (i > 2)
					{
						packet.addElement(PacketElementType.C, "0x" + tokens[i]);
					}
				}
			}

			if (packet != null)
				PacketSendUtility.sendPacket(admin, packet);
		}
		catch(IOException e)
		{
			PacketSendUtility.sendMessage(admin, "An error has occurred.");
			logger.warn("IO Error.", e);
		}
	}
}