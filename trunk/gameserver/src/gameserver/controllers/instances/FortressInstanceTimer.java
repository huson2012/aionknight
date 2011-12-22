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

package gameserver.controllers.instances;

import gameserver.controllers.NpcController;
import gameserver.model.NpcType;
import gameserver.model.gameobjects.AionObject;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.modifiers.Executor;
import gameserver.network.aion.serverpackets.SM_QUEST_ACCEPTED;
import gameserver.services.InstanceService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.WorldMapInstance;

public class FortressInstanceTimer extends NpcController
{
	public static void schedule(final Player player, int timeInSeconds)
	{
		if(!player.getQuestTimerOn() && player.isInInstance())
		{
			// usual delay is 15 minutes (inf' strat) 10 minutes (strat sup')
			final WorldMapInstance instance = InstanceService.getRegisteredInstance(player.getWorldId(), player.getPlayerGroup().getGroupId());
			instance.setTimerEnd(timeInSeconds);
			
			ThreadPoolManager.getInstance().schedule(new Runnable()
			{
				@Override
				public void run()
				{
					instance.doOnAllObjects(new Executor<AionObject>(){
						@Override
						public boolean run(AionObject obj)
						{
							if(obj instanceof Player)
								((Player)obj).setQuestTimerOn(false);
							else if(obj instanceof Npc && ((Npc)obj).getObjectTemplate().getNpcType() == NpcType.CHEST)
								((Npc)obj).getController().delete();
							return true;
						}
					}, true);
				}
			}, timeInSeconds * 1000);
			
			for(Player member : player.getPlayerGroup().getMembers())
			{
				if(!member.getQuestTimerOn() && member.getWorldId() == instance.getMapId() && member.getInstanceId() == instance.getInstanceId())
				{
					member.setQuestTimerOn(true);
					//member.getController().addTask(TaskId.QUEST_TIMER, task);
					PacketSendUtility.sendPacket(member, new SM_QUEST_ACCEPTED(4, 0, timeInSeconds));
				}
			}
		}
	}
}