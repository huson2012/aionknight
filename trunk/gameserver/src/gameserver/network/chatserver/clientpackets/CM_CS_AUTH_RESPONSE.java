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
				log.info("GameServer authed IP : "+(ip[0]& 0xFF)+"."+(ip[1] & 0xFF)+"."+(ip[2] & 0xFF)+"."+(ip[3] & 0xFF)+" Port: " +port);
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