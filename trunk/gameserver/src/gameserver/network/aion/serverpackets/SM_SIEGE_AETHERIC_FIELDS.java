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

import gameserver.model.siege.SiegeLocation;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;
import java.util.Collection;

public class SM_SIEGE_AETHERIC_FIELDS extends AionServerPacket
{
	
	private Collection<SiegeLocation> locations;

	public SM_SIEGE_AETHERIC_FIELDS(Collection<SiegeLocation> locations)
	{
		this.locations = locations;
	}
	
	@Override
	public void writeImpl(AionConnection con, ByteBuffer buf)
	{		
		writeH(buf, locations.size()); // Artifact Count
		
		for(SiegeLocation loc : locations)
		{
			writeD(buf, loc.getLocationId());
			if(loc.isVulnerable() && loc.isShieldActive()) // display shield if vulnerable and generator alive	
				writeC(buf, 1);
			else
				writeC(buf,0); // peace mode
		}
	}
}
