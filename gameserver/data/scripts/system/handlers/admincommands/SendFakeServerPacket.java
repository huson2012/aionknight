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

package admincommands;

import gameserver.configs.administration.AdminConfig;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_CUSTOM_PACKET;
import gameserver.network.aion.serverpackets.SM_CUSTOM_PACKET.PacketElementType;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;

public class SendFakeServerPacket extends AdminCommand
{
	public SendFakeServerPacket()
	{
		super("fsc");
	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_SENDFAKESERVERPACKET)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
			return;
		}

		if (params.length < 3)
		{
			PacketSendUtility.sendMessage(admin, "Incorrent number of params in //fsc command");
			return;
		}

		int id = Integer.decode(params[0]);
		String format = "";

		if (params.length > 1)
			format = params[1];

		SM_CUSTOM_PACKET packet = new SM_CUSTOM_PACKET(id);

		int i = 0;
		for (char c : format.toCharArray())
		{
			packet.addElement(PacketElementType.getByCode(c), params[i + 2]);
			i++;
		}
		PacketSendUtility.sendPacket(admin, packet);
	}
}