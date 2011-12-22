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

package gameserver.controllers;

import gameserver.controllers.attack.AttackStatus;
import gameserver.model.EmotionType;
import gameserver.model.TaskId;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Kisk;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.RequestResponseHandler;
import gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.services.KiskService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.World;
import java.util.List;

public class KiskController extends NpcController
{
	@Override
	public void onAttack(Creature creature, int skillId, TYPE type, int damage, int logId, AttackStatus status, boolean notifyAttackedObservers, boolean sendPacket)
	{
		Kisk kisk = (Kisk)this.getOwner();

		if (kisk.getLifeStats().isFullyRestoredHp())
		{
			List<Player> members = kisk.getCurrentMemberList();
			for(Player member : members)
			{
				PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.STR_BINDSTONE_IS_ATTACKED);
			}
		}
		
		super.onAttack(creature, skillId, type, damage, logId, status, notifyAttackedObservers, sendPacket);
		
	}
	
	@Override
	public void onDespawn(boolean forced)
	{
		final Kisk kisk = (Kisk)this.getOwner();
		kisk.broadcastPacket(SM_SYSTEM_MESSAGE.STR_BINDSTONE_IS_REMOVED);
		removeKisk(kisk);
	}
	
	@Override
	public void onDie(Creature lastAttacker)
	{
		final Kisk kisk = (Kisk)this.getOwner();
		PacketSendUtility.broadcastPacket(kisk, new SM_EMOTION(kisk, EmotionType.DIE, 0, 0));
		kisk.broadcastPacket(SM_SYSTEM_MESSAGE.STR_BINDSTONE_IS_DESTROYED);
		removeKisk(kisk);
	}
	
	private void removeKisk(final Kisk kisk)
	{
		KiskService.removeKisk(kisk);
		
		// Schedule World Removal
		addTask(TaskId.DECAY, ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				if (kisk != null && kisk.isSpawned())
					World.getInstance().despawn(kisk);
			}
		}, 3 * 1000));
	}
	
	@Override
	public void onDialogRequest(Player player)
	{
		final Kisk kisk = (Kisk)this.getOwner();
		
		if (player.getKisk() == kisk)
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_BINDSTONE_ALREADY_REGISTERED);
			return;
		}
		
		if (kisk.canBind(player))
		{
			RequestResponseHandler responseHandler = new RequestResponseHandler(kisk) {
				
				@Override
				public void acceptRequest(Creature requester, Player responder)
				{
					Kisk kisk = (Kisk)requester;
					
					// Check again if it's full (If they waited to press OK)
					if (!kisk.canBind(responder))
					{
						PacketSendUtility.sendPacket(responder, SM_SYSTEM_MESSAGE.STR_CANNOT_REGISTER_BINDSTONE_HAVE_NO_AUTHORITY);
						return;
					}
					KiskService.onBind(kisk, responder);
				}
	
				@Override
				public void denyRequest(Creature requester, Player responder)
				{
					// Nothing Happens
				}
			};
			
			boolean requested = player.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_BIND_TO_KISK, responseHandler);
			if (requested)
			{
				PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_BIND_TO_KISK, player.getObjectId()));
			}
		}
		else if (kisk.getCurrentMemberCount() >= kisk.getMaxMembers())
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_REGISTER_BINDSTONE_FULL);
		}
		else
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_REGISTER_BINDSTONE_HAVE_NO_AUTHORITY);
		}
	}
	
}
