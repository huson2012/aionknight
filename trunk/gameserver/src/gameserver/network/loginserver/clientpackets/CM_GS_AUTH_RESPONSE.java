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

import commons.utils.ExitCode;
import gameserver.network.loginserver.LoginServer;
import gameserver.network.loginserver.LoginServerConnection.State;
import gameserver.network.loginserver.LsClientPacket;
import gameserver.network.loginserver.serverpackets.SM_ACCOUNT_LIST;
import gameserver.network.loginserver.serverpackets.SM_GS_AUTH;
import gameserver.utils.ThreadPoolManager;
import org.apache.log4j.Logger;

/**
 * This packet is response for SM_GS_AUTH its notify Gameserver if registration was ok or what was wrong.
 */
public class CM_GS_AUTH_RESPONSE extends LsClientPacket
{
	/**
	 * Logger for this class.
	 */
	protected static final Logger	log	= Logger.getLogger(CM_GS_AUTH_RESPONSE.class);

	/**
	 * Response: 0=Authed,1=NotAuthed,2=AlreadyRegistered
	 */
	private int						response;

	/**
	 * Constructs new instance of <tt>CM_GS_AUTH_RESPONSE </tt> packet.
	 * @param opcode
	 */
	public CM_GS_AUTH_RESPONSE(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		response = readC();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		/**
		 * Authed
		 */
		if(response == 0)
		{
			getConnection().setState(State.AUTHED);
			sendPacket(new SM_ACCOUNT_LIST(LoginServer.getInstance().getLoggedInAccounts()));
		}

		/**
		 * NotAuthed
		 */
		else if(response == 1)
		{
			log.fatal("GameServer is not authenticated at LoginServer side, shutting down!");
			System.exit(ExitCode.CODE_ERROR);
		}
		/**
		 * AlreadyRegistered
		 */
		else if(response == 2)
		{
			log.info("GameServer is already registered at LoginServer side! trying again...");
			/**
			 * try again after 10s
			 */
			ThreadPoolManager.getInstance().schedule(new Runnable(){
				@Override
				public void run()
				{
					CM_GS_AUTH_RESPONSE.this.getConnection().sendPacket(new SM_GS_AUTH());
				}

			}, 10000);
		}
	}
}