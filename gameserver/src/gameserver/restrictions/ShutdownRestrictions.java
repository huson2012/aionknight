/**
 * Эмулятор игрового сервера Aion 2.7 от команды разработчиков 'Aion-Knight Dev. Team' является 
 * свободным программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного программного 
 * обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой более поздней 
 * версии.
 * 
 * Программа распространяется в надежде, что она будет полезной, но БЕЗ КАКИХ БЫ ТО НИ БЫЛО 
 * ГАРАНТИЙНЫХ ОБЯЗАТЕЛЬСТВ; даже без косвенных  гарантийных  обязательств, связанных с 
 * ПОТРЕБИТЕЛЬСКИМИ СВОЙСТВАМИ и ПРИГОДНОСТЬЮ ДЛЯ ОПРЕДЕЛЕННЫХ ЦЕЛЕЙ. Для подробностей смотрите 
 * Стандартную Общественную Лицензию GNU.
 * 
 * Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе с этой программой. 
 * Если это не так, напишите в Фонд Свободного ПО (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * Веб-cайт разработчиков : http://aion-knight.ru
 * Поддержка клиента игры : Aion 2.7 - 'Арена Смерти' (Иннова) 
 * Версия серверной части : Aion-Knight 2.7 (Beta version)
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