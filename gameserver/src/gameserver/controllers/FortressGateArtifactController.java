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

import gameserver.model.DescriptionId;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.RequestResponseHandler;
import gameserver.model.siege.FortressGateArtifact;
import gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import gameserver.utils.PacketSendUtility;

public class FortressGateArtifactController extends NpcController
{	

	@Override
	public void onDialogRequest(final Player player)
	{
		RequestResponseHandler artifactHandler = new RequestResponseHandler(player){
			
			@Override
			public void denyRequest(Creature requester, Player responder)
			{
				// Close window
			}
			
			@Override
			public void acceptRequest(Creature requester, Player responder)
			{
				RequestResponseHandler acceptItem = new RequestResponseHandler(player){
					
					@Override
					public void denyRequest(Creature requester, Player responder)
					{
						// Refuse item do nothing
					}
					
					@Override
					public void acceptRequest(Creature requester, Player responder)
					{
						onActivate(player);
					}
				};
				if(player.getResponseRequester().putRequest(160016, acceptItem))
				{
					PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(160016, player.getObjectId(), new DescriptionId(2*716568+1)));
				}
			}
		};
		if(player.getResponseRequester().putRequest(160027, artifactHandler))
		{
			PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(160027, player.getObjectId()));
		}
	}
	
	public void onActivate(Player player)
	{
		
	}
	
	@Override
	public void onRespawn()
	{
		super.onRespawn();
	}

	@Override
	public FortressGateArtifact getOwner()
	{
		return (FortressGateArtifact) super.getOwner();
	}
}
