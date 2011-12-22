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