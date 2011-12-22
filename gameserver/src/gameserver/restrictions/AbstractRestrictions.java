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

public abstract class AbstractRestrictions implements Restrictions
{
	public void activate()
	{
		RestrictionsManager.activate(this);
	}

	public void deactivate()
	{
		RestrictionsManager.deactivate(this);
	}

	@Override
	public int hashCode()
	{
		return getClass().hashCode();
	}

	/**
	 * To avoid accidentally multiple times activated restrictions.
	 */
	@Override
	public boolean equals(Object obj)
	{
		return getClass().equals(obj.getClass());
	}
	
	@DisabledRestriction
	public boolean isRestricted(Player player, Class<? extends Restrictions> callingRestriction)
	{
		throw new AbstractMethodError();
	}
	
	@DisabledRestriction
	public boolean canAttack(Player player, VisibleObject target)
	{
		throw new AbstractMethodError();
	}

	@DisabledRestriction
	public boolean canAffectBySkill(Player player, VisibleObject target)
	{
		throw new AbstractMethodError();
	}

	@DisabledRestriction
	public boolean canUseSkill(Player player, Skill skill)
	{
		throw new AbstractMethodError();
	}
	
	@DisabledRestriction
	public boolean canChat(Player player)
	{
		throw new AbstractMethodError();
	}
	
	@DisabledRestriction
	public boolean canInviteToGroup(Player player, Player target)
	{
		throw new AbstractMethodError();
	}
	
	@DisabledRestriction
	public boolean canChangeEquip(Player player)
	{
		throw new AbstractMethodError();
	}
	
	@DisabledRestriction
	public boolean canUseWarehouse(Player player)
	{
		throw new AbstractMethodError();
	}

	@DisabledRestriction
	public boolean canTrade(Player player)
	{
		throw new AbstractMethodError();
	}
	
	@DisabledRestriction
	public boolean canUseItem(Player player)
	{
		throw new AbstractMethodError();
	}
}