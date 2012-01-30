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

package gameserver.network.chatserver.clientpackets;

import commons.utils.ExitCode;
import gameserver.network.chatserver.ChatServerConnection.State;
import gameserver.network.chatserver.CsClientPacket;
import gameserver.network.chatserver.serverpackets.SM_CS_AUTH;
import gameserver.services.ChatService;
import gameserver.utils.ThreadPoolManager;
import org.apache.log4j.Logger;

public class CM_CS_AUTH_RESPONSE extends CsClientPacket
{
	/**
	 * Logger for this class.
	 */
	protected static final Logger log = Logger.getLogger(CM_CS_AUTH_RESPONSE.class);

	/**
	 * Response: 0=Authed,
	 * 1=NotAuthed,
	 * 2=AlreadyRegistered
	 */
	private int response;
	private byte[] ip;
	private int port;
	/**
	 * @param opcode
	 */
	public CM_CS_AUTH_RESPONSE(int opcode)
	{
		super(opcode);
	}

	@Override
	protected void readImpl()
	{
		response = readC();
		ip = readB(4);
		port = readH();
	}

	@Override
	protected void runImpl()
	{
		switch(response)
		{
			case 0: // Authed
				log.info("GameServer authed IP : "+(ip[0]& 0xFF)+ '.' +(ip[1] & 0xFF)+ '.' +(ip[2] & 0xFF)+ '.' +(ip[3] & 0xFF)+" Port: " +port);
				getConnection().setState(State.AUTHED);
				ChatService.setIp(ip);
				ChatService.setPort(port);
				break;
			case 1: // NotAuthed
				log.fatal("GameServer is not auth at ChatServer side");
				System.exit(ExitCode.CODE_ERROR);
				break;
			case 2: // AlreadyRegistered
				log.info("GameServer is already registered at ChatServer side! trying again...");
				ThreadPoolManager.getInstance().schedule(new Runnable(){
					@Override
					public void run()
					{
						CM_CS_AUTH_RESPONSE.this.getConnection().sendPacket(new SM_CS_AUTH());
					}

				}, 10000);
			break;
		}
	}
}
