/**
 * ������� �������� �� ������� ������������� 'Aion-Knight Dev. Team' �������� ��������� 
 * ����������� ������������; �� ������ �������������� �/��� �������� ��� �������� �������� 
 * ����������� ������������ �������� GNU (GNU GPL), �������������� ������ ���������� 
 * ������������ ����������� (FSF), ���� �������� ������ 3, ���� (�� ���� ����������) ����� 
 * ����� ������� ������.
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

package chatserver.network.gameserver.clientpackets;

import org.jboss.netty.buffer.ChannelBuffer;
import chatserver.network.gameserver.AbstractGameClientPacket;
import chatserver.network.gameserver.GsAuthResponse;
import chatserver.network.gameserver.serverpackets.SM_GS_AUTH_RESPONSE;
import chatserver.network.netty.handler.GameChannelHandler;
import chatserver.network.netty.handler.GameChannelHandler.State;
import chatserver.service.GameServerService;

public class CM_CS_AUTH extends AbstractGameClientPacket
{
	/**
	 * Password for authentication
	 */
	private String				password;

	/**
	 * Id of GameServer
	 */
	private byte				gameServerId;

	/**
	 * Default address for server
	 */
	private byte[]				defaultAddress;

	public CM_CS_AUTH(ChannelBuffer buf, GameChannelHandler gameChannelHandler)
	{
		super(buf, gameChannelHandler, 0x00);
	}

	@Override
	protected void readImpl()
	{
		gameServerId = (byte) readC();

		defaultAddress = readB(readC());
		password = readS();
	}

	@Override
	protected void runImpl()
	{
		GsAuthResponse resp = GameServerService.registerGameServer(gameChannelHandler, gameServerId, defaultAddress,
			password);

		switch (resp)
		{
			case AUTHED:
				gameChannelHandler.setState(State.AUTHED);
				gameChannelHandler.sendPacket(new SM_GS_AUTH_RESPONSE(resp));
				break;
			case NOT_AUTHED:
				gameChannelHandler.sendPacket(new SM_GS_AUTH_RESPONSE(resp));
				break;
			default:
				gameChannelHandler.close(new SM_GS_AUTH_RESPONSE(resp));
		}
	}
}