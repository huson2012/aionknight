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

package gameserver.restrictions;

import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.skill.model.Skill;
import gameserver.utils.PacketSendUtility;

public class ShutdownRestrictions extends AbstractRestrictions
{
	@Override
	public boolean isRestricted(Player player, Class<? extends Restrictions> callingRestriction)
	{
		if(isInShutdownProgress(player))
		{
			PacketSendUtility.sendMessage(player, "You are in shutdown progress!");
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean canAttack(Player player, VisibleObject target)
	{
		if(isInShutdownProgress(player))
		{
			PacketSendUtility.sendMessage(player, "You cannot attack in Shutdown progress!");
			return false;
		}

		return true;
	}

	@Override
	public boolean canAffectBySkill(Player player, VisibleObject target)
	{
		return true;
	}
	
	@Override
	public boolean canUseSkill(Player player, Skill skill)
	{
		if(isInShutdownProgress(player))
		{
			PacketSendUtility.sendMessage(player, "You cannot use skills in Shutdown progress!");
			return false;
		}

		return true;
	}

	@Override
	public boolean canChat(Player player)
	{
		if(isInShutdownProgress(player))
		{
			PacketSendUtility.sendMessage(player, "You cannot chat in Shutdown progress!");
			return false;
		}

		return true;
	}

	@Override
	public boolean canInviteToGroup(Player player, Player target)
	{
		if(isInShutdownProgress(player))
		{
			PacketSendUtility.sendMessage(player, "You cannot invite members to group in Shutdown progress!");
			return false;
		}

		return true;
	}

	@Override
	public boolean canInviteToAlliance(Player player, Player target)
	{
		if(isInShutdownProgress(player))
		{
			PacketSendUtility.sendMessage(player, "You cannot invite members to alliance in Shutdown progress!");
			return false;
		}

		return true;
	}
	
	@Override
	public boolean canChangeEquip(Player player)
	{
		if(isInShutdownProgress(player))
		{
			PacketSendUtility.sendMessage(player, "You cannot equip / unequip item in Shutdown progress!");
			return false;
		}
		
		return true;
	}
	
	private boolean isInShutdownProgress(Player player)
	{
		return player.getController().isInShutdownProgress();
	}
}