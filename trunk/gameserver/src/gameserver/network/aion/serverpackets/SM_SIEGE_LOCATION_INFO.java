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

import gameserver.configs.main.SiegeConfig;
import gameserver.model.siege.SiegeLocation;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.services.SiegeService;
import javolution.util.FastMap;
import java.nio.ByteBuffer;
import java.util.Map;

// SM_SIEGE_LOCATION_INFO
public class SM_SIEGE_LOCATION_INFO extends AionServerPacket
{
	/***
	 * infoType
	 * 0 - reset
	 * 1 - change
	 */
	private int infoType;
	
	private Map<Integer, SiegeLocation> locations;
	
	public SM_SIEGE_LOCATION_INFO()
	{
		this.infoType = 0; // Reset
		locations = SiegeService.getInstance().getSiegeLocations();
	}
	
	/**
	 * @param loc
	 */
	public SM_SIEGE_LOCATION_INFO(SiegeLocation loc)
	{
		this.infoType = 1; // Update
		locations = new FastMap<Integer, SiegeLocation>();
		locations.put(loc.getLocationId(), loc);
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		if (SiegeConfig.SIEGE_ENABLED == false)
		{
			// Siege is Disabled
			writeC(buf, 0);
			writeH(buf, 0);
			return;
		}
		
		writeC(buf, infoType);
		writeH(buf, locations.size());

		for(SiegeLocation sLoc : locations.values())
		{	
			writeD(buf, sLoc.getLocationId()); // Artifact ID
			
			writeD(buf, sLoc.getLegionId()); // Legion ID
			
			writeD(buf, 0);		
			
			writeD(buf, 0); // unk
			
			writeC(buf, sLoc.getRace().getRaceId());
			
			// is vulnerable (0 - no, 2 - yes)
			writeC(buf, sLoc.isVulnerable() ? 2 : 0);
			
			 // faction can teleport (0 - no, 1 - yes)
			writeC(buf, 1);
			
			// Next State (0 - invulnerable, 1 - vulnerable)
			writeC(buf, sLoc.getNextState());
			
			writeH(buf, 0); // unk
			writeH(buf, 1);
			switch(sLoc.getLocationId())
			{
				case 2111:
					writeD(buf, sLoc.isVulnerable() ? 2*60*60 : 0);// mastarius & veille timer
					break;
				case 3111:
					writeD(buf, sLoc.isVulnerable() ? 2*60*60 : 0); // mastarius & veille timer
					break;
				default:
					writeD(buf, 0); // mastarius & veille timer
					break;
			}
		}
	}
}
