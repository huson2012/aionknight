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

package gameserver.controllers;

import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.modifiers.Executor;
import gameserver.model.siege.AethericField;
import gameserver.model.siege.SiegeLocation;
import gameserver.network.aion.serverpackets.SM_SIEGE_LOCATION_INFO;
import gameserver.services.SiegeService;
import gameserver.utils.PacketSendUtility;

public class AethericFieldController extends NpcController
{	
	public void onDie(Creature lastAttacker)
	{
		super.onDie(lastAttacker);
		int id = getOwner().getFortressId();
		SiegeLocation loc = SiegeService.getInstance().getSiegeLocation(id);
		//disable field
		loc.setShieldActive(false);
		//TODO : Find sys message sended on generator death
		getOwner().getKnownList().doOnAllPlayers(new Executor<Player>(){		
			@Override
			public boolean run(Player object)
			{
				//Needed to update the display of shield effect
				PacketSendUtility.sendPacket(object, new SM_SIEGE_LOCATION_INFO());
				return true;
			}
		}, true);
	}

	@Override
	public AethericField getOwner()
	{
		return (AethericField) super.getOwner();
	}	
}