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

package gameserver.network.aion.serverpackets;

import gameserver.model.gameobjects.player.Cmotion;
import gameserver.model.gameobjects.player.CmotionList;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_CMOTION extends AionServerPacket
{
	private CmotionList	cmotionList;
	private byte 		control;
	private int		 	active;
	private int 		cmotionid;
	
	/**
	 * �������� ������
	 */
	public SM_CMOTION(Player player)
	{
		this.control	= 1;
		this.cmotionList 	= player.getCmotionList();
	}
	
	/**
	 * {@inheritDoc}
	 */	
	public SM_CMOTION(Player player, int active, int id)
	{
		this.cmotionid  = id;
		this.active 	= active;
		this.control	= 2;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override	
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		switch (control)
		{
			case 1:
			{
				writeC(buf, 0x01); 
				writeH(buf, 0x09);
				for(Cmotion cmotion : cmotionList.getCmotions())
				{  
					writeC(buf, cmotion.getCmotionId());
					writeD(buf, 0);
					writeH(buf, 0);				
				}
			}
			break;
			case 2:
			{
				writeC(buf, 0x05); 
				writeH(buf, cmotionid);
				writeC(buf, active);
			}
			break;
		}
	}
}