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

import gameserver.model.siege.Influence;
import gameserver.model.siege.SiegeLocation;
import gameserver.model.siege.SiegeType;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.services.SiegeService;
import javolution.util.FastList;
import java.nio.ByteBuffer;

public class SM_FORTRESS_STATUS extends AionServerPacket
{
	public SM_FORTRESS_STATUS()
	{
	}
	
	@Override
	public void writeImpl(AionConnection con, ByteBuffer buf)
	{
		FastList<SiegeLocation> validLocations = new FastList<SiegeLocation>();
		
		for(SiegeLocation loc : SiegeService.getInstance().getSiegeLocations().values())
		{
			if(loc.getSiegeType() == SiegeType.FORTRESS)
			{
				validLocations.add(loc);
			}
		}
		
		writeC(buf, 1); //unk
		writeD(buf, SiegeService.getInstance().getSiegeTime());
		writeF(buf, Influence.getInstance().getElyos());
		writeF(buf, Influence.getInstance().getAsmos());
		writeF(buf, Influence.getInstance().getBalaur());
		
		writeH(buf, 3); //map count
		
		writeD(buf, 210050000);
		writeF(buf, Influence.getInstance().getElyos());
		writeF(buf, Influence.getInstance().getAsmos());
		writeF(buf, Influence.getInstance().getBalaur());
		
		writeD(buf, 220070000);
		writeF(buf, Influence.getInstance().getElyos());
		writeF(buf, Influence.getInstance().getAsmos());
		writeF(buf, Influence.getInstance().getBalaur());
		
		writeD(buf, 400010000);
		writeF(buf, Influence.getInstance().getElyos());
		writeF(buf, Influence.getInstance().getAsmos());
		writeF(buf, Influence.getInstance().getBalaur());
		
		writeD(buf, 0);
		writeD(buf, 0);
		writeD(buf, 0);
		writeD(buf, 0);
		
		writeH(buf, validLocations.size());
		
		for(SiegeLocation loc : validLocations)
		{
			writeD(buf, loc.getLocationId());
			writeC(buf, 1); //unk
		}
	}
}
