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

package chatserver.network.gameserver;

import org.jboss.netty.buffer.ChannelBuffer;
import chatserver.common.netty.BaseServerPacket;
import chatserver.network.netty.handler.GameChannelHandler;

public abstract class AbstractGameServerPacket extends BaseServerPacket
{
	/**
	 * @param opCode
	 */
	public AbstractGameServerPacket(int opCode)
	{
		super(opCode);
	}
	
	/**
	 * @param gameChannelHandler
	 * @param buf
	 */
	public void write(GameChannelHandler gameChannelHandler, ChannelBuffer buf)
	{
		writeH(buf, 0);
		writeImpl(gameChannelHandler, buf);
	}
	
	/**
	 * @param gameChannelHandler
	 * @param buf
	 */
	protected abstract void writeImpl(GameChannelHandler gameChannelHandler, ChannelBuffer buf);
	
}