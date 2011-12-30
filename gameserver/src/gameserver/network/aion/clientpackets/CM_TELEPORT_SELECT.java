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

package gameserver.network.aion.clientpackets;

import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.teleport.TelelocationTemplate;
import gameserver.model.templates.teleport.TeleporterTemplate;
import gameserver.network.aion.AionClientPacket;
import gameserver.services.TeleportService;
import gameserver.utils.MathUtil;
import gameserver.world.World;
import org.apache.log4j.Logger;

public class CM_TELEPORT_SELECT extends AionClientPacket
{
	/** NPC ID */
	public int targetObjectId;

	/** Destination of teleport */
	public int locId;

	public TelelocationTemplate _tele;

	private TeleporterTemplate teleport;

	public CM_TELEPORT_SELECT(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		// empty
		targetObjectId = readD();
		locId = readD(); //locationId
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		Player activePlayer = getConnection().getActivePlayer();

		Npc npc = (Npc)World.getInstance().findAionObject(targetObjectId);

		if(activePlayer == null || activePlayer.getLifeStats().isAlreadyDead())
			return;
		
		if(!MathUtil.isIn3dRange(npc, activePlayer, 10))
		{
			Logger.getLogger(this.getClass()).info("[AUDIT]Player "+activePlayer.getName()+" sending fake CM_TELEPORT_SELECT!");
			return;
		}
		
		teleport = DataManager.TELEPORTER_DATA.getTeleporterTemplate(npc.getNpcId());

		switch(teleport.getType())
		{
			case FLIGHT:
				TeleportService.flightTeleport(teleport, locId, activePlayer);
			break;
			case REGULAR:
				TeleportService.regularTeleport(teleport, locId, activePlayer);
			break;
			default:
		}
	}
}