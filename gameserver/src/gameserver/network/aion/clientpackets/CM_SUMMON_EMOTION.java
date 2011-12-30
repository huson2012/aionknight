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

import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Summon;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.state.CreatureState;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.utils.PacketSendUtility;
import org.apache.log4j.Logger;

public class CM_SUMMON_EMOTION extends AionClientPacket
{
	private static final Logger	log	= Logger.getLogger(CM_SUMMON_EMOTION.class);
	
	@SuppressWarnings("unused")
	private int objId;
	
	private int emotionTypeId;
	
	public CM_SUMMON_EMOTION(int opcode)
	{
		super(opcode);
	}

	@Override
	protected void readImpl()
	{
		objId = readD();
		emotionTypeId = readC();
	}

	@Override
	protected void runImpl()
	{
		EmotionType emotionType = EmotionType.getEmotionTypeById(emotionTypeId);
		
		// Unknown Summon Emotion Type
		if(emotionType == EmotionType.UNK)
			log.error("Unknown emotion type? 0x" + Integer.toHexString(emotionTypeId).toUpperCase());
		
		Player activePlayer = getConnection().getActivePlayer();
		if(activePlayer == null) return;
		
		Summon summon = activePlayer.getSummon();
		if (summon == null) return;
			
		switch(emotionType)
		{
			case FLY:
			case LAND:
				PacketSendUtility.broadcastPacket(summon, new SM_EMOTION(summon, emotionType));
				break;
			case ATTACKMODE: //start attacking
				summon.setState(CreatureState.WEAPON_EQUIPPED);
				PacketSendUtility.broadcastPacket(summon, new SM_EMOTION(summon, emotionType));
				break;
			case NEUTRALMODE: //stop attacking
				summon.unsetState(CreatureState.WEAPON_EQUIPPED);
				PacketSendUtility.broadcastPacket(summon, new SM_EMOTION(summon, emotionType));
				break;
		}	
	}
}