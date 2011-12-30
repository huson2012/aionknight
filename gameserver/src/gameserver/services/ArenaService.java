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

package gameserver.services;

import gameserver.controllers.SummonController.UnsummonType;
import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Summon;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.zone.ZoneName;

public class ArenaService
{
	public static final ArenaService getInstance()
	{
		return SingletonHolder.instance;
	}
	
	public boolean isEnemy(Player effector, Player effected)
	{
		if(effector == effected)
			return false;
		if(!isInSameGroup(effector, effected) && !isInSameAlliance(effector, effected) && effector.getInArena() && effected.getInArena())
			return true;
			
		return false;
	}
	
	public void onDie(final Player player, Creature lastAttacker)
	{
		player.getEffectController().removeAllEffects();
		player.getController().cancelCurrentSkill();
		
		Summon summon = player.getSummon();
		if(summon != null)
			summon.getController().release(UnsummonType.UNSPECIFIED);

		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, lastAttacker == null ? 0 : lastAttacker.getObjectId()), true);
		
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.DIE);
		player.getObserveController().notifyDeath(player);
		
		ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				if(isInTrinielArena(player))
					TeleportService.teleportTo(player, 120010000, 1, 1005.1f, 1528.9f, 222.1f, 0);
				if(isInSanctumArena(player))
					TeleportService.teleportTo(player, 110010000, 1, 1470.3f, 1343.5f, 563.7f, 0);
			}
		}, 5000);
	}
	
	public boolean isInArena(Player player)
	{
		if(isInTrinielArena(player) || isInSanctumArena(player))
			return true;
		return false;
	}
	
	private boolean isInTrinielArena(Player player)
	{
		int world = player.getWorldId();
		if(world == 120010000 && ZoneService.getInstance().isInsideZone(player, ZoneName.TRINIEL_PVP_ZONE))
			return true;		
		return false;
	}
	
	private boolean isInSanctumArena(Player player)
	{
		int world = player.getWorldId();
		if(world == 110010000 && ZoneService.getInstance().isInsideZone(player, ZoneName.COLISEUM_PVP_ZONE))
			return true;
		return false;
	}
	
	public boolean isInSameGroup(Player player1, Player player2)
	{
		if(player1.isInGroup() && player2.isInGroup())
		{
			if(player1.getPlayerGroup().getGroupId() == player2.getPlayerGroup().getGroupId())
				return true;
		}
		return false;
	}
	
	public boolean isInSameAlliance(Player player1, Player player2)
	{
		if(player1.isInAlliance() && player2.isInAlliance())
		{
			if(player1.getPlayerAlliance().getAllianceIdFor(player1.getObjectId()) == player2.getPlayerAlliance().getAllianceIdFor(player2.getObjectId()))
				return true;
		}
		return false;
	}
	
	@SuppressWarnings("synthetic-access")
	public static class SingletonHolder
	{
		protected static final ArenaService instance = new ArenaService();
	}
}