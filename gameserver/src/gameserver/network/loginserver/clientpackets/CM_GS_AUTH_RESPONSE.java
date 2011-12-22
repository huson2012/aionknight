/**   
 * �������� �������� ������� Aion 2.7 �� ������� ������������� 'Aion-Knight Dev. Team' �������� 
 * ��������� ����������� ������������; �� ������ �������������� �/��� �������� ��� �������� �������� 
 * ����������� ������������ �������� GNU (GNU GPL), �������������� ������ ���������� ������������ 
 * ����������� (FSF), ���� �������� ������ 3, ���� (�� ���� ����������) ����� ����� ������� 
 * ������.
 * 
 * ��������� ���������������� � �������, ��� ��� ����� ��������, �� ��� ����� �� �� �� ���� 
 * ����������� ������������; ���� ��� ���������  �����������  ������������, ��������� � 
 * ���������������� ���������� � ������������ ��� ������������ �����. ��� ������������ �������� 
 * ����������� ������������ �������� GNU.
 * 
 * �� ������ ���� �������� ����� ����������� ������������ �������� GNU ������ � ���� ����������. 
 * ���� ��� �� ���, �������� � ���� ���������� �� (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * ���-c��� ������������� : http://aion-knight.ru
 * ��������� ������� ���� : Aion 2.7 - '����� ������' (������) 
 * ������ ��������� ����� : Aion-Knight 2.7 (Beta version)
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