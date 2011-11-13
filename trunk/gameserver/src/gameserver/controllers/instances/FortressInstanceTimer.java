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


/**
 * @author HellBoy, Dns, Ritsu
 * 
 */

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
