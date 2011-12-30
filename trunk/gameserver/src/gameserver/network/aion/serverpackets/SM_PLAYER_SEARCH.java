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

import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import org.apache.log4j.Logger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Sent to fill the search panel of a players social window<br />
 * I.E.: In response to a <tt>CM_PLAYER_SEARCH</tt>
 */
public class SM_PLAYER_SEARCH extends AionServerPacket
{
	private static final Logger log = Logger.getLogger(SM_PLAYER_SEARCH.class);
	
	private List<Player> players;
	private int region;
	private int status;
	
	/**
	 * Constructs a new packet that will send these players
	 * @param players List of players to show
	 * @param region of search - should be passed as parameter
	 * to prevent null in player.getActiveRegion()
	 * 
	 */
	public SM_PLAYER_SEARCH(List<Player> players, int region)
	{
		this.players = new ArrayList<Player>(players);
		this.region = region;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeH(buf, players.size());
		for (Player player : players)
		{
			if(player.getActiveRegion() == null)
			{
				log.warn("CHECKPOINT: null active region for " + player.getObjectId() 
					+ "-" + player.getX() + "-" + player.getY() + "-" + player.getZ());
			}
			writeD(buf, player.getActiveRegion() == null ? region : player.getActiveRegion().getMapId());
			writeF(buf, player.getPosition().getX());
			writeF(buf, player.getPosition().getY());
			writeF(buf, player.getPosition().getZ());
			writeC(buf, player.getPlayerClass().getClassId());
			writeC(buf, player.getGender().getGenderId());
			writeC(buf, player.getLevel());
			if(player.isLookingForGroup())
				status = 2;
			else if(player.isInGroup())
				status = 3;
			else
				status = 0;
			writeC(buf, status); // Status. 2 = LFG, 3 = In group, others = solo
			writeS(buf, player.getName());
			byte[] unknown = new byte[52 - (player.getName().length()*2 +2)]; // What on earth is this nonsense?
			writeB(buf, unknown);
			
		}
	}
	
}
