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

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import chatserver.model.ChatClient;
import chatserver.network.gameserver.AbstractGameClientPacket;
import chatserver.network.gameserver.serverpackets.SM_PLAYER_AUTH_RESPONSE;
import chatserver.network.netty.handler.GameChannelHandler;
import chatserver.service.ChatService;

public class CM_PLAYER_AUTH extends AbstractGameClientPacket
{
	private static final Logger	log	= Logger.getLogger(CM_PLAYER_AUTH.class);

	private int					playerId;
	
	private String				playerLogin;

	public CM_PLAYER_AUTH(ChannelBuffer buf, GameChannelHandler gameChannelHandler)
	{
		super(buf, gameChannelHandler, 0x01);
	}

	@Override
	protected void readImpl()
	{
		playerId = readD();
		playerLogin = readS();
	}

	@Override
	protected void runImpl()
	{
		ChatClient chatClient = null;
		try
		{
			chatClient = ChatService.getInstance().registerPlayer(playerId, playerLogin);
		}
		catch (NoSuchAlgorithmException e)
		{
			log.error("Error registering player on ChatServer: " + e.getMessage());
		}
		catch (UnsupportedEncodingException e)
		{
			log.error("Error registering player on ChatServer: " + e.getMessage());
		}

		if (chatClient != null)
		{
			gameChannelHandler.sendPacket(new SM_PLAYER_AUTH_RESPONSE(chatClient));
		}
		else
		{
			log.info("Player was not authed " + playerId);
		}
	}
}