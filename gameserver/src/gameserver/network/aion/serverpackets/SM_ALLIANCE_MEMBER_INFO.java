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

import gameserver.model.alliance.PlayerAllianceEvent;
import gameserver.model.alliance.PlayerAllianceMember;
import gameserver.model.gameobjects.player.PlayerCommonData;
import gameserver.model.gameobjects.stats.PlayerLifeStats;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.skill.model.Effect;
import gameserver.world.WorldPosition;
import java.nio.ByteBuffer;
import java.util.List;

public class SM_ALLIANCE_MEMBER_INFO extends AionServerPacket
{
	private PlayerAllianceMember member;
	private PlayerAllianceEvent event;
	
	public SM_ALLIANCE_MEMBER_INFO(PlayerAllianceMember member, PlayerAllianceEvent event)
	{
		this.member = member;
		this.event = event;
	}
	
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		PlayerCommonData pcd = member.getCommonData();
		WorldPosition wp = pcd.getPosition();

		if (!member.isOnline())
			event = PlayerAllianceEvent.DISCONNECTED;
		
		writeD(buf, member.getAllianceId());
		writeD(buf, member.getObjectId());
		if (member.isOnline())
		{
			PlayerLifeStats pls = member.getPlayer().getLifeStats();
			writeD(buf, pls.getMaxHp());
			writeD(buf, pls.getCurrentHp());
			writeD(buf, pls.getMaxMp());
			writeD(buf, pls.getCurrentMp());
			writeD(buf, pls.getMaxFp());
			writeD(buf, pls.getCurrentFp());
		}
		else
		{
			writeD(buf, 0);
			writeD(buf, 0);
			writeD(buf, 0);
			writeD(buf, 0);
			writeD(buf, 0);
			writeD(buf, 0);
		}
		writeD(buf, wp.getMapId());
		writeD(buf, wp.getMapId());
		writeF(buf, wp.getX());
		writeF(buf, wp.getY());
		writeF(buf, wp.getZ());
		writeC(buf, pcd.getPlayerClass().getClassId());
		writeC(buf, pcd.getGender().getGenderId());
		writeC(buf, pcd.getLevel());
		writeC(buf, this.event.getId());
		writeH(buf, 0x01);
		writeC(buf, 0x00);
		switch(this.event)
		{
			case LEAVE:
			case LEAVE_TIMEOUT:
			case BANNED:
			case MOVEMENT:
			case DISCONNECTED:
				break;				
			case ENTER:
			case UPDATE:
			case RECONNECT:
			case MEMBER_GROUP_CHANGE:				
			case APPOINT_VICE_CAPTAIN:
			case DEMOTE_VICE_CAPTAIN:
			case APPOINT_CAPTAIN:
				writeS(buf, pcd.getName());
				writeD(buf, 0x00);
                writeD(buf, 0x00);
				
				if (member.isOnline())
				{
					List<Effect> abnormalEffects = member.getPlayer().getEffectController().getAbnormalEffects();
					writeH(buf, abnormalEffects.size());
					for(Effect effect : abnormalEffects)
					{
						writeD(buf, effect.getEffectorId());
						writeH(buf, effect.getSkillId());
						writeC(buf, effect.getSkillLevel());
						writeC(buf, effect.getTargetSlot());
						writeD(buf, effect.getElapsedTime());
					}
				}
				else
				{
					writeH(buf, 0);
				}
				break;
			default:
				break;
		}
	}	
}