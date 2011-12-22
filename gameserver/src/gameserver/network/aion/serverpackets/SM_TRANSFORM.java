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

import gameserver.model.gameobjects.Creature;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.model.gameobjects.player.Player;
import gameserver.utils.PacketSendUtility;
import java.nio.ByteBuffer;

public class SM_TRANSFORM extends AionServerPacket
{
	private Creature creature;
	private int	state;

	public SM_TRANSFORM(Creature creature)
	{
		this.creature = creature;
		this.state = creature.getState();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, creature.getObjectId());
		writeD(buf, creature.getTransformedModelId());
		writeH(buf, state);
		writeF(buf, 0.55f);
		writeF(buf, 1.5f);
		writeC(buf, 0);
		writeD(buf, 1);
        writeD(buf, 0);
        writeH(buf, 0);
        writeC(buf, 0);
		
		Player player;
		try
		{
			player = (Player)creature;
		}
		catch(ClassCastException e)
		{
			return;
		}

		int raceId = player.getCommonData().getRace().getRaceId();

		switch(creature.getTransformedModelId())
		{
			case 0:
				switch(player.getAbyssRank().getRank().getId())
				{
					case 14:
						PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11895, false));
						break;
					case 15:
						if(raceId == 0){
							PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11899, false));
						}else{
							PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11901, false));
						}
						PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11896, false));
						break;
					case 16:
						if(raceId == 0){
							PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11899, false));
						}else{
							PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11901, false));
						}
						PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11897, false));
						PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11903, false));
						break;
					case 17:
						if(raceId == 0){
							PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11900, false));
						}else{
							PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11902, false));
						}
						PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11898, false));
						PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11903, false));
						PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11904, false));
						break;
					case 18:
						if(raceId == 0){
							PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11900, false));
						}else{
							PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11902, false));
						}
						PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11898, false));
						PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11903, false));
						PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11904, false));
						PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11905, false));
						PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11906, false));
						break;
				}
				break;
			case 202502: // trans I Elyos
			case 202507: // trans I Asmo
				PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11895, true));
				break;
			case 202503: // trans II Elyos
				PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11896, true));
				PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11899, true));
				break;
			case 202508: // trans II Asmo
				PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11896, true));
				PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11901, true));
				break;
			case 202504: // trans III Elyos
				PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11899, true));
				PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11897, true));
				PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11903, true));
				break;
			case 202509: // trans III Asmo
				PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11901, true));
				PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11897, true));
				PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11903, true));
				break;
			case 202505: // trans IV Elyos
				PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11900, true));
				PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11898, true));
				PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11903, true));
				PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11904, true));
				break;
			case 202510: // trans IV Asmo
				PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11902, true));
				PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11898, true));
				PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11903, true));
				PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11904, true));
				break;
			case 202506: // trans V Elyos
				PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11900, true));
				PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11898, true));
				PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11903, true));
				PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11904, true));
				PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11905, true));
				PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11906, true));
				break;
			case 202511: // trans V Asmo
				PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11902, true));
				PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11898, true));
				PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11903, true));
				PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11904, true));
				PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11905, true));
				PacketSendUtility.sendPacket(player, new SM_SKILL_ACTIVATION(11906, true));
				break;
		}
	}
}