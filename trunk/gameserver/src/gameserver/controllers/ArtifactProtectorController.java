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
import gameserver.model.gameobjects.Summon;
import gameserver.model.gameobjects.Trap;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.siege.ArtifactProtector;
import gameserver.services.SiegeService;

public class ArtifactProtectorController extends NpcController
{

	@Override
	public void onRespawn()
	{
		super.onRespawn();
	}
	
	@Override
	public void onStartMove()
	{
		super.onStartMove();
	}
	
	@Override
	public void onMove()
	{
		super.onMove();
	}
	
	@Override
	public void onStopMove()
	{
		super.onStopMove();
	}

	@Override
	public void onDie(Creature lastAttacker)
	{
		if(lastAttacker instanceof Player || lastAttacker instanceof Summon || lastAttacker instanceof Trap)
		{
			Player taker;
			if(lastAttacker instanceof Player)
				taker = (Player)lastAttacker;
			else if(lastAttacker instanceof Summon)
				taker = ((Summon)lastAttacker).getMaster();
			else if(lastAttacker instanceof Trap)
				taker = (Player)((Trap)lastAttacker).getCreator();
			else
				taker = null;
			
			if(taker != null)			
				SiegeService.getInstance().onArtifactCaptured(getOwner().getArtifact(), taker);
		}
		else
		{
			// Taken by Balaur
			if(lastAttacker != null)
				SiegeService.getInstance().onArtifactCaptured(getOwner().getArtifact());
		}
		super.onDie(lastAttacker);
	}

	@Override
	public ArtifactProtector getOwner()
	{
		return (ArtifactProtector) super.getOwner();
	}
}
